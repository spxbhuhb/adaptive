package `fun`.adaptive.iot.device.ui.editor

import `fun`.adaptive.iot.app.IotWsModule
import `fun`.adaptive.iot.generated.resources.account_tree
import `fun`.adaptive.iot.generated.resources.add
import `fun`.adaptive.iot.generated.resources.devices
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.builtin.*
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPaneAction
import `fun`.adaptive.ui.workspace.model.WsPaneMenuAction
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID


fun wsDeviceEditorToolDef(
    module: IotWsModule<*>
) {

    val workspace = module.workspace

    val controller = DeviceEditorToolController(workspace)

    workspace.addToolPane {
        WsPane(
            UUID(),
            workspace = workspace,
            Strings.devices,
            Graphics.account_tree,
            WsPanePosition.RightMiddle,
            module.WSPANE_DEVICE_TOOL,
            actions = listOf(
                WsPaneAction(Graphics.unfold_more, Strings.expandAll, Unit) { controller.expandAll() },
                WsPaneAction(Graphics.unfold_less, Strings.collapseAll, Unit) { controller.collapseAll() },
                WsPaneMenuAction(Graphics.add, Strings.add, addTopMenu, { apply(controller, it.menuItem, null) })
            ),
            data = Unit,
            controller = controller
        )
    }

    workspace.io {
        controller.start()
    }
}