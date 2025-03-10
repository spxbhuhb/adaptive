package `fun`.adaptive.grove.apm

import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.WsContext

class ApmContext(
    override val workspace: Workspace
) : WsContext {
    val activeRecipeKey = storeFor<String?> { null }
}