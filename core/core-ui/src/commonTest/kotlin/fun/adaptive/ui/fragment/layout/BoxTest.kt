package `fun`.adaptive.ui.fragment.layout

import `fun`.adaptive.foundation.dumpFragmentTree
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.alignSelf
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.fit
import `fun`.adaptive.ui.api.position
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.AlignItems
import `fun`.adaptive.ui.instruction.layout.AlignSelf
import `fun`.adaptive.ui.support.C1
import `fun`.adaptive.ui.support.F1
import `fun`.adaptive.ui.testing.uiTest
import kotlin.test.Test

class BoxTest {

    fun f(top: Int, left: Int, width: Int, height: Int) =
        RawFrame(top.toDouble(), left.toDouble(), width.toDouble(), height.toDouble())

    @Test
    fun alignSelf_changeSize() {
        alignSelfBase(alignSelf.topStart, f(0, 0, 20, 20), f(0, 0, 40, 20))
        alignSelfBase(alignSelf.topCenter, f(0, 190, 20, 20), f(0, 180, 40, 20))
        alignSelfBase(alignSelf.topEnd, f(0, 380, 20, 20), f(0, 360, 40, 20))
        alignSelfBase(alignSelf.startCenter, f(90, 0, 20, 20), f(90, 0, 40, 20))
        alignSelfBase(alignSelf.center, f(90, 190, 20, 20), f(90, 180, 40, 20))
        alignSelfBase(alignSelf.endCenter, f(90, 380, 20, 20), f(90, 360, 40, 20))
        alignSelfBase(alignSelf.bottomStart, f(180, 0, 20, 20), f(180, 0, 40, 20))
        alignSelfBase(alignSelf.bottomCenter, f(180, 190, 20, 20), f(180, 180, 40, 20))
        alignSelfBase(alignSelf.bottomEnd, f(180, 380, 20, 20), f(180, 360, 40, 20))
    }

    fun alignSelfBase(
        alignment: AlignSelf,
        initial: RawFrame,
        result: RawFrame
    ) {
        uiTest(0, 0, 400, 200) {
            box(C1) {
                fit.container
                text("a", F1) .. alignment
            }
        }.also { adapter ->

            adapter.assertFinal(C1, 0, 0, 400, 200)
            adapter.assertFinal(F1, initial)

            adapter[F1].also {
                it.setStateVariable(1, "ab")
                it.setDirtyBatch(1)
            }

            adapter.assertFinal(C1, 0, 0, 400, 200)
            adapter.assertFinal(F1, result)

        }
    }

    @Test
    fun alignItems_changeSize() {
        alignItemsBase(alignItems.topStart, f(0, 0, 20, 20), f(0, 0, 40, 20))
        alignItemsBase(alignItems.topCenter, f(0, 190, 20, 20), f(0, 180, 40, 20))
        alignItemsBase(alignItems.topEnd, f(0, 380, 20, 20), f(0, 360, 40, 20))
        alignItemsBase(alignItems.startCenter, f(90, 0, 20, 20), f(90, 0, 40, 20))
        alignItemsBase(alignItems.center, f(90, 190, 20, 20), f(90, 180, 40, 20))
        alignItemsBase(alignItems.endCenter, f(90, 380, 20, 20), f(90, 360, 40, 20))
        alignItemsBase(alignItems.bottomStart, f(180, 0, 20, 20), f(180, 0, 40, 20))
        alignItemsBase(alignItems.bottomCenter, f(180, 190, 20, 20), f(180, 180, 40, 20))
        alignItemsBase(alignItems.bottomEnd, f(180, 380, 20, 20), f(180, 360, 40, 20))
    }

    fun alignItemsBase(
        alignment: AlignItems,
        initial: RawFrame,
        result: RawFrame
    ) {
        uiTest(0, 0, 400, 200) {
            box(C1) {
                fit.container .. alignment
                text("a", F1)
            }
        }.also { adapter ->

            adapter.assertFinal(C1, 0, 0, 400, 200)
            adapter.assertFinal(F1, initial)

            adapter[F1].also {
                it.setStateVariable(1, "ab")
                it.setDirtyBatch(1)
            }

            adapter.assertFinal(C1, 0, 0, 400, 200)
            adapter.assertFinal(F1, result)

        }
    }

    @Test
    fun changePosition() {
        uiTest(0, 0, 400, 200) {
            box(C1) {
                fit.container
                text("a", F1) .. position(100.dp, 100.dp)
            }
        }.also { adapter ->
            adapter.assertFinal(F1, 100, 100, 20, 20)

            adapter[F1].also {
                it.setStateVariable(0, instructionsOf(F1, position(150.dp, 150.dp)))
                it.setDirtyBatch(0)
            }

            adapter.assertFinal(F1, 150, 150, 20, 20)
        }
    }

    @Test
    fun changeSize() {
        uiTest(0, 0, 400, 200) {
            box(C1) {
                fit.container
                text("a", F1) .. size(100.dp, 100.dp)
            }
        }.also { adapter ->
            adapter.assertFinal(F1, 0, 0, 100, 100)

            adapter[F1].also {
                it.setStateVariable(0, instructionsOf(F1, size(150.dp, 150.dp)))
                it.setDirtyBatch(0)
            }

            adapter.assertFinal(F1, 0, 0, 150, 150)
        }
    }
}