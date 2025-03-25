package `fun`.adaptive.iot

import `fun`.adaptive.adaptive_lib_iot.generated.resources.*
import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.iot.device.DeviceMarkers
import `fun`.adaptive.iot.history.model.AioBooleanHistoryRecord
import `fun`.adaptive.iot.history.model.AioDoubleHistoryRecord
import `fun`.adaptive.iot.history.model.AioHistoryMetadata
import `fun`.adaptive.iot.history.model.AioHistoryQuery
import `fun`.adaptive.iot.history.model.AioStringHistoryRecord
import `fun`.adaptive.iot.domain.rht.ui.wsRhtBrowserContentDef
import `fun`.adaptive.iot.domain.rht.ui.wsRhtBrowserToolDef
import `fun`.adaptive.iot.point.PointMarkers
import `fun`.adaptive.iot.point.computed.AioComputedPointSpec
import `fun`.adaptive.iot.point.sim.AioSimPointSpec
import `fun`.adaptive.iot.space.AioSpaceSpec
import `fun`.adaptive.iot.space.SpaceMarkers
import `fun`.adaptive.iot.space.ui.browser.SpaceBrowserWsItem
import `fun`.adaptive.iot.space.ui.editor.wsSpaceEditorContentDef
import `fun`.adaptive.iot.space.ui.editor.wsSpaceEditorToolDef
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsClassPaneController
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.builtin.AvString
import `fun`.adaptive.value.item.AvItemIdList
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvStatus
import `fun`.adaptive.value.operation.*
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
    r += AvItemIdList

    r += AioSpaceSpec
    r += AioDeviceSpec
    r += AioComputedPointSpec
    r += AioSimPointSpec

    r += AvString

    r += AioDoubleHistoryRecord
    r += AioBooleanHistoryRecord
    r += AioStringHistoryRecord
    r += AioHistoryMetadata
    r += AioHistoryQuery

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
