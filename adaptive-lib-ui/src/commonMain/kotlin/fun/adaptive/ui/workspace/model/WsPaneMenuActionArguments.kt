package `fun`.adaptive.ui.workspace.model

import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.workspace.Workspace

class WsPaneMenuActionArguments<T>(
    val workspace: Workspace,
    val pane: WsPane<*, *>,
    val menuItem: MenuItem<T>,
    val modifiers: Set<EventModifier>
)