package `fun`.adaptive.iot

import `fun`.adaptive.adaptive_lib_iot.generated.resources.*
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.iot.marker.rht.ui.wsRhtBrowserContentDef
import `fun`.adaptive.iot.marker.rht.ui.wsRhtBrowserToolDef
import `fun`.adaptive.iot.space.markers.AmvSpace
import `fun`.adaptive.iot.space.markers.SpaceMarkers
import `fun`.adaptive.iot.space.ui.browser.SpaceBrowserWsItem
import `fun`.adaptive.iot.space.ui.editor.wsSpaceEditorContentDef
import `fun`.adaptive.iot.space.ui.editor.wsSpaceEditorToolDef
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.iot.ws.DeviceMarkers
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

class WsIotModule(
    val loadStrings: Boolean = true
) : AppModule<Workspace>() {

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

        this += AvString
    }
    
    override suspend fun loadResources() {
        if (loadStrings) {
            commonMainStringsStringStore0.load()
        }
    }

    override fun AdaptiveAdapter.init() {
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

    override fun Workspace.init() {

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
}