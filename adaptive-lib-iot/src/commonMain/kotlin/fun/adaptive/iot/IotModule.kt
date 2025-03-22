package `fun`.adaptive.iot

import `fun`.adaptive.adaptive_lib_iot.generated.resources.*
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.iot.device.marker.AmvDevice
import `fun`.adaptive.iot.device.marker.DeviceMarkers
import `fun`.adaptive.iot.device.ui.editor.wsDeviceEditorContentDef
import `fun`.adaptive.iot.device.ui.editor.wsDeviceEditorToolDef
import `fun`.adaptive.iot.marker.rht.ui.wsRhtBrowserContentDef
import `fun`.adaptive.iot.marker.rht.ui.wsRhtBrowserToolDef
import `fun`.adaptive.iot.space.marker.AmvSpace
import `fun`.adaptive.iot.space.marker.SpaceMarkers
import `fun`.adaptive.iot.space.ui.browser.SpaceBrowserWsItem
import `fun`.adaptive.iot.space.ui.editor.wsSpaceEditorContentDef
import `fun`.adaptive.iot.space.ui.editor.wsSpaceEditorToolDef
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsClassPaneController
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.builtin.AvString
import `fun`.adaptive.value.item.AmvItemIdList
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvStatus
import `fun`.adaptive.value.operation.*
import `fun`.adaptive.value.ui.iconCache
import `fun`.adaptive.wireformat.WireFormatRegistry

open class IotModule<WT>(
    val loadStrings: Boolean = true
) : AppModule<WT>() {

    override fun WireFormatRegistry.init() {
        this += AvoAdd
        this += AvoAddOrUpdate
        this += AvoMarkerRemove
        this += AvoUpdate
        this += AvoTransaction

        this += AvSubscribeCondition

        this += AvItem
        this += AvStatus
        this += AmvItemIdList

        this += AmvSpace
        this += AmvDevice

        this += AvString
    }
    
    override suspend fun loadResources() {
        if (loadStrings) {
            commonMainStringsStringStore0.load()
        }
    }

}