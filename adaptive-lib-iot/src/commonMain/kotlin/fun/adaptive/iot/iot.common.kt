package `fun`.adaptive.iot

import `fun`.adaptive.adaptive_lib_iot.generated.resources.apartment
import `fun`.adaptive.adaptive_lib_iot.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.adaptive_lib_iot.generated.resources.dew_point
import `fun`.adaptive.adaptive_lib_iot.generated.resources.temperatureAndHumidity
import `fun`.adaptive.iot.model.device.AioDevice
import `fun`.adaptive.iot.model.device.point.AioDevicePoint
import `fun`.adaptive.iot.model.project.AioProject
import `fun`.adaptive.iot.model.space.AioSpace
import `fun`.adaptive.iot.model.space.AioSpaceType
import `fun`.adaptive.iot.model.space.SpaceBrowserConfig
import `fun`.adaptive.iot.model.space.SpaceBrowserWsItem
import `fun`.adaptive.iot.ui.space.wsSpaceBrowserTool
import `fun`.adaptive.iot.ui.space.SpacePaneController
import `fun`.adaptive.iot.ui.space.wsSpaceTreeEditorDef
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsClassPaneController
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.wireformat.WireFormatRegistry

suspend fun iotCommon(loadStrings : Boolean = true) {
    val r = WireFormatRegistry

    r += AioProject
    r += AioSpace
    r += AioSpaceType
    r += AioDevice
    r += AioDevicePoint

    if (loadStrings) {
        commonMainStringsStringStore0.load()
    }
}

fun AbstractAuiAdapter<*, *>.iotCommon() {
    fragmentFactory += IotFragmentFactory
}

fun Workspace.iotCommon() {
    val context = AioWsContext(this)

    contexts += context

    toolPanes += wsSpaceTreeEditorDef()
    toolPanes += wsSpaceBrowserTool(
        SpaceBrowserConfig(
            Strings.temperatureAndHumidity,
            Graphics.dew_point,
            "", "", ""
        )
    )

    addContentPaneBuilder(AioWsContext.WSIT_SPACE) { item ->
        WsPane(
            UUID(),
            item.name,
            context[item].icon,
            WsPanePosition.Center,
            AioWsContext.WSPANE_SPACE_CONTENT,
            controller = SpacePaneController(context),
            model = item
        )
    }

    addContentPaneBuilder(AioWsContext.WSIT_MEASUREMENT_LOCATION) { item ->
        WsPane(
            UUID(),
            item.name,
            context[item].icon,
            WsPanePosition.Center,
            AioWsContext.WSPANE_MEASUREMENT_LOCATION_CONTENT,
            controller = WsClassPaneController(SpaceBrowserWsItem::class),
            model = item as SpaceBrowserWsItem
        )
    }

    addItemConfig(AioWsContext.WSIT_SPACE, Graphics.apartment)
    addItemConfig(AioWsContext.WSIT_MEASUREMENT_LOCATION, Graphics.dew_point)

}
