package `fun`.adaptive.cookbook

import `fun`.adaptive.cookbook.model.CbWsRecipeItem
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.ui.workspace.model.WsItem
import `fun`.adaptive.ui.workspace.model.WsPane

internal class CookbookPaneController(
    val context: CbWsContext,
) : WsPaneController<WsItem>() {

    override fun accepts(pane: WsPane<WsItem>, modifiers: Set<EventModifier>, item: WsItem): Boolean {
        if (item !is CbWsRecipeItem) return false
        if (EventModifier.ALT in modifiers) return false
        return true
    }

    override fun load(pane: WsPane<WsItem>, modifiers: Set<EventModifier>, item: WsItem) : WsPane<WsItem> {
        check(item is CbWsRecipeItem)
        context.activeRecipeKey.value = item.key
        return pane.copy(name = item.name, model = item, tooltip = item.tooltip)
    }

}