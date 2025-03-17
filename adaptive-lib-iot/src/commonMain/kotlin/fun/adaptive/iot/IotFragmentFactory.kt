package `fun`.adaptive.iot

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.iot.ui.measurement.wsMeasurementContentPane
import `fun`.adaptive.iot.ui.measurement.wsMeasurementToolPane
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.iot.ui.space.wsSpaceContentPane
import `fun`.adaptive.iot.ui.space.wsSpaceToolPane

object IotFragmentFactory : FoundationFragmentFactory() {
    init {
        add(AioWsContext.WSPANE_SPACE_TOOL, ::wsSpaceToolPane)
        add(AioWsContext.WSPANE_SPACE_CONTENT, ::wsSpaceContentPane)
        add(AioWsContext.WSPANE_MEASUREMENT_LOCATION_TOOL, ::wsMeasurementToolPane)
        add(AioWsContext.WSPANE_MEASUREMENT_LOCATION_CONTENT, ::wsMeasurementContentPane)
    }
}