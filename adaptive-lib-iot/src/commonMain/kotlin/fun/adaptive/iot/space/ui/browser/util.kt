package `fun`.adaptive.iot.space.ui.browser

import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.ui.workspace.model.WsPane

fun WsPane<SpaceBrowserWsItem, *>.subSpaces(): List<AvValueId> {
    val browserItem = this.data
    val toolController = browserItem.config.controller
    return toolController.valueTreeStore.getSubItems(browserItem.item.uuid)
}

fun WsPane<SpaceBrowserWsItem, *>.getSpace(spaceId: AvValueId): AvItem? {
    val toolController = this.data.config.controller
    return toolController.valueTreeStore[spaceId]
}

fun SpaceBrowserWsItem.spacePathNames(): List<String> {
    val toolController = this.config.controller

    val spaceId = this.uuid
    val names = mutableListOf<String>()

    var space: AvItem? = toolController.valueTreeStore[spaceId]
    while (space != null) {
        names.add(space.name)
        space = space.parentId?.let { toolController.valueTreeStore[it] }
    }

    return names.reversed()
}