package `fun`.adaptive.value.store

import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.utility.p04
import `fun`.adaptive.value.AvSubscription
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvItem.Companion.withSpec
import `fun`.adaptive.value.item.AvItemIdList
import `fun`.adaptive.value.item.AvMarker
import `fun`.adaptive.value.operation.AvoAddOrUpdate

class AvComputeContext(
    val store: AvValueStore,
    val commitSet: MutableSet<AvSubscription>
) {

    operator fun plusAssign(value: AvValue) {
        store.addOrUpdate(AvoAddOrUpdate(value), commitSet)
    }

    operator fun get(valueId: AvValueId): AvValue? =
        store.unsafeValueOrNull(valueId)

    inline fun <reified T : Any> item(valueId: AvValueId): AvItem<T> =
        checkNotNull(itemOrNull(valueId)) { "cannot find item for $valueId" }.withSpec<T>()

    fun itemOrNull(valueId: AvValueId?): AvItem<*>? =
        valueId?.let { store.unsafeItem(valueId) }

    inline fun <reified T : Any> refItem(item: AvItem<*>, refMarker: AvMarker): AvItem<T> =
        checkNotNull(refItemOrNull(item, refMarker)) { "cannot find ref item for marker $refMarker in item $item" }
            .withSpec<T>()

    fun refItemOrNull(item: AvItem<*>, refMarker: AvMarker): AvItem<*>? =
        item.markersOrNull?.get(refMarker)?.let { store.unsafeItem(it) }

    fun nextFriendlyId(marker: AvMarker, prefix: String): String {
        var max = 0

        store.unsafeForEachItemByMarker(marker) { value ->
            val i = value.friendlyId.removePrefix(prefix).toIntOrNull()
            if (i != null && i > max) max = i
        }

        return "$prefix${(max + 1).p04}"
    }

    fun queryByMarker(marker: AvMarker): List<AvValue> =
        store.unsafeQueryByMarker(marker)

    @Suppress("unused") // used for debugging
    fun dump(): String = store.dump()

    fun itemIdsOrNull(itemListValeId: AvValueId?): List<AvValueId>? =
        (store.unsafeValueOrNull(itemListValeId) as? AvItemIdList)?.itemIds

    fun safeItemIds(itemListValeId: AvValueId?): List<AvValueId> =
        itemIdsOrNull(itemListValeId) ?: emptyList()

    inline fun <reified T> markerVal(itemId: AvValueId, marker: AvMarker) =
        internalGetMarkerVal(itemId, marker) as T

    inline fun <reified T> markerValOrNull(itemId: AvValueId, marker: AvMarker) =
        internalGetMarkerVal(itemId, marker) as T?

    fun internalGetMarkerVal(itemId: AvValueId, marker: AvMarker) =
        store.unsafeGetMarkerValue(itemId, marker)

    inline fun <reified T> markerVal(item: AvItem<*>, marker: AvMarker) =
        internalGetMarkerVal(item, marker) as T

    inline fun <reified T> markerValOrNull(item: AvItem<*>, marker: AvMarker) =
        internalGetMarkerVal(item, marker) as T?

    fun internalGetMarkerVal(item: AvItem<*>, marker: AvMarker) =
        store.unsafeGetMarkerValue(item, marker)

    fun getContainingList(
        childId: AvValueId,
        childListMarker: AvMarker, topListMarker: AvMarker
    ): AvItemIdList? {
        val parentId = store.unsafeItem(childId).parentId

        val original: AvItemIdList?

        if (parentId == null) {
            original = queryByMarker(topListMarker).firstOrNull() as? AvItemIdList
        } else {
            original = markerVal(parentId, childListMarker)
        }

        return original
    }

    fun addTopList(
        spaceId: AvValueId,
        listMarker: AvMarker
    ) {
        val original = queryByMarker(listMarker).firstOrNull() as AvItemIdList?
        val new: AvItemIdList

        if (original != null) {
            new = original.copy(itemIds = original.itemIds + spaceId)
        } else {
            new = AvItemIdList(parentId = uuid7(), listMarker, listOf(spaceId))
        }

        this += new
    }

    fun addChild(
        parentId: AvValueId,
        childId: AvValueId,
        childListMarker: AvMarker
    ) {
        val original: AvItemIdList? = markerVal(parentId, childListMarker)

        if (original != null) {
            this += original.copy(itemIds = original.itemIds + childId)
            return
        }

        val new = AvItemIdList(parentId = parentId, childListMarker, listOf(childId))

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
        val original: AvItemIdList? = markerVal(parentId, childListMarker)

        if (original != null) {
            this += original.copy(itemIds = original.itemIds - childId)
        }
    }

    fun moveUp(
        childId: AvValueId,
        childListMarker: AvMarker,
        topListMarker: AvMarker
    ) {

        val original = getContainingList(childId, childListMarker, topListMarker) ?: return

        val originalList = original.itemIds.toMutableList()
        val index = originalList.indexOf(childId)
        if (index < 1) return

        val newList = originalList.toMutableList()
        newList[index] = newList[index - 1]
        newList[index - 1] = childId

        this += original.copy(itemIds = newList)
    }

    fun moveDown(
        childId: AvValueId,
        childListMarker: AvMarker,
        topListMarker: AvMarker
    ) {

        val original = getContainingList(childId, childListMarker, topListMarker) ?: return

        val originalList = original.itemIds.toMutableList()
        val index = originalList.indexOf(childId)
        if (index >= originalList.lastIndex) return

        val newList = originalList.toMutableList()
        newList[index] = newList[index + 1]
        newList[index + 1] = childId

        this += original.copy(itemIds = newList)
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