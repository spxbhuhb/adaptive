package `fun`.adaptive.grove.doc.ws.browser

import `fun`.adaptive.grove.doc.model.GroveDocSpec
import `fun`.adaptive.grove.doc.model.GroveDocValue
import `fun`.adaptive.grove.doc.model.avDomain
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.generated.resources.menu_book
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace
import `fun`.adaptive.ui.workspace.logic.WsPaneViewBackend
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPaneItem
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValue.Companion.checkSpec
import `fun`.adaptive.value.util.checkValue

class DocBrowserContentViewBackend(
    override val workspace: MultiPaneWorkspace,
    var value : GroveDocValue
) : WsPaneViewBackend<DocBrowserContentViewBackend>() {

    override fun accepts(pane: WsPane<DocBrowserContentViewBackend>, modifiers: Set<EventModifier>, item: WsPaneItem): Boolean {
        return item is AvValue<*> && item.spec is GroveDocSpec && avDomain.node in item.markers
    }

    override fun load(pane: WsPane<DocBrowserContentViewBackend>, modifiers: Set<EventModifier>, item: WsPaneItem): WsPane<DocBrowserContentViewBackend> {

        value = checkValue(item).checkSpec()

        return pane.copy(
            name = item.nameLike,
            icon = Graphics.menu_book
        )
    }

}