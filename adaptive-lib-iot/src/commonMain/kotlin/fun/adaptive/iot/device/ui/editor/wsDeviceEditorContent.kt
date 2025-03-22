package `fun`.adaptive.iot.device.ui.editor

import `fun`.adaptive.adaptive_lib_iot.generated.resources.*
import `fun`.adaptive.adat.api.update
import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.Independent
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.producer.fetch
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.iot.device.marker.AmvDevice
import `fun`.adaptive.iot.device.ui.localizedDeviceType
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.input.InputContext
import `fun`.adaptive.ui.input.text.textInput
import `fun`.adaptive.ui.input.text.textInputArea
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.label.uuidLabel
import `fun`.adaptive.ui.label.withLabel
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

fun wsDeviceEditorContentDef(context: AioWsContext) {
    val workspace = context.workspace

    workspace.addContentPaneBuilder(AioWsContext.WSIT_DEVICE) { item ->
        WsPane(
            UUID(),
            item.name,
            context[item].icon,
            WsPanePosition.Center,
            AioWsContext.WSPANE_DEVICE_CONTENT,
            controller = DeviceEditorContentController(workspace),
            data = item as AvItem
        )
    }
}

@Adaptive
fun wsDeviceContentPane(pane: WsPane<AvItem, DeviceEditorContentController>): AdaptiveFragment {

    @Independent
    val originalItem = copyOf { pane.data }

    @Independent
    val copyItem = copyOf { pane.data }

    val originalDevice = fetch { pane.controller.deviceService.getDeviceData(pane.data.uuid) } ?: AmvDevice(originalItem.uuid)
    val copyDevice = copyOf { originalDevice }

    column {
        maxSize .. verticalScroll .. padding { 16.dp } .. backgrounds.surface

        column {
            paddingBottom { 32.dp }
            h2(pane.data.name.ifEmpty { Strings.noname })
            uuidLabel { copyItem.uuid }
        }

        column {

            gap { 24.dp }

            withLabel(Strings.spxbId, InputContext(disabled = true)) { state ->
                width { 400.dp }
                textInput(copyItem.friendlyId, state) { }
            }

            withLabel(Strings.type, InputContext(disabled = true)) { state ->
                width { 400.dp }
                textInput(copyItem.localizedDeviceType, state) { }
            }

            withLabel(Strings.name) {
                width { 400.dp }
                textInput(copyItem.name) { v ->
                    println("update: ${copyItem.name} $v")
                    copyItem.update(copyItem::name, v)
                }
            }

            withLabel(Strings.note) {
                width { 400.dp }
                textInputArea(copyDevice.notes) { v ->
                    copyDevice.update(copyDevice::notes, v)
                } .. height { 300.dp }
            }

            button(Strings.save) .. onClick {
                if (copyItem.name != originalItem.name) {
                    pane.controller.rename(copyItem.uuid, copyItem.name)
                    originalItem.update(originalItem::name, copyItem.name)
                }
                if (copyDevice != originalDevice) {
                    pane.controller.setDeviceData(copyDevice)
                }
            }
        }
    }

    return fragment()
}