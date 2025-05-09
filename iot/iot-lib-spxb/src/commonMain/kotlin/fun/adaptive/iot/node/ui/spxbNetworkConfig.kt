package `fun`.adaptive.iot.node.ui

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.iot.device.DeviceMarkers
import `fun`.adaptive.iot.device.ui.editor.DeviceEditorToolController
import `fun`.adaptive.iot.node.spec.SpxbNetworkSpec
import `fun`.adaptive.iot_lib_spxb.generated.resources.aio_driver_spxb
import `fun`.adaptive.iot_lib_spxb.generated.resources.aio_driver_spxb_description
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.editor.booleanEditor
import `fun`.adaptive.ui.editor.textEditor
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.theme.textSmall
import `fun`.adaptive.value.AvValue

@Adaptive
fun spxbNetworkConfig(
    controller: DeviceEditorToolController
): AdaptiveFragment {

    val template = AvValue(
        Strings.aio_driver_spxb,
        DeviceMarkers.DEVICE,
        friendlyId = Strings.aio_driver_spxb,
        spec = SpxbNetworkSpec()
    )

    val form = adatFormBackend(template).also { controller.networkConfigForm = it }

    localContext(form) {
        column {
            gap { 16.dp }

            text(Strings.aio_driver_spxb_description) .. textColors.onSurfaceVariant .. textSmall

            textEditor { template.name }
            textEditor { template.friendlyId }
            booleanEditor { template.spec.enabled }

        }
    }

    return fragment()
}