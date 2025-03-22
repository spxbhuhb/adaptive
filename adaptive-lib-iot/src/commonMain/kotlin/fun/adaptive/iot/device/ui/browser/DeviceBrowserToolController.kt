
package `fun`.adaptive.iot.device.ui.browser

import `fun`.adaptive.iot.device.ui.AbstractDeviceToolController
import `fun`.adaptive.iot.device.ui.DeviceTreeModel
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.workspace.Workspace

class DeviceBrowserToolController(
    workspace: Workspace,
    val config: DeviceBrowserConfig
) : AbstractDeviceToolController(workspace) {

    override fun selectedFun(viewModel: DeviceTreeModel, treeItem: TreeItem<AvValueId>, modifiers: Set<EventModifier>) {
        val aioItem = valueTreeStore[treeItem.data] ?: return

        val browserItem = DeviceBrowserWsItem(
            aioItem.name,
            config.itemType,
            config,
            aioItem
        )

        workspace.addContent(browserItem, modifiers)

        TreeViewModel.Companion.defaultSelectedFun(viewModel, treeItem, modifiers)
    }

}