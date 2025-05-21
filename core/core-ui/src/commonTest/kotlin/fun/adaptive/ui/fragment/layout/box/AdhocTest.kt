package `fun`.adaptive.ui.fragment.layout.box

import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.testing.snapshotTest
import kotlin.test.Test

class AdhocTest {

    @Test
    fun test() {
        val test = snapshotTest {
            box { width { 40.dp } .. height { 40.dp } }
        }

        test.replace(width { 40.dp }, maxWidth)
    }
}