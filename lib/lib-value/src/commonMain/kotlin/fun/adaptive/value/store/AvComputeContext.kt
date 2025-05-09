package `fun`.adaptive.value.store

import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.utility.p04
import `fun`.adaptive.value.AvSubscription
import `fun`.adaptive.value.AvValue2
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValue.Companion.withSpec
import `fun`.adaptive.value.item.AvRefList
import `fun`.adaptive.value.item.AvMarker
import `fun`.adaptive.value.operation.AvoAddOrUpdate

class AvComputeContext(
    val store: AvValueStore,
    val commitSet: MutableSet<AvSubscription>
) {

    operator fun plusAssign(value: AvValue2) {
        store.addOrUpdate(AvoAddOrUpdate(value), commitSet)
    }

    operator fun get(valueId: AvValueId): AvValue2? =
        store.unsafeValueOrNull(valueId)

    inline fun <reified T : Any> item(valueId: AvValueId): AvValue<T> =
        checkNotNull(itemOrNull(valueId)) { "cannot find item for $valueId" }.withSpec<T>()

    fun itemOrNull(valueId: AvValueId?): AvValue<*>? =
        valueId?.let { store.unsafeItem(valueId) }

    inline fun <reified T : Any> refItem(item: AvValue<*>, refMarker: AvMarker): AvValue<T> =
        checkNotNull(refItemOrNull(item, refMarker)) { "cannot find ref item for marker $refMarker in item $item" }
            .withSpec<T>()

    fun refItemOrNull(item: AvValue<*>, refMarker: AvMarker): AvValue<*>? =
        item.markersOrNull?.get(refMarker)?.let { store.unsafeItem(it) }

    fun nextFriendlyId(marker: AvMarker, prefix: String): String {
        var max = 0

        store.unsafeForEachItemByMarker(marker) { value ->
            val i = value.friendlyId.removePrefix(prefix).toIntOrNull()
            if (i != null && i > max) max = i
        }

        return "$prefix${(max + 1).p04}"
    }

    fun queryByMarker(marker: AvMarker): List<AvValue2> =
        store.unsafeQueryByMarker(marker)

    @Suppress("unused") // used for debugging
    fun dump(): String = store.dump()

    fun itemIdsOrNull(itemListValeId: AvValueId?): List<AvValueId>? =
        (store.unsafeValueOrNull(itemListValeId) as? AvRefList)?.refs

    fun safeItemIds(itemListValeId: AvValueId?): List<AvValueId> =
        itemIdsOrNull(itemListValeId) ?: emptyList()

    inline fun <reified T> markerVal(itemId: AvValueId, marker: AvMarker) =
        internalGetMarkerVal(itemId, marker) as T

    inline fun <reified T> markerValOrNull(itemId: AvValueId, marker: AvMarker) =
        internalGetMarkerVal(itemId, marker) as T?

    fun internalGetMarkerVal(itemId: AvValueId, marker: AvMarker) =
        store.unsafeGetMarkerValue(itemId, marker)

    inline fun <reified T> markerVal(item: AvValue<*>, marker: AvMarker) =
        internalGetMarkerVal(item, marker) as T

    inline fun <reified T> markerValOrNull(item: AvValue<*>, marker: AvMarker) =
        internalGetMarkerVal(item, marker) as T?

    fun internalGetMarkerVal(item: AvValue<*>, marker: AvMarker) =
        store.unsafeGetMarkerValue(item, marker)

    fun getContainingList(
        childId: AvValueId,
        childListMarker: AvMarker, topListMarker: AvMarker
    ): AvRefList? {
        val parentId = store.unsafeItem(childId).parentId

        val original: AvRefList?

        if (parentId == null) {
            original = queryByMarker(topListMarker).firstOrNull() as? AvRefList
        } else {
            original = markerVal(parentId, childListMarker)
        }

        return original
    }

    fun addTopList(
        spaceId: AvValueId,
        listMarker: AvMarker
    ) {
        val original = queryByMarker(listMarker).firstOrNull() as AvRefList?
        val new: AvRefList

        if (original != null) {
            new = original.copy(refs = original.refs + spaceId)
        } else {
            new = AvRefList(parentId = uuid7(), listMarker, listOf(spaceId))
        }

        this += new
    }

    fun addChild(
        parentId: AvValueId,
        childId: AvValueId,
        childListMarker: AvMarker
    ) {
        val original: AvRefList? = markerVal(parentId, childListMarker)

        if (original != null) {
            this += original.copy(refs = original.refs + childId)
            return
        }

        val new = AvRefList(parentId = parentId, childListMarker, listOf(childId))

        val parent = store.unsafeItem(parentId)

        val markers = parent.toMutableMarkers()

        markers[childListMarker] = new.uuid

        this += new
        this += parent.copy(markersOrNull = markers)
    }

    fun removeChild(
        parentId: AvValueId,
        childId: AvValueId,
        childListMarker: AvMarker
    ) {
        val original: AvRefList? = markerVal(parentId, childListMarker)

        if (original != null) {
            this += original.copy(refs = original.refs - childId)
        }
    }

    fun moveUp(
        childId: AvValueId,
        childListMarker: AvMarker,
        topListMarker: AvMarker
    ) {

        val original = getContainingList(childId, childListMarker, topListMarker) ?: return

        val originalList = original.refs.toMutableList()
        val index = originalList.indexOf(childId)
        if (index < 1) return

        val newList = originalList.toMutableList()
        newList[index] = newList[index - 1]
        newList[index - 1] = childId

        this += original.copy(refs = newList)
    }

    fun moveDown(
        childId: AvValueId,
        childListMarker: AvMarker,
        topListMarker: AvMarker
    ) {

        val original = getContainingList(childId, childListMarker, topListMarker) ?: return

        val originalList = original.refs.toMutableList()
        val index = originalList.indexOf(childId)
        if (index >= originalList.lastIndex) return

        val newList = originalList.toMutableList()
        newList[index] = newList[index + 1]
        newList[index + 1] = childId

        this += original.copy(refs = newList)
    }

    fun addRef(
        itemId: AvValueId,
        refMarker: AvMarker,
        refValueId: AvValueId,
        listMarker: AvMarker
    ) {
        val item = store.unsafeItem(itemId)
        val markers = item.toMutableMarkers()

        val ref = markers[refMarker]

        if (ref == refValueId) return

        if (ref != null) {
            removeChild(ref, itemId, listMarker)
        }

        markers[refMarker] = refValueId

        this += item.copy(markersOrNull = markers)

        addChild(refValueId, itemId, listMarker)
    }

    fun removeRef(
        itemId: AvValueId,
        refMarker: AvMarker,
        listMarker: AvMarker
    ) {
        val item = store.unsafeItem(itemId)
        val markers = item.toMutableMarkers()
        if (markers[refMarker] == null) return

        this += item.copy(markersOrNull = markers)

        val ref = markers.remove(refMarker) ?: return

        removeChild(itemId, ref, listMarker)
    }
}