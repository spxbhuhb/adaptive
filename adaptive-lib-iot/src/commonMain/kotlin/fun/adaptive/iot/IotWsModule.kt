package `fun`.adaptive.iot

import `fun`.adaptive.adaptive_lib_iot.generated.resources.*
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.iot.alarm.ui.wsAlarmToolDef
import `fun`.adaptive.iot.device.marker.DeviceMarkers
import `fun`.adaptive.iot.device.ui.editor.wsDeviceEditorContentDef
import `fun`.adaptive.iot.device.ui.editor.wsDeviceEditorToolDef
import `fun`.adaptive.iot.history.ui.wsHistoryToolDef
import `fun`.adaptive.iot.marker.rht.ui.wsRhtBrowserContentDef
import `fun`.adaptive.iot.marker.rht.ui.wsRhtBrowserToolDef
import `fun`.adaptive.iot.space.marker.SpaceMarkers
import `fun`.adaptive.iot.space.ui.browser.SpaceBrowserWsItem
import `fun`.adaptive.iot.space.ui.editor.wsSpaceEditorContentDef
import `fun`.adaptive.iot.space.ui.editor.wsSpaceEditorToolDef
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsClassPaneController
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.ui.iconCache

class IotWsModule : IotModule<Workspace>(true) {

    override fun AdaptiveAdapter.init() {
        fragmentFactory += IotFragmentFactory

        iconCache[SpaceMarkers.SITE] = Graphics.responsive_layout
        iconCache[SpaceMarkers.BUILDING] = Graphics.apartment
        iconCache[SpaceMarkers.FLOOR] = Graphics.stacks
        iconCache[SpaceMarkers.ROOM] = Graphics.meeting_room
        iconCache[SpaceMarkers.AREA] = Graphics.crop_5_4

        iconCache[DeviceMarkers.COMPUTER] = Graphics.host
        iconCache[DeviceMarkers.NETWORK] = Graphics.account_tree
        iconCache[DeviceMarkers.CONTROLLER] = Graphics.memory
        iconCache[DeviceMarkers.POINT] = Graphics.database
    }

    override fun Workspace.init() {

        val context = AioWsContext(this)

        contexts += context

        + wsRhtBrowserToolDef(context)
        wsRhtBrowserContentDef(context)

        wsHistoryToolDef()
        wsAlarmToolDef()

        + wsSpaceEditorToolDef(context)
        wsSpaceEditorContentDef(context)

        + wsDeviceEditorToolDef(context)
        wsDeviceEditorContentDef(context)

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
}