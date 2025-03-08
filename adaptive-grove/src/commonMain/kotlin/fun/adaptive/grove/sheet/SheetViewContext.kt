package `fun`.adaptive.grove.sheet

import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.ui.workspace.model.Workspace
import `fun`.adaptive.ui.workspace.model.WorkspaceContext

class SheetViewContext(
    override val workspace: Workspace
) : WorkspaceContext {
    val focusedView = storeFor<SheetViewController?> { null }
}