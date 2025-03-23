package `fun`.adaptive.iot.space.ui.editor

import `fun`.adaptive.adaptive_lib_iot.generated.resources.*
import `fun`.adaptive.adat.api.update
import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.common.AioTheme
import `fun`.adaptive.iot.device.ui.controllerSummary
import `fun`.adaptive.iot.space.AioSpaceSpec
import `fun`.adaptive.iot.space.SpaceMarkers
import `fun`.adaptive.iot.space.ui.localizedSpaceType
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.input.InputContext
import `fun`.adaptive.ui.input.number.doubleOrNullUnitInput
import `fun`.adaptive.ui.input.text.textInput
import `fun`.adaptive.ui.input.text.textInputArea
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.label.uuidLabel
import `fun`.adaptive.ui.label.withLabel
import `fun`.adaptive.ui.snackbar.warningNotification
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvItem.Companion.asAvItem
import `fun`.adaptive.value.ui.AvUiList

fun wsSpaceEditorContentDef(context: AioWsContext) {
    val workspace = context.workspace

    workspace.addContentPaneBuilder(AioWsContext.WSIT_SPACE) { item ->
        WsPane(
            UUID(),
            item.name,
            context[item].icon,
            WsPanePosition.Center,
            AioWsContext.WSPANE_SPACE_CONTENT,
            controller = SpaceEditorContentController(workspace),
            data = item.asAvItem()
        )
    }
}

@Adaptive
fun wsSpaceContentPane(pane: WsPane<AvItem<AioSpaceSpec>, SpaceEditorContentController>): AdaptiveFragment {

    val originalItem = copyOf { pane.data }

    val editItem = copyOf { pane.data }
    val editSpec = copyOf { pane.data.specific !! }

    grid {
        maxSize .. padding { 16.dp } .. backgrounds.surface
        colTemplate(400.dp, 1.fr)
        rowTemplate(56.dp, 312.dp, 1.fr)
        gap { 16.dp }

        title(originalItem)
        actions(pane.controller, originalItem, editItem, editSpec)

        editFields(editItem, editSpec)
        editNotes(editSpec)

        points(pane.controller)
        devices(originalItem.uuid)
    }

    return fragment()
}

@Adaptive
fun title(
    item: AvItem<AioSpaceSpec>,
) {
    column {
        paddingBottom { 32.dp }
        h2(item.name.ifEmpty { Strings.noname })
        uuidLabel { item.uuid }
    }
}

@Adaptive
fun actions(
    controller: SpaceEditorContentController,
    originalItem: AvItem<AioSpaceSpec>,
    editItem: AvItem<AioSpaceSpec>,
    editSpec: AioSpaceSpec
) {
    button(Strings.save) ..
        alignSelf.end ..
        onClick {
            if (editItem.name == originalItem.name && editSpec == originalItem.specific) {
                warningNotification(Strings.noDataChanged)
                return@onClick
            }

            if (editItem.name != originalItem.name) {
                controller.rename(editItem.uuid, editItem.name)
                originalItem.update(originalItem::name, editItem.name)
            }
            if (originalItem.specific != editSpec) {
                controller.setSpaceSpec(originalItem.uuid, editSpec)
            }
        }
}

@Adaptive
private fun editFields(
    editItem: AvItem<AioSpaceSpec>,
    editSpec: AioSpaceSpec
) {
    column {

        gap { 24.dp }

        withLabel(Strings.spxbId, InputContext(disabled = true)) { state ->
            width { 400.dp }
            textInput(editItem.friendlyId, state) { }
        }

        withLabel(Strings.type, InputContext(disabled = true)) { state ->
            width { 400.dp }
            textInput(editItem.localizedSpaceType, state) { }
        }


        withLabel(Strings.name) {
            width { 400.dp }
            textInput(editItem.name) { v ->
                editItem.update(editItem::name, v)
            }
        }

        withLabel(Strings.area) { state ->
            width { 120.dp }
            doubleOrNullUnitInput(editSpec.area, 0, "m²", state) { v ->
                editSpec.update(editSpec::area, v)
            }
        }
    }
}

@Adaptive
private fun editNotes(editSpec: AioSpaceSpec) {
    withLabel(Strings.note) {
        maxSize .. fill.constrain

        textInputArea(editSpec.notes) { v ->
            editSpec.update(editSpec::notes, v)
        } .. maxSize
    }
}

@Adaptive
private fun devices(spaceId : AvValueId) {
    val devices = valueFrom { AvUiList(adapter(), spaceId, SpaceMarkers.SPACE_DEVICES) }

    withLabel(Strings.devices) {
        maxSize .. fill.constrain

        column {
            AioTheme.DEFAULT.controllerListContainer

            for (device in devices) {
                controllerSummary(device.asAvItem())
            }
        }
    }
}

@Adaptive
private fun points(controller: SpaceEditorContentController) {
    val deviceIds = valueFrom { controller.devices }

    withLabel(Strings.points) {
        maxSize .. fill.constrain

        column {
            maxSize .. verticalScroll .. borders.outline .. paddingBottom { 16.dp } .. cornerRadius { 8.dp }

            for (deviceId in deviceIds) {
                controllerSummary(controller.getDevice(deviceId))
            }
        }
    }
}

