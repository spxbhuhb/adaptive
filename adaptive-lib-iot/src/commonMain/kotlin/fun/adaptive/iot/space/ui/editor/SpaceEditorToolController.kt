package `fun`.adaptive.iot.space.ui.editor

import `fun`.adaptive.iot.space.ui.AbstractSpaceToolController
import `fun`.adaptive.iot.space.ui.SpaceTreeModel
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.workspace.Workspace

class SpaceEditorToolController(
    workspace: Workspace
) : AbstractSpaceToolController(workspace) {

    override fun selectedFun(viewModel: SpaceTreeModel, treeItem: TreeItem<AvValueId>, modifiers: Set<EventModifier>) {
        val item = valueTreeStore[treeItem.data] ?: return
        workspace.addContent(item, modifiers)
        TreeViewModel.defaultSelectedFun(viewModel, treeItem, modifiers)
    }

}