package `fun`.adaptive.sandbox

import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace
import `fun`.adaptive.ui.workspace.model.WsContext

class CbWsContext(
    override val workspace: MultiPaneWorkspace
) : WsContext {

    val activeRecipeKey = storeFor<String?> { null }

    companion object {
        const val RECIPES_TOOL_KEY = "cookbook:recipes"

        const val WSIT_CB_RECIPE = "cookbook:recipe"
        const val WSIT_CB_RECIPE_FOLDER = "cookbook:recipe-folder"
    }
}