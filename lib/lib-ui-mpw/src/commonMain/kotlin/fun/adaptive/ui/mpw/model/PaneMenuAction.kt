package `fun`.adaptive.ui.mpw.model

import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.menu.ContextMenuTheme
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.menu.MenuItemBase
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace

class PaneMenuAction<T>(
    icon: GraphicsResourceSet,
    tooltip: String,
    override val data: List<MenuItemBase<T>>,
    val selectedFun: (PaneMenuActionArguments<T>) -> Unit,
    val theme: ContextMenuTheme = ContextMenuTheme.DEFAULT,
) : AbstractPaneAction<List<MenuItemBase<T>>>(icon, tooltip) {

    override fun execute(workspace: MultiPaneWorkspace, pane: Pane<*>) {
        throw UnsupportedOperationException()
    }

    fun selected(workspace: MultiPaneWorkspace, pane: Pane<*>, menuItem: MenuItem<*>, modifiers: Set<EventModifier>) {
        @Suppress("UNCHECKED_CAST")
        selectedFun(
            PaneMenuActionArguments<T>(workspace, pane, menuItem as MenuItem<T>, modifiers)
        )
    }
}