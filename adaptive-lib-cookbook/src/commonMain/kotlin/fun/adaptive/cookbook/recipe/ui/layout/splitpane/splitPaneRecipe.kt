package `fun`.adaptive.cookbook.recipe.ui.layout.splitpane

import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.fragment.layout.SplitPaneConfiguration
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Orientation
import `fun`.adaptive.ui.instruction.layout.SplitMethod
import `fun`.adaptive.ui.instruction.layout.SplitVisibility
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors

@Adaptive
fun splitPaneRecipe(): AdaptiveFragment {

    column {
        gap { 16.dp } .. maxSize .. verticalScroll

        text(SplitMethod.FixFirst)
        sp { SplitPaneConfiguration(SplitVisibility.Both, SplitMethod.FixFirst, 100.0, Orientation.Horizontal) }

        text(SplitMethod.FixSecond)
        sp { SplitPaneConfiguration(SplitVisibility.Both, SplitMethod.FixSecond, 100.0, Orientation.Horizontal) }

        text(SplitMethod.Proportional)
        sp { SplitPaneConfiguration(SplitVisibility.Both, SplitMethod.Proportional, 0.4, Orientation.Horizontal) }

    }

    return fragment()
}

@Adaptive
private fun sp(configFun: () -> SplitPaneConfiguration) {

    // When the `onChange` parameter of `splitPane` is null, it uses `update` of the config
    // to apply distribution changes.

    val config = copyOf { configFun() }

    box {
        height { 300.dp } .. maxWidth

        splitPane(
            config,
            { text("pane1") },
            { divider() },
            { text("pane2") }
        ) .. maxSize .. borders.outline .. margin { 16.dp }

    }

}

@Adaptive
private fun divider() {
    box {
        maxHeight
        width { 9.dp }
        zIndex { 300 }
        paddingHorizontal { (9.dp - 1.dp) / 2.dp }
        cursor.colResize

        box {
            maxHeight
            width { 1.dp }
            borderLeft(colors.outline)
        }
    }
}