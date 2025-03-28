package `fun`.adaptive.iot

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.iot.alarm.ui.wsAlarmToolDef
import `fun`.adaptive.iot.device.DeviceMarkers
import `fun`.adaptive.iot.device.ui.editor.wsDeviceEditorContentDef
import `fun`.adaptive.iot.device.ui.editor.wsDeviceEditorToolDef
import `fun`.adaptive.iot.domain.rht.ui.wsRhtBrowserContentDef
import `fun`.adaptive.iot.domain.rht.ui.wsRhtBrowserToolDef
import `fun`.adaptive.iot.generated.resources.*
import `fun`.adaptive.iot.history.ui.wsHistoryContentDef
import `fun`.adaptive.iot.history.ui.wsHistoryToolDef
import `fun`.adaptive.iot.space.SpaceMarkers
import `fun`.adaptive.iot.space.ui.editor.wsSpaceEditorContentDef
import `fun`.adaptive.iot.space.ui.editor.wsSpaceEditorToolDef
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.value.ui.iconCache

class IotWsModule<AT : Any> : IotModule<Workspace, AT>(true) {

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
    }

    override fun Workspace.init() {

        val context = AioWsContext(this)

        contexts += context

        + wsRhtBrowserToolDef(context)
        wsRhtBrowserContentDef(context)

        wsHistoryToolDef()
        wsHistoryContentDef(context)

        + wsSpaceEditorToolDef(context)
        wsSpaceEditorContentDef(context)

        + wsDeviceEditorToolDef(context)
        wsDeviceEditorContentDef(context)

        wsAlarmToolDef()

        addItemConfig(AioWsContext.WSIT_SPACE, Graphics.apartment)

    }
}