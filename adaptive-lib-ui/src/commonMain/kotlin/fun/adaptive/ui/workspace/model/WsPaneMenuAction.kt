package `fun`.adaptive.ui.workspace.model

import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.menu.ContextMenuTheme
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.menu.MenuItemBase
import `fun`.adaptive.ui.menu.MenuItemSelectedFun
import `fun`.adaptive.ui.workspace.Workspace

class WsPaneMenuAction<T>(
    icon: GraphicsResourceSet,
    tooltip: String,
    override val data: List<MenuItemBase<T>>,
    val selectedFun: ActionMenuItemFun<T>,
    val theme: ContextMenuTheme = ContextMenuTheme.DEFAULT,
) : AbstractWsPaneAction<List<MenuItemBase<T>>>(icon, tooltip) {

    override fun execute(workspace: Workspace, pane: WsPane<*>) {
        throw UnsupportedOperationException()
    }

    fun selected(workspace: Workspace, pane: WsPane<*>, menuItem: MenuItem<*>, modifiers: Set<EventModifier>) {
        @Suppress("UNCHECKED_CAST")
        selectedFun(workspace, pane, menuItem as MenuItem<T>, modifiers)
    }
}