package `fun`.adaptive.grove.apm

import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.ui.workspace.Workspace

class ApmContext(
    val workspace: Workspace
) {
    val activeRecipeKey = storeFor<String?> { null }
}