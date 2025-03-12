package `fun`.adaptive.cookbook

import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.WsContext

class CbWsContext(
    override val workspace: Workspace
) : WsContext {

    val activeRecipeKey = storeFor<String?> { null }

    companion object {
        const val RECIPES_TOOL_KEY = "cookbook:recipes"

        const val RECIPE_ITEM_TYPE = "cookbook:recipe"
        const val RECIPE_FOLDER_TYPE = "cookbook:recipe-folder"
    }
}