package `fun`.adaptive.iot

import `fun`.adaptive.adaptive_lib_iot.generated.resources.apartment
import `fun`.adaptive.adaptive_lib_iot.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.adaptive_lib_iot.generated.resources.dew_point
import `fun`.adaptive.iot.item.AioItem
import `fun`.adaptive.iot.item.AioStatus
import `fun`.adaptive.iot.item.AmvItemIdList
import `fun`.adaptive.iot.space.markers.AmvSpace
import `fun`.adaptive.iot.space.ui.SpacePaneController
import `fun`.adaptive.iot.space.ui.model.SpaceBrowserWsItem
import `fun`.adaptive.iot.space.ui.wsSpaceEditorToolDef
import `fun`.adaptive.iot.value.AioSubscribeCondition
import `fun`.adaptive.iot.value.builtin.AvString
import `fun`.adaptive.iot.value.operation.*
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsClassPaneController
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.wireformat.WireFormatRegistry

suspend fun iotCommon(loadStrings : Boolean = true) {
    val r = WireFormatRegistry

    r += AvoAdd
    r += AvoAddOrUpdate
    r += AvoMarkerRemove
    r += AvoUpdate
    r += AvoTransaction

    r += AioSubscribeCondition

    r += AioItem
    r += AioStatus
    r += AmvItemIdList

    r += AmvSpace

    r += AvString

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

    toolPanes += wsSpaceEditorToolDef(context)
//    toolPanes += wsInfrastructureEditorDef()
//
//    toolPanes += wsSpaceBrowserTool(
//        SpaceToolConfig(
//            Strings.temperatureAndHumidity,
//            Graphics.dew_point,
//            "", "", ""
//        )
//    )

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
