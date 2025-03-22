package `fun`.adaptive.iot.device.ui.editor

import `fun`.adaptive.iot.device.ui.AbstractDeviceToolController
import `fun`.adaptive.iot.device.ui.DeviceTreeModel
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.workspace.Workspace

class DeviceEditorToolController(
    workspace: Workspace
) : AbstractDeviceToolController(workspace) {

    override fun selectedFun(viewModel: DeviceTreeModel, treeItem: TreeItem<AvValueId>, modifiers: Set<EventModifier>) {
        val item = valueTreeStore[treeItem.data] ?: return
        workspace.addContent(item, modifiers)
        TreeViewModel.defaultSelectedFun(viewModel, treeItem, modifiers)
    }

}