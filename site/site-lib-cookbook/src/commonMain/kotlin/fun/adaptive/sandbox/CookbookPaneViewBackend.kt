package `fun`.adaptive.sandbox

import `fun`.adaptive.sandbox.model.CbWsRecipeItem
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.backends.PaneViewBackend
import `fun`.adaptive.ui.mpw.model.Pane
import `fun`.adaptive.ui.mpw.model.WsPaneItem

internal class CookbookPaneViewBackend(
    override val workspace: MultiPaneWorkspace,
    val context: CbWsContext,
    var item : CbWsRecipeItem? = null
) : PaneViewBackend<CookbookPaneViewBackend>() {

    override fun accepts(pane: Pane<CookbookPaneViewBackend>, modifiers: Set<EventModifier>, item: WsPaneItem): Boolean {
        if (item !is CbWsRecipeItem) return false
        if (EventModifier.ALT in modifiers) return false
        return true
    }

    override fun load(pane: Pane<CookbookPaneViewBackend>, modifiers: Set<EventModifier>, item: WsPaneItem): Pane<CookbookPaneViewBackend> {
        check(item is CbWsRecipeItem)
        this.item = item
        context.activeRecipeKey.value = item.key
        return pane.copy(name = item.name, tooltip = pane.tooltip)
    }

}