package `fun`.adaptive.iot.history.ui

import `fun`.adaptive.ui.value.AvUiTree
import `fun`.adaptive.value.item.AvItem

fun historyPathNames(controller : HistoryToolController, item : AvItem<*>): List<String> {

    val parentId = item.parentId ?: return emptyList()

    var space: AvItem<*>? = controller.spaceTreeStore[parentId]
    var device: AvItem<*>? = controller.deviceTreeStore[parentId]

    when {
        space != null -> collect(space, controller.spaceTreeStore)
        device != null -> collect(device, controller.deviceTreeStore)
        else -> emptyList()
    }.also {
        return it
    }
}

private fun collect(start : AvItem<*>?, tree: AvUiTree) : List<String> {
    val names = mutableListOf<String>()
    var item = start

    while (item != null) {
        names.add(item.name)
        item = item.parentId?.let { tree[it] }
    }

    return names.reversed()

}