package `fun`.adaptive.ui.fragment.layout.box

import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.testing.snapshotTest
import kotlin.js.JsName
import kotlin.test.Test

class MaxHeightTest {

    val name = "container"
    
    @Test
    @JsName("changeHeightToMaxHeightAndBack")
    fun `change height to maxHeight and back`() {
        snapshotTest {
            box { name(name) .. width { 40.dp } .. height { 40.dp } }
        }.apply {

            replace(height { 40.dp }, maxHeight)
            assertFinal(name, 0, 0, 40, testHeight)

            replace(maxHeight, height { 50.dp })
            assertFinal(name, 0, 0, 40, 50)

        }
    }

    @Test
    @JsName("maxHeightShouldRespectContainerMargins")
    fun `maxHeight should respect container margins`() {
        snapshotTest {
            box {
                marginTop { 3.dp } .. marginBottom { 5.dp }
                box { name(name) .. maxHeight .. width { 1.dp }}
            }
        }.apply {
            assertFinal(name, 3, 0, 1, testHeight - 8)
        }
    }

    @Test
    @JsName("maxHeightShouldRespectContainerBorder")
    fun `maxHeight should respect container border`() {
        snapshotTest {
            box {
                border(color(0x0), 1.dp)
                box { name(name) .. maxHeight .. width { 1.dp }}
            }
        }.apply {
            assertFinal(name, 1, 1, 1, testHeight - 2)
        }
    }

    @Test
    @JsName("maxHeightShouldRespectContainerPadding")
    fun `maxHeight should respect container padding`() {
        snapshotTest {
            box {
                paddingTop { 3.dp } .. paddingBottom { 5.dp }
                box { name(name) .. maxHeight .. width { 1.dp }}
            }
        }.apply {
            assertFinal(name, 3, 0, 1, testHeight - 8)
        }
    }

}