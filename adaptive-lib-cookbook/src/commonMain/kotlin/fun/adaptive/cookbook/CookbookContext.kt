package `fun`.adaptive.cookbook

import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.WsContext

class CookbookContext(
    override val workspace: Workspace
) : WsContext {
    val activeRecipeKey = storeFor<String?> { null }
}