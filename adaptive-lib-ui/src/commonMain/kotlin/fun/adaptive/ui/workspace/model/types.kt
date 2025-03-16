package `fun`.adaptive.ui.workspace.model

import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.utility.UUID

typealias WsItemType = String
typealias WsItemTooltip = String
typealias WsPaneId = UUID<WsPane<*>>

typealias ActionMenuItemFun<T> = (
    workspace: Workspace,
    pane: WsPane<*>,
    menuItem: MenuItem<T>,
    modifiers: Set<EventModifier>
) -> Unit
