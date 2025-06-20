package `fun`.adaptive.sandbox

import `fun`.adaptive.foundation.value.observableOf
import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.model.WsContext

class CbWsContext(
    override val workspace: MultiPaneWorkspace
) : WsContext {

    val activeRecipeKey = observableOf<String?> { null }

    companion object {
        const val RECIPES_TOOL_KEY = "cookbook:recipes"

        const val WSIT_CB_RECIPE = "cookbook:recipe"
        const val WSIT_CB_RECIPE_FOLDER = "cookbook:recipe-folder"
    }
}