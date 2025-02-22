package `fun`.adaptive.cookbook.ui.splitpane

import `fun`.adaptive.adat.store.copyStore
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.fragment.layout.SplitPaneConfiguration
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Orientation
import `fun`.adaptive.ui.instruction.layout.SplitMethod
import `fun`.adaptive.ui.instruction.layout.SplitVisibility
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders

@Adaptive
fun splitPaneRecipe() {

    column {
        gap { 16.dp } .. maxSize .. verticalScroll

        text(SplitMethod.FixFirst)
        sp { SplitPaneConfiguration(SplitVisibility.Both, SplitMethod.FixFirst, 100.0, Orientation.Horizontal, 4.dp) }

        text(SplitMethod.FixSecond)
        sp { SplitPaneConfiguration(SplitVisibility.Both, SplitMethod.FixSecond, 100.0, Orientation.Horizontal, 4.dp) }

        text(SplitMethod.Proportional)
        sp { SplitPaneConfiguration(SplitVisibility.Both, SplitMethod.Proportional, 0.4, Orientation.Horizontal, 4.dp) }

    }

}

@Adaptive
private fun sp(configFun: () -> SplitPaneConfiguration) {

    // When the `onChange` parameter of `splitPane` is null, it uses `update` of the config
    // to apply distribution changes.

    val config = copyStore { configFun() }

    box {
        height { 300.dp } .. maxWidth

        splitPane(
            config,
            { text("pane1") },
            { box { maxSize .. backgrounds.primary } },
            { text("pane2") }
        ) .. maxSize .. borders.outline .. margin { 16.dp }

    }

}