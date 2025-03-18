package `fun`.adaptive.iot.infrastructure

import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.ui.tree.TreeItem

suspend fun initInfrastructure(context: AioWsContext) {
    val list = context.infrastructureService.query()

    val childMap = mutableMapOf<AioInfrastructureItemId?, MutableList<AioInfrastructureItem>>()

    for (item in list) {
        context.infrastructureMap[item.uuid] = item
        val children = childMap.getOrPut(item.parentId) { mutableListOf() }
        children += item
    }

    context.infrastructureTree.items = childMap[null]?.map {
        it.mapToTreeItem(null, childMap)
    } ?: emptyList()

}

private fun AioInfrastructureItem.mapToTreeItem(
    parent: TreeItem<AioInfrastructureItem>?,
    childMap: MutableMap<AioInfrastructureItemId?, MutableList<AioInfrastructureItem>> = mutableMapOf()
): TreeItem<AioInfrastructureItem> =

    toTreeItem(parent).also { item ->

        val children = childMap[uuid]
        if (children == null) return@also

        item.children = children.map { child ->
            child.mapToTreeItem(item, childMap)
        }
    }