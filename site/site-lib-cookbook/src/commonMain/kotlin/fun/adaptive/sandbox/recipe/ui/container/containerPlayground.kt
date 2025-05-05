package `fun`.adaptive.sandbox.recipe.ui.container

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.internal.BoundFragmentFactory
import `fun`.adaptive.sandbox.support.configureForm
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.editor.colorEditor
import `fun`.adaptive.ui.editor.doubleEditor
import `fun`.adaptive.ui.editor.selectEditorList
import `fun`.adaptive.ui.form.AdatFormViewBackend
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.input.select.item.selectInputOptionCheckbox
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors

@Adaptive
fun containerPlayground(): AdaptiveFragment {

    val form = adatFormBackend(PlaygroundConfig())

    flowBox {
        gap { 16.dp }
        containerPlaygroundForm(form)
        containerPlaygroundResult(form.inputValue)
    }

    return fragment()
}

val containers = listOf(
    "aui:box",
    "aui:column",
    "aui:row",
    "aui:grid"
)

@Adat
class PlaygroundConfig(
    val container: FragmentKey = containers.first(),
    val layout: LayoutConfig = LayoutConfig(),
    val decoration: DecorationConfig = DecorationConfig()
) {
    fun toInstructions(): AdaptiveInstructionGroup {
        return instructionsOf(layout.toInstructions(), decoration.toInstructions())
    }
}


@Adat
class LayoutConfig(
    val top: Double = 20.0,
    val left: Double = 20.0,
    val width: Double = 40.0,
    val height: Double = 40.0
) {
    fun toInstructions(): AdaptiveInstructionGroup =
        instructionsOf(
            position(top.dp, left.dp),
            width { width.dp },
            height { height.dp }
        )
}

@Adat
class DecorationConfig(
    val borderColor: Color = colors.outline,
    val borderWidth: Double = 1.0,
    val backgroundColor: Color? = null,
) {
    fun toInstructions(): AdaptiveInstructionGroup =
        if (borderWidth < 0.1) {
            emptyInstructions
        } else {
            instructionsOf(border(borderColor, borderWidth.dp))
        }
}


@Adaptive
fun containerPlaygroundForm(
    form: AdatFormViewBackend<PlaygroundConfig>
) {

    val template = PlaygroundConfig()

    configureForm(form) {
        width { 288.dp }

        column {
            gap { 8.dp } .. padding { 16.dp }

            selectEditorList(containers, { selectInputOptionCheckbox(it) }) { template.container } .. width { 256.dp }

            row {
                gap { 16.dp }
                doubleEditor { template.layout.top } .. width { 120.dp }
                doubleEditor { template.layout.left } .. width { 120.dp }
            }

            row {
                gap { 16.dp }
                doubleEditor { template.layout.width } .. width { 120.dp }
                doubleEditor { template.layout.height } .. width { 120.dp }
            }

            row {
                gap { 16.dp }
                colorEditor { template.decoration.borderColor } .. width { 120.dp }
                doubleEditor { template.decoration.borderWidth } .. width { 120.dp }
            }
        }
    }

}

@Adaptive
fun containerPlaygroundResult(config: PlaygroundConfig) {

    val self = fragment()

    box {
        width { 400.dp } .. height { 400.dp } .. borders.outline

        actualize(config.container, config.toInstructions(), BoundFragmentFactory(self, -1, ::fakeContent))
    }
}

fun fakeContent(parent: AdaptiveFragment?, declarationIndex: Int): AdaptiveFragment? {
    return null
}
