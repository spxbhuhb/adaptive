package `fun`.adaptive.iot.space.ui

import `fun`.adaptive.adaptive_lib_iot.generated.resources.*
import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.item.AioItem
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.InputContext
import `fun`.adaptive.ui.input.number.doubleOrNullUnitInput
import `fun`.adaptive.ui.input.text.textInput
import `fun`.adaptive.ui.input.text.textInputArea
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.label.uuidLabel
import `fun`.adaptive.ui.label.withLabel
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.workspace.Workspace.Companion.wsContext
import `fun`.adaptive.ui.workspace.model.WsPane

@Adaptive
fun wsSpaceContentPane(pane : WsPane<*>): AdaptiveFragment {
//    val context = fragment().wsContext<AioWsContext>()
//
//    val observed = valueFrom { context.spaceTree }
//
//    val item = observed.selection.firstOrNull()
//
//    column {
//        maxSize
//
//        if (item == null) {
//            text(Strings.selectArea) .. alignSelf.center .. textColors.onSurfaceVariant
//        } else {
//            areaEditor(context, item)
//        }
//    }

    return fragment()
}

@Adaptive
fun areaEditor(context: AioWsContext, item: TreeItem<AioItem>) {
//    val observed = valueFrom { item }
//    val space = observed.data
//
//    column {
//        maxSize .. verticalScroll .. padding { 16.dp } .. backgrounds.surface
//
//        column {
//            paddingBottom { 32.dp }
//            h2(space.name.ifEmpty { Strings.noname })
//            uuidLabel { space.uuid }
//        }
//
//        column {
//
//            gap { 24.dp }
//
//            withLabel(Strings.spxbId, InputContext(disabled = true)) { state ->
//                width { 400.dp }
//                textInput(space.friendlyId, state) { }
//            }
//
//            withLabel(Strings.type, InputContext(disabled = true)) { state ->
//                width { 400.dp }
//                textInput(space.spaceType.localized(), state) { }
//            }
//
//            withLabel(Strings.area) { state ->
//                width { 120.dp }
//                doubleOrNullUnitInput(space.area, 0, "m²", state) { v ->
//                    // FIXME space editor update mess
//                    observed.data = space.copy(area = v)
//                    context.updateSpace(space)
//                }
//            }
//
//            withLabel(Strings.name) {
//                width { 400.dp }
//                textInput(space.name) { v ->
//                    item.title = v
//                    // FIXME space editor update mess
//                    observed.data = space.copy(name = v)
//                    context.updateSpace(space)
//                }
//            }
//
//            withLabel(Strings.note) {
//                width { 400.dp }
//                textInputArea(space.notes) { v ->
//                    // FIXME space editor update mess
//                    observed.data = space.copy(notes = v)
//                    context.updateSpace(space)
//                } .. height { 300.dp }
//            }
//        }
//    }
}