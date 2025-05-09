package `fun`.adaptive.iot.sim.ui

import `fun`.adaptive.document.ui.direct.markdownHint
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.iot.device.DeviceMarkers
import `fun`.adaptive.iot.device.ui.editor.DeviceEditorToolController
import `fun`.adaptive.iot.sim.spec.SimNetworkSpec
import `fun`.adaptive.iot_lib_sim.generated.resources.aio_driver_sim
import `fun`.adaptive.iot_lib_sim.generated.resources.aio_sim_driver_description
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.editor.booleanEditor
import `fun`.adaptive.ui.editor.textEditor
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.value.AvValue

@Adaptive
fun simNetworkConfig(
    controller: DeviceEditorToolController
): AdaptiveFragment {

    val template = AvValue(
        Strings.aio_driver_sim,
        DeviceMarkers.DEVICE,
        friendlyId = Strings.aio_driver_sim,
        spec = SimNetworkSpec()
    )

    val form = adatFormBackend(template).also { controller.networkConfigForm = it }

    localContext(form) {
        column {
            gap { 16.dp }

            markdownHint(Strings.aio_sim_driver_description)

            textEditor { template.name }
            textEditor { template.friendlyId }
            booleanEditor { template.spec.enabled }
        }
    }

    return fragment()
}