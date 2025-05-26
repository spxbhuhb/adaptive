package `fun`.adaptive.sandbox

import `fun`.adaptive.sandbox.model.CbWsRecipeItem
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace
import `fun`.adaptive.ui.workspace.logic.WsPaneViewBackend
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPaneItem

internal class CookbookPaneViewBackend(
    override val workspace: MultiPaneWorkspace,
    val context: CbWsContext,
    var item : CbWsRecipeItem? = null
) : WsPaneViewBackend<CookbookPaneViewBackend>() {

    override fun accepts(pane: WsPane<CookbookPaneViewBackend>, modifiers: Set<EventModifier>, item: WsPaneItem): Boolean {
        if (item !is CbWsRecipeItem) return false
        if (EventModifier.ALT in modifiers) return false
        return true
    }

    override fun load(pane: WsPane<CookbookPaneViewBackend>, modifiers: Set<EventModifier>, item: WsPaneItem): WsPane<CookbookPaneViewBackend> {
        check(item is CbWsRecipeItem)
        this.item = item
        context.activeRecipeKey.value = item.key
        return pane.copy(name = item.name, tooltip = pane.tooltip)
    }

}