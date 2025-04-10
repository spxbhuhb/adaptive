package `fun`.adaptive.grove.sheet

import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.WsContext

class SheetViewContext(
    override val workspace: Workspace
) : WsContext {
    val focusedView = storeFor<SheetViewController?> { null }
}