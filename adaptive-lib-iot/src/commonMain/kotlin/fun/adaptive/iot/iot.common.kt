package `fun`.adaptive.iot

import `fun`.adaptive.adaptive_lib_iot.generated.resources.account_tree
import `fun`.adaptive.adaptive_lib_iot.generated.resources.apartment
import `fun`.adaptive.adaptive_lib_iot.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.adaptive_lib_iot.generated.resources.crop_5_4
import `fun`.adaptive.adaptive_lib_iot.generated.resources.database
import `fun`.adaptive.adaptive_lib_iot.generated.resources.dew_point
import `fun`.adaptive.adaptive_lib_iot.generated.resources.host
import `fun`.adaptive.adaptive_lib_iot.generated.resources.meeting_room
import `fun`.adaptive.adaptive_lib_iot.generated.resources.memory
import `fun`.adaptive.adaptive_lib_iot.generated.resources.responsive_layout
import `fun`.adaptive.adaptive_lib_iot.generated.resources.stacks
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvStatus
import `fun`.adaptive.value.item.AmvItemIdList
import `fun`.adaptive.iot.marker.rht.ui.wsRhtBrowserContentDef
import `fun`.adaptive.iot.marker.rht.ui.wsRhtBrowserToolDef
import `fun`.adaptive.iot.space.markers.AmvSpace
import `fun`.adaptive.iot.space.markers.SpaceMarkers
import `fun`.adaptive.iot.space.ui.browser.SpaceBrowserWsItem
import `fun`.adaptive.iot.space.ui.editor.wsSpaceEditorContentDef
import `fun`.adaptive.iot.space.ui.editor.wsSpaceEditorToolDef
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.builtin.AvString
import `fun`.adaptive.value.operation.*
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.iot.ws.DeviceMarkers
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsClassPaneController
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.ui.iconCache
import `fun`.adaptive.wireformat.WireFormatRegistry

suspend fun iotCommon(loadStrings: Boolean = true) {
    val r = WireFormatRegistry

    r += AvoAdd
    r += AvoAddOrUpdate
    r += AvoMarkerRemove
    r += AvoUpdate
    r += AvoTransaction

    r += AvSubscribeCondition

    r += AvItem
    r += AvStatus
    r += AmvItemIdList

    r += AmvSpace

    r += AvString

    if (loadStrings) {
        commonMainStringsStringStore0.load()
    }
}

fun AbstractAuiAdapter<*, *>.iotCommon() {
    fragmentFactory += IotFragmentFactory

    iconCache[SpaceMarkers.SITE] = Graphics.responsive_layout
    iconCache[SpaceMarkers.BUILDING] = Graphics.apartment
    iconCache[SpaceMarkers.FLOOR] = Graphics.stacks
    iconCache[SpaceMarkers.ROOM] = Graphics.meeting_room
    iconCache[SpaceMarkers.AREA] = Graphics.crop_5_4

    iconCache[DeviceMarkers.HOST] = Graphics.host
    iconCache[DeviceMarkers.NETWORK] = Graphics.account_tree
    iconCache[DeviceMarkers.DEVICE] = Graphics.memory
    iconCache[DeviceMarkers.POINT] = Graphics.database
}

fun Workspace.iotCommon() {
    val context = AioWsContext(this)

    contexts += context

    toolPanes += wsSpaceEditorToolDef(context)
    wsSpaceEditorContentDef(context)

    toolPanes += wsRhtBrowserToolDef(context)
    wsRhtBrowserContentDef(context)

    addContentPaneBuilder(AioWsContext.WSIT_MEASUREMENT_LOCATION) { item ->
        WsPane(
            UUID(),
            item.name,
            context[item].icon,
            WsPanePosition.Center,
            AioWsContext.WSPANE_MEASUREMENT_LOCATION_CONTENT,
            controller = WsClassPaneController(SpaceBrowserWsItem::class),
            data = item as SpaceBrowserWsItem
        )
    }

    addItemConfig(AioWsContext.WSIT_SPACE, Graphics.apartment)
    addItemConfig(AioWsContext.WSIT_MEASUREMENT_LOCATION, Graphics.dew_point)

}
