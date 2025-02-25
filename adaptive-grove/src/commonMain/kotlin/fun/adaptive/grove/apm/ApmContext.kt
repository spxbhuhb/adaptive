package `fun`.adaptive.grove.apm

import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.WorkspaceContext

class ApmContext(
    override val workspace: Workspace
) : WorkspaceContext {
    val activeRecipeKey = storeFor<String?> { null }
}