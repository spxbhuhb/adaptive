package `fun`.adaptive.sandbox.recipe.ui.container

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.internal.BoundFragmentFactory
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.log.devInfo
import `fun`.adaptive.sandbox.support.configureForm
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.editor.booleanEditor
import `fun`.adaptive.ui.editor.colorEditor
import `fun`.adaptive.ui.editor.dPixelEditor
import `fun`.adaptive.ui.editor.doubleEditor
import `fun`.adaptive.ui.editor.selectEditorList
import `fun`.adaptive.ui.form.AdatFormViewBackend
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.fragment.layout.SplitPaneViewBackend
import `fun`.adaptive.ui.fragment.layout.SplitPaneViewBackend.Companion.splitPaneBackend
import `fun`.adaptive.ui.input.select.item.selectInputOptionCheckbox
import `fun`.adaptive.ui.instruction.decoration.Border
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.*
import `fun`.adaptive.ui.splitpane.SplitPaneTheme
import `fun`.adaptive.ui.splitpane.verticalSplitDivider
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders

@Adaptive
fun containerPlayground(): AdaptiveFragment {

    val form = valueFrom { adatFormBackend(PlaygroundConfig()) }

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
    "aui:grid",
    "splitPane"
)

@Adat
class PlaygroundConfig(
    val container: FragmentKey = containers.first(),
    val layout: LayoutConfig = LayoutConfig(),
    val decoration: DecorationConfig = DecorationConfig(),
    val splitPane: SplitPaneViewBackend = splitPaneBackend(SplitVisibility.Both, SplitMethod.FixFirst, 100.0, Orientation.Horizontal)
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
    val height: Double = 40.0,
    val margin: Margin = Margin.NONE,
    val padding: Padding = Padding.NONE,
    val useMaxWidth: Boolean = true,
    val useMaxHeight: Boolean = false
) {
    fun toInstructions(): AdaptiveInstructionGroup =
        instructionsOf(
            position(top.dp, left.dp),
            if (useMaxWidth) maxWidth else width { width.dp },
            if (useMaxHeight) maxHeight else height { height.dp }
        )
}

@Adat
class DecorationConfig(
    val border: Border = borders.outline,
    val backgroundColor: Color? = null,
) {
    fun toInstructions(): AdaptiveInstructionGroup =
        if ((border.top?.value ?: 0.0) < 0.1) {
            emptyInstructions
        } else {
            instructionsOf(border)
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
                booleanEditor { template.layout.useMaxWidth } .. width { 120.dp }
                booleanEditor { template.layout.useMaxHeight } .. width { 120.dp }
            }

            column {
                gap { 16.dp }
                colorEditor { template.decoration.border.color } .. width { 120.dp }

                row {
                    gap { 16.dp }

                    dPixelEditor { template.decoration.border.top } .. width { 52.dp }
                    dPixelEditor { template.decoration.border.right } .. width { 52.dp }
                    dPixelEditor { template.decoration.border.left } .. width { 52.dp }
                    dPixelEditor { template.decoration.border.bottom } .. width { 52.dp }
                }
            }
        }
    }

}

@Adaptive
fun containerPlaygroundResult(config: PlaygroundConfig) {

    val self = fragment()
    val splitConfig = copyOf { config.splitPane }

    devInfo {
        //config.encodeToPrettyJson()
        config.toInstructions()
    }

    box {
        width { 400.dp } .. height { 400.dp } .. borders.outline

        if (config.container == "splitPane") {
            splitPane(
                splitConfig,
                { box { maxSize .. backgrounds.surface; text("pane1") } },
                { verticalSplitDivider(SplitPaneTheme.outline) },
                { box { maxSize .. backgrounds.surface; text("pane2") } }
            ) .. config.toInstructions()
        } else {
            actualize(config.container, config.toInstructions(), BoundFragmentFactory(self, - 1, ::fakeContent))
        }
    }
}

fun fakeContent(parent: AdaptiveFragment?, declarationIndex: Int): AdaptiveFragment? {
    return null
}
