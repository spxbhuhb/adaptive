package `fun`.adaptive.iot.space.ui.browser

import `fun`.adaptive.iot.item.AioItem
import `fun`.adaptive.iot.value.AioValueId
import `fun`.adaptive.ui.workspace.model.WsPane

fun WsPane<SpaceBrowserWsItem, *>.subSpaces(): List<AioValueId> {
    val browserItem = this.data
    val toolController = browserItem.config.controller
    return toolController.valueTreeStore.getSubSpaces(browserItem.item.uuid)
}

fun WsPane<SpaceBrowserWsItem, *>.getSpace(spaceId: AioValueId): AioItem? {
    val toolController = this.data.config.controller
    return toolController.valueTreeStore[spaceId]
}

fun SpaceBrowserWsItem.spacePathNames(): List<String> {
    val toolController = this.config.controller

    val spaceId = this.uuid
    val names = mutableListOf<String>()

    var space: AioItem? = toolController.valueTreeStore[spaceId]
    while (space != null) {
        names.add(space.name)
        space = space.parentId?.let { toolController.valueTreeStore[it] }
    }

    return names.reversed()
}