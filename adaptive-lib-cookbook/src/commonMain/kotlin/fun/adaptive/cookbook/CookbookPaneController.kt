package `fun`.adaptive.cookbook

import `fun`.adaptive.cookbook.model.CbWsRecipeItem
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.ui.workspace.logic.WsPaneType
import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.ui.workspace.Workspace

internal class CookbookPaneController(
    override val workspace: Workspace,
    val context: CbWsContext,
) : WsPaneController<NamedItem>() {

    override fun accepts(pane: WsPaneType<NamedItem>, modifiers: Set<EventModifier>, item: NamedItem): Boolean {
        if (item !is CbWsRecipeItem) return false
        if (EventModifier.ALT in modifiers) return false
        return true
    }

    override fun load(pane: WsPaneType<NamedItem>, modifiers: Set<EventModifier>, item: NamedItem): WsPaneType<NamedItem> {
        check(item is CbWsRecipeItem)
        context.activeRecipeKey.value = item.key
        return pane.copy(name = item.name, data = item, tooltip = pane.tooltip)
    }

}