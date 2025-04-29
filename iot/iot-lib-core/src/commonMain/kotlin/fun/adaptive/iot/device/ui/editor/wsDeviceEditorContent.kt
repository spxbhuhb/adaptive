package `fun`.adaptive.iot.device.ui.editor

import `fun`.adaptive.adat.api.update
import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.*
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.common.AioTheme
import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.iot.device.ui.localizedDeviceType
import `fun`.adaptive.iot.generated.resources.*
import `fun`.adaptive.iot.point.PointMarkers
import `fun`.adaptive.iot.point.ui.pointSummary
import `fun`.adaptive.iot.space.SpaceMarkers
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.editor.textEditor
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.input.InputConfig.Companion.inputConfig
import `fun`.adaptive.ui.input.select.item.selectInputOptionText
import `fun`.adaptive.ui.input.select.item.selectInputValueText
import `fun`.adaptive.ui.input.select.selectInputBackend
import `fun`.adaptive.ui.input.select.selectInputDropdown
import `fun`.adaptive.ui.input.text.textInput2
import `fun`.adaptive.ui.input.text.textInputArea
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.label.uuidLabel
import `fun`.adaptive.ui.label.withLabel
import `fun`.adaptive.ui.snackbar.warningNotification
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.value.AvNameCacheEntry
import `fun`.adaptive.ui.value.AvUiList
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvItem.Companion.asAvItem

@Adaptive
fun wsDeviceContentPane(pane: WsPane<AvItem<AioDeviceSpec>, DeviceEditorContentController>): AdaptiveFragment {

    val originalItem = copyOf { pane.data }

    val form = adatFormBackend(pane.data)

    val editItem = copyOf { pane.data }
    val editSpec = copyOf { pane.data.spec }

    val spaceNames = valueFrom { pane.controller.spaceNameCache }

    val originalSpace = spaceNames.firstOrNull { it.item.uuid == editItem.markers[SpaceMarkers.SPACE_REF] }
    var editSpace = spaceNames.firstOrNull { it.item.uuid == editItem.markers[SpaceMarkers.SPACE_REF] }

    grid {
        maxSize .. padding { 16.dp } .. backgrounds.surface
        colTemplate(400.dp, 1.fr)
        rowTemplate(56.dp, 312.dp, 1.fr)
        gap { 16.dp }

        localContext(form) {
            title(originalItem)
            actions(pane.controller, originalItem, editItem, editSpec, originalSpace, editSpace)

            editFields(editItem, editSpace, spaceNames) { editSpace = it }
            editNotes(editSpec)

            points(originalItem.uuid) .. colSpan(2)
        }
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
    originalSpace: AvNameCacheEntry?,
    editSpace: AvNameCacheEntry?
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
                editItem.update(editItem::markersOrNull, originalItem.toMutableMarkers().also { it[SpaceMarkers.SPACE_REF] = editSpace.item.uuid })
            }

            if (originalItem.spec != editSpec) {
                controller.setSpec(editItem.uuid, editSpec)
                originalItem.update(originalItem::spec, editItem.spec)
            }
        }
}

@Adaptive
private fun editFields(
    editItem: AvItem<AioDeviceSpec>,
    editSpace: AvNameCacheEntry?,
    spaceNames: List<AvNameCacheEntry>,
    updateSpace: (AvNameCacheEntry?) -> Unit
) {

    val backend = selectInputBackend<AvNameCacheEntry> {
        this.options = spaceNames
        label = Strings.space
        toText = { it.names.joinToString(" / ") }
        withSurfaceContainer = true
        //onChange = { updateSpace(it) }
    }


    column {
        width { 400.dp } .. gap { 24.dp }

        textEditor { editItem.friendlyId } .. inputConfig(label = Strings.spxbId, disabled = true)
        textInput2 { editItem.localizedDeviceType } .. inputConfig(label = Strings.type, disabled = true)
        textEditor { editItem.name }

        selectInputDropdown(backend, { selectInputOptionText(it) }) { selectInputValueText(it) }

//        withLabel(Strings.area) {
//            width { 400.dp }
//
//            select(editSpace, spaceNames) { v -> updateSpace(v) } ..
//                inputPlaceholder { "(no item selected)" } ..
//                toText<AvNameCacheEntry> { v -> v.names.joinToString(" / ") }
//        }
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
private fun points(deviceId: AvValueId): AdaptiveFragment {
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
