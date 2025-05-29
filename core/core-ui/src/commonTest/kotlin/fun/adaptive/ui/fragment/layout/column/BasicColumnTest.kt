package `fun`.adaptive.ui.fragment.layout.column

import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.testing.snapshotTest
import kotlin.js.JsName
import kotlin.test.Test

class BasicColumnTest {

    val name = "container"

    @Test
    @JsName("basicColumnTest")
    fun `empty column with fix size`() {
        snapshotTest {
            column { name(name) .. width { 40.dp } .. height { 40.dp } }
        }.apply {
            assertFinal(name, 0, 0, 40, 40)
        }
    }

}