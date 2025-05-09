package `fun`.adaptive.value.store

import `fun`.adaptive.utility.p04
import `fun`.adaptive.value.AvMarker
import `fun`.adaptive.value.AvSubscription
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValue.Companion.withSpec
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.operation.AvoAddOrUpdate

class AvComputeContext(
    val store: AvValueStore,
    val commitSet: MutableSet<AvSubscription>
) {

    operator fun plusAssign(value: AvValue<*>) {
        store.addOrUpdate(AvoAddOrUpdate(value), commitSet)
    }

    inline fun <reified T : Any> get(valueId: AvValueId): AvValue<T> =
        checkNotNull(getOrNull(valueId)) { "cannot find item for id $valueId" }.withSpec<T>()

    fun getOrNull(valueId: AvValueId?): AvValue<*>? =
        store.unsafeGetOrNull(valueId)

    inline fun <reified T : Any> ref(value: AvValue<*>, refMarker: AvMarker): AvValue<T> =
        checkNotNull(refOrNull(value, refMarker)) { "cannot find ref item for marker $refMarker in item ${value.uuid}" }
            .withSpec<T>()

    fun refOrNull(value: AvValue<*>, refMarker: AvMarker): AvValue<*>? =
        store.unsafeRefOrNull(value, refMarker)

    fun nextFriendlyId(marker: AvMarker, prefix: String): String {
        var max = 0

        store.unsafeForEachItemByMarker(marker) { value ->
            val i = value.friendlyId?.removePrefix(prefix)?.toIntOrNull()
            if (i != null && i > max) max = i
        }

        return "$prefix${(max + 1).p04}"
    }

    fun queryByMarker(marker: AvMarker): List<AvValue<*>> =
        store.unsafeQueryByMarker(marker)

    @Suppress("unused") // used for debugging
    fun dump(): String = store.dump()

//    fun getContainingList(
//        childId: AvValueId,
//        childListMarker: AvMarker, topListMarker: AvMarker
//    ): AvRefList? {
//        val parentId = store.unsafeItem(childId).parentId
//
//        val original: AvRefList?
//
//        if (parentId == null) {
//            original = queryByMarker(topListMarker).firstOrNull() as? AvRefList
//        } else {
//            original = markerVal(parentId, childListMarker)
//        }
//
//        return original
//    }
//
//    fun addTopList(
//        spaceId: AvValueId,
//        listMarker: AvMarker
//    ) {
//        val original = queryByMarker(listMarker).firstOrNull() as AvRefList?
//        val new: AvRefList
//
//        if (original != null) {
//            new = original.copy(refs = original.refs + spaceId)
//        } else {
//            new = AvRefList(parentId = uuid7(), listMarker, listOf(spaceId))
//        }
//
//        this += new
//    }
//
//    fun addChild(
//        parentId: AvValueId,
//        childId: AvValueId,
//        childListMarker: AvMarker
//    ) {
//        val original: AvRefList? = markerVal(parentId, childListMarker)
//
//        if (original != null) {
//            this += original.copy(refs = original.refs + childId)
//            return
//        }
//
//        val new = AvRefList(parentId = parentId, childListMarker, listOf(childId))
//
//        val parent = store.unsafeItem(parentId)
//
//        val markers = parent.toMutableMarkers()
//
//        markers[childListMarker] = new.uuid
//
//        this += new
//        this += parent.copy(markersOrNull = markers)
//    }
//
//    fun removeChild(
//        parentId: AvValueId,
//        childId: AvValueId,
//        childListMarker: AvMarker
//    ) {
//        val original: AvRefList? = markerVal(parentId, childListMarker)
//
//        if (original != null) {
//            this += original.copy(refs = original.refs - childId)
//        }
//    }
//
//    fun moveUp(
//        childId: AvValueId,
//        childListMarker: AvMarker,
//        topListMarker: AvMarker
//    ) {
//
//        val original = getContainingList(childId, childListMarker, topListMarker) ?: return
//
//        val originalList = original.refs.toMutableList()
//        val index = originalList.indexOf(childId)
//        if (index < 1) return
//
//        val newList = originalList.toMutableList()
//        newList[index] = newList[index - 1]
//        newList[index - 1] = childId
//
//        this += original.copy(refs = newList)
//    }
//
//    fun moveDown(
//        childId: AvValueId,
//        childListMarker: AvMarker,
//        topListMarker: AvMarker
//    ) {
//
//        val original = getContainingList(childId, childListMarker, topListMarker) ?: return
//
//        val originalList = original.refs.toMutableList()
//        val index = originalList.indexOf(childId)
//        if (index >= originalList.lastIndex) return
//
//        val newList = originalList.toMutableList()
//        newList[index] = newList[index + 1]
//        newList[index + 1] = childId
//
//        this += original.copy(refs = newList)
//    }
//
//    fun addRef(
//        valueId: AvValueId,
//        refName: AvMarker,
//        refValueId: AvValueId
//    ) {
//        val value = store.unsafeGet(valueId)
//        val refs = value.toMutableRefs()
//
//        val ref = refs[refName]
//        if (ref == refValueId) return
//
//        if (ref != null) {
//            removeChild(ref, itemId, listMarker)
//        }
//
//        markers[refMarker] = refValueId
//
//        this += item.copy(markersOrNull = markers)
//
//        addChild(refValueId, itemId, listMarker)
//    }

}