package `fun`.adaptive.document.ws.browser

import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValue

fun WsPane<DocBrowserWsItem, *>.subDocuments(): List<AvValueId> {
    val browserItem = this.data
    val toolController = browserItem.config.controller
    return toolController.valueTreeStore.getChildrenIds(browserItem.item.uuid)
}

fun WsPane<DocBrowserWsItem, *>.getDocument(spaceId: AvValueId): AvValue<*>? {
    val toolController = this.data.config.controller
    return toolController.valueTreeStore[spaceId]
}

fun DocBrowserWsItem.docPathNames(): List<String> {
    return this.config.controller.valueTreeStore.pathNames(this.item)
}