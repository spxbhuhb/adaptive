package `fun`.adaptive.cookbook

import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.ui.workspace.model.Workspace
import `fun`.adaptive.ui.workspace.model.WorkspaceContext

class CookbookContext(
    override val workspace: Workspace
) : WorkspaceContext {
    val activeRecipeKey = storeFor<String?> { null }
}