package `fun`.adaptive.cookbook

import `fun`.adaptive.cookbook.model.CbWsRecipeItem
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.ui.workspace.logic.WsPaneType
import `fun`.adaptive.ui.workspace.model.WsItem

internal class CookbookPaneController(
    val context: CbWsContext,
) : WsPaneController<WsItem>() {

    override fun accepts(pane: WsPaneType<WsItem>, modifiers: Set<EventModifier>, item: WsItem): Boolean {
        if (item !is CbWsRecipeItem) return false
        if (EventModifier.ALT in modifiers) return false
        return true
    }

    override fun load(pane: WsPaneType<WsItem>, modifiers: Set<EventModifier>, item: WsItem): WsPaneType<WsItem> {
        check(item is CbWsRecipeItem)
        context.activeRecipeKey.value = item.key
        return pane.copy(name = item.name, data = item, tooltip = pane.tooltip)
    }

}