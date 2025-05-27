package `fun`.adaptive.sandbox.app

import `fun`.adaptive.sandbox.*
import `fun`.adaptive.cookbook.generated.resources.dining
import `fun`.adaptive.cookbook.generated.resources.flatware
import `fun`.adaptive.sandbox.recipe.ui.layout.workspace.WorkspaceRecipePaneFragmentFactory
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.sandbox.model.CbWsRecipeItem
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.backends.UnitPaneViewBackend
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.ui.mpw.model.PanePosition
import `fun`.adaptive.utility.UUID

class CookbookWsModule<WT : MultiPaneWorkspace> : CookbookModule<WT>() {

    override fun frontendAdapterInit(adapter : AdaptiveAdapter)= with(adapter) {
        + CookbookFragmentFactory
        + WorkspaceRecipePaneFragmentFactory
    }

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {

        val context = CbWsContext(this)

        contexts += context

        toolPanes += PaneDef(
            UUID(),
            "Cookbook",
            Graphics.flatware,
            PanePosition.LeftMiddle,
            cookbookRecipePaneKey
        )

        addContentPaneBuilder(CbWsContext.WSIT_CB_RECIPE) { item ->
            item as CbWsRecipeItem

            PaneDef(
                UUID(),
                workspace = workspace,
                item.name,
                context[item].icon,
                PanePosition.Center,
                "cookbook:center",
                viewBackend = CookbookPaneViewBackend(workspace, context)
            )
        }

        addItemConfig(CbWsContext.WSIT_CB_RECIPE, Graphics.dining)
        addItemConfig(CbWsContext.WSIT_CB_RECIPE_FOLDER, Graphics.dining)

    }
}