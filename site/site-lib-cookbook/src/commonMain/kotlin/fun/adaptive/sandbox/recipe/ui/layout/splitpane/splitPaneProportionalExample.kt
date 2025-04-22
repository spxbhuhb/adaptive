package `fun`.adaptive.sandbox.recipe.ui.layout.splitpane

import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.sandbox.support.hardCodedExample
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.fragment.layout.SplitPaneViewBackend.Companion.splitPaneBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Orientation
import `fun`.adaptive.ui.instruction.layout.SplitMethod
import `fun`.adaptive.ui.instruction.layout.SplitVisibility
import `fun`.adaptive.ui.splitpane.SplitPaneTheme
import `fun`.adaptive.ui.splitpane.verticalSplitDivider
import `fun`.adaptive.ui.theme.borders

@Adaptive
fun splitPaneProportionalExample(): AdaptiveFragment {

    val config = copyOf { splitPaneBackend(SplitVisibility.Both, SplitMethod.FixFirst, 100.0, Orientation.Horizontal) }

    hardCodedExample(
        """
            * SplitVisibility.Both
            * SplitMethod.FixFirst
            * split value: 100.0
            * Orientation.Horizontal
        """.trimIndent()
    ) {
        box {
            width { 256.dp } .. height { 100.dp }

            splitPane(
                config,
                { text("pane1") },
                { verticalSplitDivider(SplitPaneTheme.outline) },
                { text("pane2") }
            ) .. maxSize .. borders.outline .. margin { 16.dp }

        }
    }

    return fragment()
}