package `fun`.adaptive.iot.space.ui.browser

import `fun`.adaptive.iot.space.ui.AbstractSpaceToolController
import `fun`.adaptive.iot.space.ui.SpaceTreeModel
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.workspace.Workspace

class SpaceBrowserToolController(
    workspace: Workspace,
    val config: SpaceBrowserConfig
) : AbstractSpaceToolController(workspace) {

    override fun selectedFun(viewModel: SpaceTreeModel, treeItem: TreeItem<AvValueId>, modifiers: Set<EventModifier>) {
        val aioItem = valueTreeStore[treeItem.data] ?: return

        val browserItem = SpaceBrowserWsItem(
            aioItem.name,
            config.itemType,
            config,
            aioItem
        )

        workspace.addContent(browserItem, modifiers)

        TreeViewModel.Companion.defaultSelectedFun(viewModel, treeItem, modifiers)
    }

}