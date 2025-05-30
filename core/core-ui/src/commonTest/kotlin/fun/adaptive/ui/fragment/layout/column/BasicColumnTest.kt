package `fun`.adaptive.ui.fragment.layout.column

import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.fillStrategy
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.maxHeight
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.testing.snapshotTest
import kotlin.js.JsName
import kotlin.test.Test

class BasicColumnTest {

    val name = "container"
    val child1 = "child1"
    val child2 = "child2"

    @Test
    @JsName("emptyWithFixSize")
    fun `empty with fix size`() {
        snapshotTest {
            column { name(name) .. width { 40.dp } .. height { 40.dp } }
        }.apply {
            assertFinal(name, 0, 0, 40, 40)
        }
    }

    @Test
    @JsName("twoChildrenWithFixSize")
    fun `two children with fix size`() {
        snapshotTest {
            column {
                name(name)
                box { size(40.dp) .. name(child1) }
                box { size(50.dp) .. name(child2) }
            }
        }.apply {
            // width is the maximum width, height is the sum of heights
            assertFinal(name, 0, 0, 50, 90)
        }
    }

    @Test
    @JsName("twoChildrenAndConstrain")
    fun `two children and constrain strategy`() {
        snapshotTest {
            column {
                name(name) .. size(100.dp) .. fillStrategy.constrain
                box { size(40.dp) .. name(child1) }
                box { name(child2) .. maxSize }
            }
        }.apply {
            assertFinal(name, 0, 0, 100, 100)
            assertFinal(child1, 0, 0, 40, 40)
            assertFinal(child2, 40, 0, 100, 60)

            replace(height { 40.dp }, maxHeight)
        }
    }

}