package `fun`.adaptive.iot

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.iot.device.ui.editor.wsDeviceContentPane
import `fun`.adaptive.iot.device.ui.editor.wsDeviceEditorTool
import `fun`.adaptive.iot.marker.rht.AmvRelativeHumidityAndTemperature
import `fun`.adaptive.iot.marker.rht.ui.rhtListItem
import `fun`.adaptive.iot.space.ui.browser.wsSpaceBrowserContent
import `fun`.adaptive.iot.space.ui.browser.wsSpaceBrowserTool
import `fun`.adaptive.iot.space.ui.editor.wsSpaceContentPane
import `fun`.adaptive.iot.space.ui.editor.wsSpaceEditorTool
import `fun`.adaptive.iot.ws.AioWsContext

object IotFragmentFactory : FoundationFragmentFactory() {
    init {
        add(AioWsContext.WSPANE_SPACE_TOOL, ::wsSpaceEditorTool)
        add(AioWsContext.WSPANE_SPACE_CONTENT, ::wsSpaceContentPane)

        add(AioWsContext.WSPANE_DEVICE_TOOL, ::wsDeviceEditorTool)
        add(AioWsContext.WSPANE_DEVICE_CONTENT, ::wsDeviceContentPane)

        add(AioWsContext.WSPANE_RHT_BROWSER_TOOL, ::wsSpaceBrowserTool)
        add(AioWsContext.WSPANE_RHT_BROWSER_CONTENT, ::wsSpaceBrowserContent)

        add(AmvRelativeHumidityAndTemperature.RHT_LIST_ITEM, ::rhtListItem)
    }
}