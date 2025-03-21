package `fun`.adaptive.document.ws.browser

import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.item.AvItem

fun WsPane<DocBrowserWsItem, *>.subDocuments(): List<AvValueId> {
    val browserItem = this.data
    val toolController = browserItem.config.controller
    return toolController.valueTreeStore.getSubSpaces(browserItem.item.uuid)
}

fun WsPane<DocBrowserWsItem, *>.getDocument(spaceId: AvValueId): AvItem? {
    val toolController = this.data.config.controller
    return toolController.valueTreeStore[spaceId]
}

fun DocBrowserWsItem.docPathNames(): List<String> {
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