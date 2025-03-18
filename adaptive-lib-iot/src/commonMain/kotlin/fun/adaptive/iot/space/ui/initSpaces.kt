package `fun`.adaptive.iot.space.ui

import `fun`.adaptive.iot.space.AioSpaceId
import `fun`.adaptive.iot.space.AioSpace
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.ui.tree.TreeItem

suspend fun initSpaces(context: AioWsContext) {
    val list = context.spaceService.query()

    val childMap = mutableMapOf<AioSpaceId?, MutableList<AioSpace>>()

    for (space in list) {
        context.spaceMap[space.uuid] = space
        val children = childMap.getOrPut(space.parentId) { mutableListOf() }
        children += space
    }

    context.spaceTree.items = childMap[null]?.map {
        it.mapToTreeItem(null, childMap)
    } ?: emptyList()

}

private fun AioSpace.mapToTreeItem(
    parent: TreeItem<AioSpace>?,
    childMap: MutableMap<AioSpaceId?, MutableList<AioSpace>> = mutableMapOf()
): TreeItem<AioSpace> =

    toTreeItem(parent).also { item ->

        val children = childMap[uuid]
        if (children == null) return@also

        children.sortBy { it.displayOrder }

        item.children = children.map { child ->
            child.mapToTreeItem(item, childMap)
        }
    }