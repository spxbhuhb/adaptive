package `fun`.adaptive.ui.mpw.model

import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace

class PaneMenuActionArguments<T>(
    val workspace: MultiPaneWorkspace,
    val pane: PaneDef<*>,
    val menuItem: MenuItem<T>,
    val modifiers: Set<EventModifier>
)