package `fun`.adaptive.cookbook

import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.ui.workspace.Workspace

class CookbookContext(
    val workspace: Workspace
) {
    val activeRecipeKey = storeFor<String?> { null }
}