package `fun`.adaptive.ui.mpw.model

import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.menu.MenuTheme
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.menu.MenuItemBase
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace

class PaneMenuAction<T>(
    icon: GraphicsResourceSet,
    tooltip: String,
    val data: List<MenuItemBase<T>>,
    val selectedFun: (PaneMenuActionArguments<T>) -> Unit,
    val theme: MenuTheme = MenuTheme.DEFAULT,
) : AbstractPaneAction(icon, tooltip) {

    override fun execute() {
        throw UnsupportedOperationException()
    }

    fun selected(workspace: MultiPaneWorkspace, pane: PaneDef, menuItem: MenuItem<*>, modifiers: Set<EventModifier>) {
        @Suppress("UNCHECKED_CAST")
        selectedFun(
            PaneMenuActionArguments(workspace, pane, menuItem as MenuItem<T>, modifiers)
        )
    }
}