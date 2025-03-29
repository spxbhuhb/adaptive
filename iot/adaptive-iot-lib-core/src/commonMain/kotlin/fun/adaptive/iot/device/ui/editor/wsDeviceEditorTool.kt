package `fun`.adaptive.iot.device.ui.editor

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.iot.device.DeviceMarkers
import `fun`.adaptive.iot.device.ui.DeviceTreeModel
import `fun`.adaptive.iot.generated.resources.*
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.zIndex
import `fun`.adaptive.ui.builtin.*
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.menu.MenuItemBase
import `fun`.adaptive.ui.menu.MenuSeparator
import `fun`.adaptive.ui.menu.contextMenu
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.tree
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPaneAction
import `fun`.adaptive.ui.workspace.model.WsPaneMenuAction
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.ui.workspace.wsToolPane
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.item.AvItem.Companion.asAvItem

@Adaptive
fun wsDeviceEditorTool(pane: WsPane<Unit, DeviceEditorToolController>): AdaptiveFragment {

    val observed = valueFrom { pane.controller.treeViewModel }

    wsToolPane(pane) {
        tree(observed, ::contextMenuBuilder)
    }

    return fragment()
}

fun wsDeviceEditorToolDef(context: AioWsContext): WsPane<Unit, DeviceEditorToolController> {

    val controller = DeviceEditorToolController(context.workspace)

    val pane = WsPane(
        UUID(),
        Strings.devices,
        Graphics.account_tree,
        WsPanePosition.RightMiddle,
        AioWsContext.WSPANE_DEVICE_TOOL,
        actions = listOf(
            WsPaneAction(Graphics.unfold_more, Strings.expandAll, Unit) { controller.expandAll() },
            WsPaneAction(Graphics.unfold_less, Strings.collapseAll, Unit) { controller.collapseAll() },
            WsPaneMenuAction(Graphics.add, Strings.add, addTopMenu, { apply(controller, it.menuItem, null) })
        ),
        data = Unit,
        controller = controller
    )

    context.io {
        controller.start()
    }

    return pane
}

private fun apply(controller: DeviceEditorToolController, menuItem: MenuItem<AioDeviceEditOperation>, treeItem: TreeItem<AvValueId>?) {

    val (name, marker, virtual) = when (menuItem.data) {
        AioDeviceEditOperation.AddComputer -> Triple(Strings.computer, DeviceMarkers.COMPUTER, false)
        AioDeviceEditOperation.AddNetwork -> Triple(Strings.network, DeviceMarkers.NETWORK, false)
        AioDeviceEditOperation.AddController -> Triple(Strings.controller, DeviceMarkers.CONTROLLER, false)
        AioDeviceEditOperation.AddDevice -> Triple(Strings.device, DeviceMarkers.DEVICE, false)
        AioDeviceEditOperation.AddVirtualNetwork -> Triple(Strings.virtualNetwork, DeviceMarkers.NETWORK, true)
        AioDeviceEditOperation.AddVirtualController -> Triple(Strings.virtualController, DeviceMarkers.CONTROLLER, true)
        else -> Triple(null, null, null)
    }

    if (name != null && marker != null && virtual != null) {
        controller.addDevice(name, marker, treeItem?.data, virtual)
        return
    }

    check(treeItem != null)

    when (menuItem.data) {
        AioDeviceEditOperation.MoveUp -> controller.moveUp(treeItem.data)
        AioDeviceEditOperation.MoveDown -> controller.moveDown(treeItem.data)
        AioDeviceEditOperation.Inactivate -> Unit
        else -> Unit
    }
}

private val addComputer = MenuItem<AioDeviceEditOperation>(Graphics.host, Strings.addComputer, AioDeviceEditOperation.AddComputer)
private val addNetwork = MenuItem<AioDeviceEditOperation>(Graphics.account_tree, Strings.addNetwork, AioDeviceEditOperation.AddNetwork)
private val addController = MenuItem<AioDeviceEditOperation>(Graphics.memory, Strings.addController, AioDeviceEditOperation.AddController)
private val addDevice = MenuItem<AioDeviceEditOperation>(Graphics.empty, Strings.addDevice, AioDeviceEditOperation.AddDevice)
private val addVirtualNetwork = MenuItem<AioDeviceEditOperation>(Graphics.account_tree, Strings.addVirtualNetwork, AioDeviceEditOperation.AddVirtualNetwork)
private val addVirtualController = MenuItem<AioDeviceEditOperation>(Graphics.memory, Strings.addVirtualController, AioDeviceEditOperation.AddVirtualController)

private val addTopMenu = listOf(addComputer, addNetwork, addDevice, addVirtualNetwork)
private val computerMenu = listOf(addNetwork, addDevice)
private val networkMenu = listOf(addController, addDevice)
private val controllerMenu = listOf(addDevice)
private val deviceMenu = listOf(addDevice)
private val virtualNetworkMenu = listOf(addVirtualController)

private fun menu(viewModel: DeviceTreeModel, treeItem: TreeItem<AvValueId>): List<MenuItemBase<AioDeviceEditOperation>> {

    val controller = viewModel.context
    val itemId = treeItem.data

    val item = controller.valueTreeStore[itemId]?.asAvItem<AioDeviceSpec>()
    val markers = controller.valueTreeStore[itemId]?.markers?.keys ?: emptySet<DeviceMarkers>()
    val virtual = (item?.spec?.virtual == true)

    val base =
        when {
            DeviceMarkers.COMPUTER in markers -> computerMenu
            DeviceMarkers.NETWORK in markers -> if (virtual == true) virtualNetworkMenu else networkMenu
            DeviceMarkers.CONTROLLER in markers -> if (virtual == true) emptyList() else controllerMenu
            DeviceMarkers.DEVICE in markers -> deviceMenu
            else -> addTopMenu
        }

    val out = mutableListOf<MenuItemBase<AioDeviceEditOperation>>()
    out.addAll(base)

    out += MenuSeparator<AioDeviceEditOperation>()

    val subDevices = controller.valueTreeStore.getParentSubItems(itemId)

    out += MenuItem<AioDeviceEditOperation>(
        Graphics.arrow_drop_up, Strings.moveUp, AioDeviceEditOperation.MoveUp,
        inactive = (subDevices.isEmpty() || subDevices.first() == itemId)
    )

    out += MenuItem<AioDeviceEditOperation>(
        Graphics.arrow_drop_down, Strings.moveDown, AioDeviceEditOperation.MoveDown,
        inactive = (subDevices.isEmpty() || subDevices.last() == itemId)
    )

    out += MenuSeparator<AioDeviceEditOperation>()

    out += MenuItem<AioDeviceEditOperation>(null, Strings.inactivate, AioDeviceEditOperation.Inactivate)

    return out
}

@Adaptive
private fun contextMenuBuilder(
    hide: () -> Unit,
    viewModel: DeviceTreeModel,
    treeItem: TreeItem<AvValueId>
): AdaptiveFragment {
    column {
        zIndex { 200 }
        contextMenu(menu(viewModel, treeItem)) { menuItem, _ ->

            apply(
                viewModel.context as DeviceEditorToolController,
                menuItem,
                treeItem
            )

            hide()
        }
    }
    return fragment()
}