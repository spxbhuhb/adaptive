package `fun`.adaptive.doc.playground

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.doc.support.configureForm
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.internal.BoundFragmentFactory
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.grove.ufd.model.LayoutConfig
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.editor.colorEditor
import `fun`.adaptive.ui.editor.dPixelEditor
import `fun`.adaptive.ui.editor.doubleEditor
import `fun`.adaptive.ui.editor.selectEditorList
import `fun`.adaptive.ui.form.AdatFormViewBackend
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.fragment.layout.SplitPaneViewBackend
import `fun`.adaptive.ui.fragment.layout.SplitPaneViewBackend.Companion.splitPaneBackend
import `fun`.adaptive.ui.fragment.layout.cellbox.CellDef
import `fun`.adaptive.ui.input.select.item.selectInputOptionCheckbox
import `fun`.adaptive.ui.instruction.decoration.Border
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Orientation
import `fun`.adaptive.ui.instruction.layout.SplitMethod
import `fun`.adaptive.ui.instruction.layout.SplitVisibility
import `fun`.adaptive.ui.splitpane.SplitPaneTheme
import `fun`.adaptive.ui.splitpane.verticalSplitDivider
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.textColors

@Adaptive
fun containerPlayground(): AdaptiveFragment {

    val form = observe { adatFormBackend(PlaygroundConfig()) }

    flowBox {
        gap { 16.dp }
        containerPlaygroundForm(form)
        containerPlaygroundResult(form.inputValue)
    }

    return fragment()
}

val containers = listOf(
    "aui:box",
    "aui:cellbox",
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
                gap { 8.dp }

                doubleEditor { template.layout.top } .. width { 58.dp }
                doubleEditor { template.layout.left } .. width { 58.dp }
                doubleEditor { template.layout.width } .. width { 58.dp }
                doubleEditor { template.layout.height } .. width { 58.dp }
            }

            row {
                gap { 16.dp }
            }

            column {
                gap { 16.dp }
                colorEditor { template.decoration.border.color } .. width { 120.dp }

                row {
                    gap { 8.dp }

                    dPixelEditor { template.decoration.border.top } .. width { 58.dp }
                    dPixelEditor { template.decoration.border.right } .. width { 58.dp }
                    dPixelEditor { template.decoration.border.left } .. width { 58.dp }
                    dPixelEditor { template.decoration.border.bottom } .. width { 58.dp }
                }
            }
        }
    }

}

@Adaptive
fun containerPlaygroundResult(config: PlaygroundConfig) {

    val self = fragment()
    val splitConfig = copyOf { config.splitPane }

    box {
        width { 400.dp } .. height { 400.dp } .. borders.outline

        when (config.container) {
            "splitPane" -> {
                splitPane(
                    splitConfig,
                    { box { maxSize .. backgrounds.surface; text("pane1") } },
                    { verticalSplitDivider(SplitPaneTheme.outline) },
                    { box { maxSize .. backgrounds.surface; text("pane2") } }
                ) .. config.toInstructions()
            }

            "aui:cellbox" -> {
                cellBox(
                    cells = listOf(
                        CellDef(null, 100.dp),
                        CellDef(null, 100.dp)
                    )
                ) {
                    text("Hello") .. backgrounds.infoSurface .. textColors.onInfoSurface .. maxWidth
                    text("World!") .. backgrounds.warningSurface .. textColors.onWarningSurface .. maxWidth
                } .. config.toInstructions()
            }

            else -> {
                actualize(config.container, null, config.toInstructions(), BoundFragmentFactory(self, - 1, ::fakeContent))
            }
        }
    }
}

fun fakeContent(parent: AdaptiveFragment?, declarationIndex: Int): AdaptiveFragment? {
    return null
}
