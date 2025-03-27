package `fun`.adaptive.iot

import `fun`.adaptive.iot.device.DeviceMarkers
import `fun`.adaptive.iot.device.virtual.AioVirtualDeviceSpec
import `fun`.adaptive.iot.domain.rht.ui.wsRhtBrowserContentDef
import `fun`.adaptive.iot.domain.rht.ui.wsRhtBrowserToolDef
import `fun`.adaptive.iot.generated.resources.*
import `fun`.adaptive.iot.history.model.*
import `fun`.adaptive.iot.point.PointMarkers
import `fun`.adaptive.iot.point.computed.AioComputedPointSpec
import `fun`.adaptive.iot.point.conversion.number.DoubleMultiplyConversion
import `fun`.adaptive.iot.point.sim.AioSimPointSpec
import `fun`.adaptive.iot.space.AioSpaceSpec
import `fun`.adaptive.iot.space.SpaceMarkers
import `fun`.adaptive.iot.space.ui.editor.wsSpaceEditorContentDef
import `fun`.adaptive.iot.space.ui.editor.wsSpaceEditorToolDef
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.value.builtin.AvDouble
import `fun`.adaptive.value.ui.iconCache
import `fun`.adaptive.wireformat.WireFormatRegistry

suspend fun iotCommon(loadStrings: Boolean = true) {
    val r = WireFormatRegistry

    r += AioSpaceSpec
    r += AioVirtualDeviceSpec
    r += AioComputedPointSpec
    r += AioSimPointSpec

    r += AioDoubleHistoryRecord
    r += AioBooleanHistoryRecord
    r += AioStringHistoryRecord
    r += AioHistoryMetadata
    r += AioHistoryQuery

    r += AvDouble
    r += DoubleMultiplyConversion

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

    iconCache[DeviceMarkers.COMPUTER] = Graphics.host
    iconCache[DeviceMarkers.NETWORK] = Graphics.account_tree
    iconCache[DeviceMarkers.DEVICE] = Graphics.memory

    iconCache[PointMarkers.POINT] = Graphics.database
}

fun Workspace.iotCommon() {
    val context = AioWsContext(this)

    contexts += context

    toolPanes += wsSpaceEditorToolDef(context)
    wsSpaceEditorContentDef(context)

    toolPanes += wsRhtBrowserToolDef(context)
    wsRhtBrowserContentDef(context)

    addItemConfig(AioWsContext.WSIT_SPACE, Graphics.apartment)
}
