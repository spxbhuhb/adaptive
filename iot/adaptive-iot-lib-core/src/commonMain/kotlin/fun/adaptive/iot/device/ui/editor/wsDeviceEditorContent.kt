package `fun`.adaptive.iot.device.ui.editor

import `fun`.adaptive.adat.api.update
import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.common.AioTheme
import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.iot.device.ui.localizedDeviceType
import `fun`.adaptive.iot.generated.resources.*
import `fun`.adaptive.iot.point.PointMarkers
import `fun`.adaptive.iot.point.ui.pointSummary
import `fun`.adaptive.iot.space.SpaceMarkers
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.input.InputContext
import `fun`.adaptive.ui.input.text.textInput
import `fun`.adaptive.ui.input.text.textInputArea
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.label.uuidLabel
import `fun`.adaptive.ui.label.withLabel
import `fun`.adaptive.ui.select.select
import `fun`.adaptive.ui.snackbar.warningNotification
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.workspace.Workspace.Companion.wsContext
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvItem.Companion.asAvItem
import `fun`.adaptive.ui.value.AvNameCacheEntry
import `fun`.adaptive.ui.value.AvUiList

fun wsDeviceEditorContentDef(context: AioWsContext) {
    val workspace = context.workspace

    workspace.addContentPaneBuilder(AioWsContext.WSIT_DEVICE) { item ->
        WsPane(
            UUID(),
            workspace = workspace,
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
    val editSpec = copyOf { pane.data.spec }

    val spaceNames = fragment().wsContext<AioWsContext>().spaceNameCache.value

    val originalSpace = spaceNames.firstOrNull { it.item.uuid == editItem.markers[SpaceMarkers.SPACE_REF] }
    var editSpace = spaceNames.firstOrNull { it.item.uuid == editItem.markers[SpaceMarkers.SPACE_REF] }

    grid {
        maxSize .. padding { 16.dp } .. backgrounds.surface
        colTemplate(400.dp, 1.fr)
        rowTemplate(56.dp, 312.dp, 1.fr)
        gap { 16.dp }

        title(originalItem)
        actions(pane.controller, originalItem, editItem, editSpec, originalSpace, editSpace)

        editFields(editItem, editSpace, spaceNames) { editSpace = it }
        editNotes(editSpec)

        points(originalItem.uuid) .. colSpan(2)
    }

    return fragment()
}

@Adaptive
fun title(
    item: AvItem<AioDeviceSpec>,
) {
    column {
        paddingBottom { 32.dp }
        h2(item.name.ifEmpty { Strings.noname })
        uuidLabel { item.uuid }
    }
}

@Adaptive
fun actions(
    controller: DeviceEditorContentController,
    originalItem: AvItem<AioDeviceSpec>,
    editItem: AvItem<AioDeviceSpec>,
    editSpec: AioDeviceSpec,
    originalSpace : AvNameCacheEntry?,
    editSpace : AvNameCacheEntry?
) {
    button(Strings.save) ..
        alignSelf.end ..
        onClick {
            if (editItem.name == originalItem.name && editSpec == originalItem.spec && editSpace == originalSpace) {
                warningNotification(Strings.noDataChanged)
                return@onClick
            }

            if (editItem.name != originalItem.name) {
                controller.rename(editItem.uuid, editItem.name)
                originalItem.update(originalItem::name, editItem.name)
            }

            if (editSpace != originalSpace) {
                controller.setSpace(editItem.uuid, editSpace !!.item.uuid)
            }

            if (originalItem.spec != editSpec) {
                controller.setSpec(editItem.uuid, editSpec)
            }
        }
}

@Adaptive
private fun editFields(
    editItem: AvItem<AioDeviceSpec>,
    editSpace : AvNameCacheEntry?,
    spaceNames : List<AvNameCacheEntry>,
    updateSpace : (AvNameCacheEntry?) -> Unit
) {
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
                editItem.update(editItem::name, v)
            }
        }

        withLabel(Strings.area) {
            width { 400.dp }

            select(editSpace, spaceNames) { v -> updateSpace(v) } ..
                inputPlaceholder { "(no item selected)" } ..
                toText<AvNameCacheEntry> { v -> v.names.joinToString(" / ") }
        }
    }
}

@Adaptive
private fun editNotes(editSpec: AioDeviceSpec) {
    withLabel(Strings.note) {
        maxSize .. fill.constrain

        textInputArea(editSpec.notes) { v ->
            editSpec.update(editSpec::notes, v)
        } .. maxSize
    }
}

@Adaptive
private fun points(deviceId : AvValueId) : AdaptiveFragment {
    val points = valueFrom { AvUiList(adapter(), deviceId, PointMarkers.POINTS) }

    withLabel(Strings.points) {
        maxSize .. fill.constrain .. instructions()

        column {
            AioTheme.DEFAULT.itemListContainer

            for (point in points) {
                pointSummary(point.asAvItem())
            }
        }
    }

    return fragment()
}
