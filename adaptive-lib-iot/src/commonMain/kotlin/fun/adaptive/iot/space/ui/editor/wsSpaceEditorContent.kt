package `fun`.adaptive.iot.space.ui.editor

import `fun`.adaptive.adaptive_lib_iot.generated.resources.*
import `fun`.adaptive.adat.api.update
import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.iot.space.AioSpaceSpec
import `fun`.adaptive.value.item.AvItem
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
import `fun`.adaptive.ui.label.uuidLabel
import `fun`.adaptive.ui.label.withLabel
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.item.AvItem.Companion.asAvItem

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

    val originalSpec = originalItem.specific!!
    val editSpec = editItem.specific!!

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
                textInput(editItem.localizedSpaceType, state) { }
            }

            withLabel(Strings.area) { state ->
                width { 120.dp }
                doubleOrNullUnitInput(editSpec.area, 0, "m²", state) { v ->
                    editSpec.update(editSpec::area, v)
                }
            }

            withLabel(Strings.name) {
                width { 400.dp }
                textInput(editItem.name) { v ->
                    println("update: ${editItem.name} $v")
                    editItem.update(editItem::name, v)
                }
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
                    pane.controller.setSpaceSpec(pane.data.uuid, editSpec)
                }
            }
        }
    }

    return fragment()
}