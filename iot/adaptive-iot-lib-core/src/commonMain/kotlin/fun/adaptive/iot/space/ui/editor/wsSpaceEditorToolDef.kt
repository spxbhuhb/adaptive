package `fun`.adaptive.iot.space.ui.editor

import `fun`.adaptive.iot.app.IotWsModule
import `fun`.adaptive.iot.generated.resources.add
import `fun`.adaptive.iot.generated.resources.apartment
import `fun`.adaptive.iot.generated.resources.areas
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.builtin.add
import `fun`.adaptive.ui.builtin.collapseAll
import `fun`.adaptive.ui.builtin.expandAll
import `fun`.adaptive.ui.builtin.unfold_less
import `fun`.adaptive.ui.builtin.unfold_more
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPaneAction
import `fun`.adaptive.ui.workspace.model.WsPaneMenuAction
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

fun wsSpaceEditorToolDef(
    module: IotWsModule<*>
) {
    val workspace = module.workspace

    val controller = SpaceEditorToolController(workspace)

    workspace.addToolPane {

        WsPane(
            UUID(),
            workspace = workspace,
            Strings.areas,
            Graphics.apartment,
            WsPanePosition.RightTop,
            module.WSPANE_SPACE_TOOL,
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