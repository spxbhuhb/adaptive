package `fun`.adaptive.iot.device.ui.browser

import `fun`.adaptive.adaptive_lib_iot.generated.resources.temperatureAndHumidity
import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.builtin.arrow_right
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.workspace.model.WsPane

@Adaptive
fun wsDeviceBrowserContent(pane: WsPane<DeviceBrowserWsItem, *>): AdaptiveFragment {

    val subDevices = pane.subDevices()

    column {
        maxSize .. verticalScroll .. padding { 16.dp } .. backgrounds.surface

        pageHeader(pane)
        listHeader(pane)

        for (device in subDevices) {
            listItem(pane, device)
        }
    }

    return fragment()
}

@Adaptive
fun pageHeader(pane: WsPane<DeviceBrowserWsItem, *>) {
    column {
        paddingBottom { 32.dp }
        h2(Strings.temperatureAndHumidity)
        devicePath(pane.data)
    }
}

@Adaptive
fun listHeader(pane: WsPane<DeviceBrowserWsItem, *>) {

}

@Adaptive
fun listItem(pane: WsPane<DeviceBrowserWsItem, *>, deviceId: AvValueId) {
    val device = pane.getDevice(deviceId) !!

    row {
        height { 32.dp } .. maxWidth
        actualize(pane.data.config.itemKey !!, emptyInstructions, device)
    }
}

@Adaptive
fun devicePath(item: DeviceBrowserWsItem) {
    val names = item.devicePathNames()

    row {
        alignItems.center

        for (name in names.indices) {
            text(names[name]) .. textColors.onSurfaceVariant
            if (name < names.size - 1) {
                icon(Graphics.arrow_right) .. textColors.onSurfaceVariant
            }
        }
    }

}
