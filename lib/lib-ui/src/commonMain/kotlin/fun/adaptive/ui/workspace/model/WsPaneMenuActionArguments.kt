package `fun`.adaptive.ui.workspace.model

import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace

class WsPaneMenuActionArguments<T>(
    val workspace: MultiPaneWorkspace,
    val pane: WsPane<*>,
    val menuItem: MenuItem<T>,
    val modifiers: Set<EventModifier>
)