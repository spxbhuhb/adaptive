package `fun`.adaptive.ui.fragment.layout

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.color
import `fun`.adaptive.ui.api.fit
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.position
import `fun`.adaptive.ui.api.splitPane
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Orientation
import `fun`.adaptive.ui.instruction.layout.SplitMethod
import `fun`.adaptive.ui.instruction.layout.SplitVisibility
import `fun`.adaptive.ui.testing.uiTest
import kotlin.test.Test

class SplitPaneTest {

    @Test
    fun basic() {
        uiTest(0, 0, 400, 200) {
            box {
                splitPane(
                    SplitPaneConfiguration(SplitVisibility.Both, SplitMethod.Proportional, 0.5, Orientation.Horizontal, 4.dp),
                    { text("pane1") },
                    { box(name("hello")) { border(color(0x0)) } },
                    { text("pane2") }
                )
            }
        }.also { adapter ->
//            adapter.assertFinal(F1, 100, 100, 20, 20)
//
//            adapter[F1].also {
//                it.setStateVariable(0, instructionsOf(F1, position(150.dp, 150.dp)))
//                it.setDirtyBatch(0)
//            }
//
//            adapter.assertFinal(F1, 150, 150, 20, 20)
        }
    }
}