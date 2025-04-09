package `fun`.adaptive.iot.domain.rht.ui.controller

import `fun`.adaptive.iot.space.ui.AbstractSpaceToolController
import `fun`.adaptive.iot.space.ui.SpaceTreeModel
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.value.AvValueId

class RhtBrowserToolController(
    workspace: Workspace
) : AbstractSpaceToolController(workspace) {

    override fun selectedFun(viewModel: SpaceTreeModel, treeItem: TreeItem<AvValueId>, modifiers: Set<EventModifier>) {
        val aioItem = valueTreeStore[treeItem.data] ?: return

        val browserItem = RhtWsItem(
            aioItem.name,
            this,
            aioItem
        )

        workspace.addContent(browserItem, modifiers)

        TreeViewModel.defaultSelectedFun(viewModel, treeItem, modifiers)
    }

}