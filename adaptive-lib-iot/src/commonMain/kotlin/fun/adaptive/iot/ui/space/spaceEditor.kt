package `fun`.adaptive.iot.ui.space

import `fun`.adaptive.adaptive_lib_iot.generated.resources.name
import `fun`.adaptive.adaptive_lib_iot.generated.resources.note
import `fun`.adaptive.adaptive_lib_iot.generated.resources.selectArea
import `fun`.adaptive.adaptive_lib_iot.generated.resources.spxbId
import `fun`.adaptive.adaptive_lib_iot.generated.resources.type
import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.model.project.AioProject
import `fun`.adaptive.iot.model.space.AioSpace
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.alignSelf
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.paddingBottom
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.input.InputContext
import `fun`.adaptive.ui.input.text.textInput
import `fun`.adaptive.ui.input.text.textInputArea
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.label.uuidLabel
import `fun`.adaptive.ui.label.withLabel
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewModel


@Adaptive
fun spaceEditor(treeViewModel: TreeViewModel<AioSpace, AioProject>): AdaptiveFragment {
    val observed = valueFrom { treeViewModel }

    val item = observed.selection.firstOrNull()

    column {
        maxSize

        if (item == null) {
            text(Strings.selectArea) .. alignSelf.center .. textColors.onSurfaceVariant
        } else {
            areaEditor(item)
        }
    }

    return fragment()
}

@Adaptive
fun areaEditor(item: TreeItem<AioSpace>) {
    val observed = valueFrom { item }
    val space = observed.data

    column {
        maxSize .. verticalScroll .. padding { 16.dp } .. backgrounds.surface

        column {
            paddingBottom { 32.dp }
            h2(space.name)
            uuidLabel { space.uuid }
        }

        column {

            gap { 24.dp }

            withLabel(Strings.spxbId, InputContext(disabled = true)) { state ->
                width { 400.dp }
                textInput(space.friendlyId, state) { }
            }

            withLabel(Strings.type, InputContext(disabled = true)) { state ->
                width { 400.dp }
                textInput(space.spaceType.localized(), state) { }
            }

            withLabel(Strings.name) {
                width { 400.dp }
                textInput(space.name) { v ->
                    item.title = v
                    observed.data = space.copy(name = v)
                }
            }

            withLabel(Strings.note) {
                width { 400.dp }
                textInputArea(space.notes) { v ->
                    observed.data = space.copy(notes = v)
                } .. height { 400.dp }
            }
        }
    }
}