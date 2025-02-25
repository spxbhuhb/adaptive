package `fun`.adaptive.grove.sheet

import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.WorkspaceContext

class SheetViewContext(
    override val workspace: Workspace
) : WorkspaceContext {
    val focusedView = storeFor<SheetViewController?> { null }
}