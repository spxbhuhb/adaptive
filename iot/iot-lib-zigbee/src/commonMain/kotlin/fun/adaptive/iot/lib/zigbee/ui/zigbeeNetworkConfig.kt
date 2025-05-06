package `fun`.adaptive.iot.lib.zigbee.ui

import `fun`.adaptive.document.ui.direct.markdownHint
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.iot.device.DeviceMarkers
import `fun`.adaptive.iot.device.ui.editor.DeviceEditorToolController
import `fun`.adaptive.iot.lib.zigbee.model.ZigBeeNetworkSpec
import `fun`.adaptive.iot_lib_zigbee.generated.resources.aio_driver_zigbee
import `fun`.adaptive.iot_lib_zigbee.generated.resources.aio_driver_zigbee_description
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.editor.booleanEditor
import `fun`.adaptive.ui.editor.textEditor
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.value.item.AvItem

@Adaptive
fun zigbeeNetworkConfig(
    controller: DeviceEditorToolController
): AdaptiveFragment {

    val template = AvItem(
        Strings.aio_driver_zigbee,
        DeviceMarkers.DEVICE,
        friendlyId = Strings.aio_driver_zigbee,
        spec = ZigBeeNetworkSpec()
    )

    val form = adatFormBackend(template).also { controller.networkConfigForm = it }

    localContext(form) {
        column {
            gap { 16.dp }

            markdownHint(Strings.aio_driver_zigbee_description)

            textEditor { template.name }
            textEditor { template.friendlyId }
            booleanEditor { template.spec.enabled }
        }
    }

    return fragment()
}