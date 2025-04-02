package `fun`.adaptive.cookbook.app

import `fun`.adaptive.cookbook.*
import `fun`.adaptive.cookbook.recipe.ui.layout.workspace.WorkspaceRecipePaneFragmentFactory
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsUnitPaneController
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

class CookbookWsModule<WT : Workspace> : CookbookModule<WT>() {

    override fun frontendAdapterInit(adapter : AdaptiveAdapter)= with(adapter) {
        + CookbookFragmentFactory
        + WorkspaceRecipePaneFragmentFactory
    }

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {

        val context = CbWsContext(this)

        contexts += context

        toolPanes += WsPane(
            UUID(),
            workspace = workspace,
            "Cookbook",
            Graphics.flatware,
            WsPanePosition.LeftMiddle,
            cookbookRecipePaneKey,
            data = Unit,
            controller = WsUnitPaneController(this)
        )

        addContentPaneBuilder(CbWsContext.WSIT_CB_RECIPE) { item ->
            WsPane(
                UUID(),
                workspace = workspace,
                item.name,
                context[item].icon,
                WsPanePosition.Center,
                "cookbook:center",
                data = item,
                controller = CookbookPaneController(workspace, context)
            )
        }

        addItemConfig(CbWsContext.WSIT_CB_RECIPE, Graphics.dining)
        addItemConfig(CbWsContext.WSIT_CB_RECIPE_FOLDER, Graphics.dining)

    }
}