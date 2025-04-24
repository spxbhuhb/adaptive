package `fun`.adaptive.iot.sim.ui

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.iot.device.DeviceMarkers
import `fun`.adaptive.iot.sim.spec.SimNetworkSpec
import `fun`.adaptive.iot_lib_sim.generated.resources.aio_driver_sim
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.editor.booleanEditor
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.value.item.AvItem

@Adaptive
fun simNetworkConfig() : AdaptiveFragment {
    val template = AvItem(
        Strings.aio_driver_sim,
        DeviceMarkers.DEVICE,
        friendlyId = Strings.aio_driver_sim,
        spec = SimNetworkSpec()
    )

    val form = adatFormBackend(template)

    localContext(form) {
        booleanEditor { template.spec.enabled }
    }

    return fragment()
}