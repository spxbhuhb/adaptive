package `fun`.adaptive.iot.item

import `fun`.adaptive.adaptive_lib_iot.generated.resources.noname
import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.space.SpaceMarkers
import `fun`.adaptive.iot.ws.iconCache
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.builtin.empty
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.workspace.model.WsItem
import `fun`.adaptive.ui.workspace.model.WsItemType
import `fun`.adaptive.utility.p04

@Adat
data class AioItem(
    override val name: String,
    override val type: WsItemType,
    val uuid: AioItemId,
    val friendlyId: FriendlyItemId,
    val markers: AioMarkerMap,
    val displayOrder: Int,
    val status: AioStatus,
    val parentId: AioItemId?
) : WsItem() {

    fun friendlySpaceId() {
        when {
            SpaceMarkers.SPACE in markers -> "SP-${friendlyId.p04}"
            else -> "XX-${friendlyId.p04}"
        }
    }

    private fun mapToTreeItem(
        parent: TreeItem<AioItem>?,
        childMap: MutableMap<AioItemId?, MutableList<AioItem>>
    ): TreeItem<AioItem> {

        val item = toTreeItem(parent)

        val children = childMap[uuid]
        if (children == null) return item

        children.sortBy { it.displayOrder }

        item.children = children.map { child ->
            child.mapToTreeItem(item, childMap)
        }

        return item
    }

    fun toTreeItem(parent: TreeItem<AioItem>?) = TreeItem(
        icon = icon(),
        title = name.ifEmpty { Strings.noname },
        data = this,
        parent = parent
    )

    fun icon(): GraphicsResourceSet {
        for (marker in markers.keys) {
            val icon = iconCache[marker]
            if (icon != null) return icon
        }
        return Graphics.empty
    }

    companion object {

        fun List<AioItem>.toTree(): List<TreeItem<AioItem>> {

            val childListMap = mutableMapOf<AioItemId?, MutableList<AioItem>>()

            for (item in this) {
                val children = childListMap.getOrPut(item.parentId) { mutableListOf() }
                children += item
            }

            return childListMap[null]?.map { it.mapToTreeItem(null, childListMap) } ?: emptyList()
        }

    }
}