package `fun`.adaptive.iot.device.ui.editor

import `fun`.adaptive.adaptive_lib_iot.generated.resources.*
import `fun`.adaptive.adat.api.update
import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.iot.device.ui.localizedDeviceType
import `fun`.adaptive.iot.space.SpaceMarkers
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
import `fun`.adaptive.ui.select.select
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.workspace.Workspace.Companion.wsContext
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvItem.Companion.asAvItem
import `fun`.adaptive.value.ui.AvNameCache

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
            data = item.asAvItem()
        )
    }
}

@Adaptive
fun wsDeviceContentPane(pane: WsPane<AvItem<AioDeviceSpec>, DeviceEditorContentController>): AdaptiveFragment {

    val originalItem = copyOf { pane.data }
    val editItem = copyOf { pane.data }

    val originalSpec = pane.data.specific!!
    val editSpec = copyOf { pane.data.specific!! }

    val spaceNames = fragment().wsContext<AioWsContext>().spaceNameCache.names

    val originalSpace = spaceNames.firstOrNull { it.itemId == editItem.markers[SpaceMarkers.SPACE_REF] }
    var editSpace = spaceNames.firstOrNull { it.itemId == editItem.markers[SpaceMarkers.SPACE_REF] }

    column {
        maxSize .. verticalScroll .. padding { 16.dp } .. backgrounds.surface

        column {
            paddingBottom { 32.dp }
            h2(pane.data.name.ifEmpty { Strings.noname })
            uuidLabel { editItem.uuid }
        }

        column {

            gap { 24.dp }

            withLabel(Strings.spxbId, InputContext(disabled = true)) { state ->
                width { 400.dp }
                textInput(editItem.friendlyId, state) { }
            }

            withLabel(Strings.type, InputContext(disabled = true)) { state ->
                width { 400.dp }
                textInput(editItem.localizedDeviceType, state) { }
            }

            withLabel(Strings.name) {
                width { 400.dp }
                textInput(editItem.name) { v ->
                    println("update: ${editItem.name} $v")
                    editItem.update(editItem::name, v)
                }
            }

            withLabel(Strings.area) {
                width { 400.dp }

                select(editSpace, spaceNames) { v -> editSpace = v } ..
                    inputPlaceholder { "(no item selected)" } ..
                    toText<AvNameCache.NameCacheEntry> { v -> v.names.joinToString(" / ") }
            }

            withLabel(Strings.note) {
                width { 400.dp }
                textInputArea(editSpec.notes) { v ->
                    editSpec.update(editSpec::notes, v)
                } .. height { 300.dp }
            }

            button(Strings.save) .. onClick {
                if (editItem.name != originalItem.name) {
                    pane.controller.rename(editItem.uuid, editItem.name)
                    originalItem.update(originalItem::name, editItem.name)
                }
                if (editSpec != originalSpec) {
                    pane.controller.setSpec(pane.data.uuid, editSpec)
                }
                if (editSpace != originalSpace) {
                    pane.controller.setSpace(editItem.uuid, editSpace !!.itemId)
                }
            }

        }
    }

    return fragment()
}