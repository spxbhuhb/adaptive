package `fun`.adaptive.ui.workspace.model

import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.menu.ContextMenuTheme
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.menu.MenuItemBase
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace

class WsPaneMenuAction<T>(
    icon: GraphicsResourceSet,
    tooltip: String,
    override val data: List<MenuItemBase<T>>,
    val selectedFun: (WsPaneMenuActionArguments<T>) -> Unit,
    val theme: ContextMenuTheme = ContextMenuTheme.DEFAULT,
) : AbstractWsPaneAction<List<MenuItemBase<T>>>(icon, tooltip) {

    override fun execute(workspace: MultiPaneWorkspace, pane: WsPane<*, *>) {
        throw UnsupportedOperationException()
    }

    fun selected(workspace: MultiPaneWorkspace, pane: WsPane<*, *>, menuItem: MenuItem<*>, modifiers: Set<EventModifier>) {
        @Suppress("UNCHECKED_CAST")
        selectedFun(
            WsPaneMenuActionArguments<T>(workspace, pane, menuItem as MenuItem<T>, modifiers)
        )
    }
}