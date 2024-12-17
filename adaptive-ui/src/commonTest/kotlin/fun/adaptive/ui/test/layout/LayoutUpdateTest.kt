package `fun`.adaptive.ui.test.layout

import `fun`.adaptive.foundation.instruction.traceAll
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.fit
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.support.C1
import `fun`.adaptive.ui.support.F1
import `fun`.adaptive.ui.testing.uiTest
import kotlin.test.Test

class LayoutUpdateTest {

    @Test
    fun basicLayoutUpdate() {
        uiTest(0, 0, 400, 200, trace = traceAll) {
            box(C1) {
                fit.container
                text("a", F1)
            }
        }.also { adapter ->
            adapter.assertFinal(C1, 0, 0, 400, 200)
            adapter.assertFinal(F1, 0, 0, 20, 20)

            adapter[F1].also {
                it.setStateVariable(0, "ab")
                it.setDirtyBatch(0)
            }

            adapter.assertFinal(C1, 0, 0, 400, 200)
            adapter.assertFinal(F1, 0, 0, 40, 20)
        }

    }

}
