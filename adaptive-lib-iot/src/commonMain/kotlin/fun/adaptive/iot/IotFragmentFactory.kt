package `fun`.adaptive.iot

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.iot.space.ui.wsSpaceBrowserContent
import `fun`.adaptive.iot.space.ui.wsMeasurementToolPane
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.iot.space.ui.wsSpaceContentPane
import `fun`.adaptive.iot.space.ui.wsSpaceTreeEditorPane

object IotFragmentFactory : FoundationFragmentFactory() {
    init {
        add(AioWsContext.WSPANE_SPACE_TOOL, ::wsSpaceTreeEditorPane)
        add(AioWsContext.WSPANE_SPACE_CONTENT, ::wsSpaceContentPane)
        add(AioWsContext.WSPANE_MEASUREMENT_LOCATION_TOOL, ::wsMeasurementToolPane)
        add(AioWsContext.WSPANE_MEASUREMENT_LOCATION_CONTENT, ::wsSpaceBrowserContent)
    }
}