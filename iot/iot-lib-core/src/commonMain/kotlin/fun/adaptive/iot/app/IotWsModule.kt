package `fun`.adaptive.iot.app

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.iot.alarm.ui.WSPANE_ALARM_TOOL
import `fun`.adaptive.iot.alarm.ui.wsAlarmTool
import `fun`.adaptive.iot.alarm.ui.wsAlarmToolDef
import `fun`.adaptive.iot.device.DeviceMarkers
import `fun`.adaptive.iot.device.ui.DeviceItems
import `fun`.adaptive.iot.device.ui.editor.wsDeviceContentPane
import `fun`.adaptive.iot.device.ui.editor.wsDeviceEditorContentDef
import `fun`.adaptive.iot.device.ui.editor.wsDeviceEditorTool
import `fun`.adaptive.iot.device.ui.editor.wsDeviceEditorToolDef
import `fun`.adaptive.iot.domain.rht.ui.def.rhtBrowserContentDef
import `fun`.adaptive.iot.domain.rht.ui.def.rhtBrowserToolDef
import `fun`.adaptive.iot.generated.resources.*
import `fun`.adaptive.iot.history.ui.wsHistoryContent
import `fun`.adaptive.iot.history.ui.wsHistoryContentDef
import `fun`.adaptive.iot.history.ui.wsHistoryTool
import `fun`.adaptive.iot.history.ui.wsHistoryToolDef
import `fun`.adaptive.iot.space.SpaceMarkers
import `fun`.adaptive.iot.domain.rht.ui.fragment.rhtBrowserContent
import `fun`.adaptive.iot.domain.rht.ui.fragment.rhtBrowserTool
import `fun`.adaptive.iot.space.ui.editor.wsSpaceContentPane
import `fun`.adaptive.iot.space.ui.editor.wsSpaceEditorContentDef
import `fun`.adaptive.iot.space.ui.editor.wsSpaceEditorTool
import `fun`.adaptive.iot.space.ui.editor.wsSpaceEditorToolDef
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.value.iconCache
import `fun`.adaptive.ui.workspace.Workspace

class IotWsModule<WT : Workspace> : IotModule<WT>() {

    val WSPANE_SPACE_TOOL = "aio:space:tool"
    val WSPANE_SPACE_CONTENT = "aio:space:content"

    val WSPANE_DEVICE_TOOL = "aio:device:tool"
    val WSPANE_DEVICE_CONTENT = "aio:device:content"

    val WSPANE_RHT_BROWSER_TOOL = "aio:rht:browser:tool"
    val WSPANE_RHT_BROWSER_CONTENT = "aio:rht:browser:content"

    val WSPANE_HISTORY_TOOL: FragmentKey = "aio:history:tool"
    val WSPANE_HISTORY_CONTENT = "aio:history:content"

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {

        add(WSPANE_SPACE_TOOL, ::wsSpaceEditorTool)
        add(WSPANE_SPACE_CONTENT, ::wsSpaceContentPane)

        add(WSPANE_DEVICE_TOOL, ::wsDeviceEditorTool)
        add(WSPANE_DEVICE_CONTENT, ::wsDeviceContentPane)

        //add(WSPANE_ALARM_TOOL, ::wsAlarmTool)

        add(WSPANE_HISTORY_TOOL, ::wsHistoryTool)
        add(WSPANE_HISTORY_CONTENT, ::wsHistoryContent)

        add(WSPANE_RHT_BROWSER_TOOL, ::rhtBrowserTool)
        add(WSPANE_RHT_BROWSER_CONTENT, ::rhtBrowserContent)

        iconCache[SpaceMarkers.SITE] = Graphics.responsive_layout
        iconCache[SpaceMarkers.BUILDING] = Graphics.apartment
        iconCache[SpaceMarkers.FLOOR] = Graphics.stacks
        iconCache[SpaceMarkers.ROOM] = Graphics.meeting_room
        iconCache[SpaceMarkers.AREA] = Graphics.crop_5_4

        iconCache[DeviceMarkers.COMPUTER] = Graphics.host
        iconCache[DeviceMarkers.NETWORK] = Graphics.account_tree
        iconCache[DeviceMarkers.CONTROLLER] = Graphics.memory
    }

    override fun contextInit() {
        workspace.contexts += this
    }

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {

        rhtBrowserToolDef(this@IotWsModule)
        rhtBrowserContentDef(this@IotWsModule)

        wsHistoryToolDef(this@IotWsModule)
        wsHistoryContentDef(this@IotWsModule)

        wsSpaceEditorToolDef(this@IotWsModule)
        wsSpaceEditorContentDef(this@IotWsModule)

        wsDeviceEditorToolDef(this@IotWsModule)
        wsDeviceEditorContentDef(this@IotWsModule)

        wsAlarmToolDef()

        addItemConfig(DeviceItems.WSIT_SPACE, Graphics.apartment)

    }
}