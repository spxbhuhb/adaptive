package `fun`.adaptive.iot.space.ui.editor

import `fun`.adaptive.iot.app.IotWsModule
import `fun`.adaptive.iot.app.WsItemTypes
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.AvValue.Companion.asAvItem


fun wsSpaceEditorContentDef(
    module : IotWsModule<*>
) {
    val workspace = module.workspace

    workspace.addContentPaneBuilder(WsItemTypes.WSIT_SPACE) { item ->
        WsPane(
            UUID(),
            workspace = workspace,
            item.name,
            workspace.getItemIcon(item),
            WsPanePosition.Center,
            module.WSPANE_SPACE_CONTENT,
            controller = SpaceEditorContentController(workspace),
            data = item.asAvItem()
        )
    }

}