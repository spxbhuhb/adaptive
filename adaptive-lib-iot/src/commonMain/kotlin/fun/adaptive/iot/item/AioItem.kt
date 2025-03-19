package `fun`.adaptive.iot.item

import `fun`.adaptive.adaptive_lib_iot.generated.resources.noname
import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.space.markers.SpaceMarkers
import `fun`.adaptive.iot.value.AioValue
import `fun`.adaptive.iot.value.AioValueId
import `fun`.adaptive.iot.ws.iconCache
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.builtin.empty
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.workspace.model.WsItemType
import `fun`.adaptive.utility.p04
import kotlinx.datetime.Instant

@Adat
data class AioItem(
    override val name: String,
    override val type: WsItemType,
    override val uuid: AioValueId,
    override val timestamp: Instant,
    override val status: AioStatus,
    val friendlyId: FriendlyItemId,
    val markersOrNull: AioMarkerMap?,
    val parentId: AioValueId?
) : AioValue() {

    val markers: AioMarkerMap
        get() = markersOrNull ?: emptyMap()

    operator fun contains(markerName : AioMarker) : Boolean {
        return markersOrNull?.containsKey(markerName) == true
    }

    operator fun get(markerName : AioMarker) : AioValueId? {
        return markersOrNull?.get(markerName)
    }

    fun copyWith(value: AioMarkerValue): AioItem {
        return copyWith(value.markerName, value)
    }

    fun copyWith(key: AioMarker, value: AioMarkerValue?): AioItem {
        val markers = markersOrNull?.toMutableMap() ?: mutableMapOf()
        markers[key] = value?.uuid
        return copy(markersOrNull = markers)
    }

    fun friendlySpaceId() {
        if (markersOrNull == null) return
        when {
            SpaceMarkers.SPACE in markersOrNull -> "SP-${friendlyId.p04}"
            else -> "XX-${friendlyId.p04}"
        }
    }

    private fun mapToTreeItem(
        parent: TreeItem<AioItem>?,
        childMap: MutableMap<AioItemId?, MutableList<AioItem>>
    ): TreeItem<AioItem> {

        TODO()
//        val item = toTreeItem(parent)
//
//        val children = childMap[uuid]
//        if (children == null) return item
//
//        item.children = children.map { child ->
//            child.mapToTreeItem(item, childMap)
//        }
//
//        return item
    }

    fun toTreeItem(parent: TreeItem<AioItem>?) = TreeItem(
        icon = icon(),
        title = name.ifEmpty { Strings.noname },
        data = this,
        parent = parent
    )

    fun icon(): GraphicsResourceSet {
        if (markersOrNull == null) return Graphics.empty
        for (marker in markersOrNull.keys) {
            val icon = iconCache[marker]
            if (icon != null) return icon
        }
        return Graphics.empty
    }

//    companion object {
//
//        fun List<AioItem>.toTree(): List<TreeItem<AioItem>> {
//
//            val childListMap = mutableMapOf<AioItemId?, MutableList<AioItem>>()
//
//            for (item in this) {
//                val children = childListMap.getOrPut(item.parentId) { mutableListOf() }
//                children += item
//            }
//
//            return childListMap[null]?.map { it.mapToTreeItem(null, childListMap) } ?: emptyList()
//        }
//
//    }
}