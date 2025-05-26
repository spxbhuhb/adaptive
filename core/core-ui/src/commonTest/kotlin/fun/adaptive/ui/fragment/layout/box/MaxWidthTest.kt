package `fun`.adaptive.ui.fragment.layout.box

import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.color
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.marginLeft
import `fun`.adaptive.ui.api.marginRight
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.paddingLeft
import `fun`.adaptive.ui.api.paddingRight
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.testing.snapshotTest
import kotlin.js.JsName
import kotlin.test.Test

class MaxWidthTest {

    val name = "container"

    @Test
    @JsName("changeWidthToMaxWidthAndBack")
    fun `change width to maxWidth and back`() {
        snapshotTest {
            box { name(name) .. width { 40.dp } .. height { 40.dp } }
        }.apply {

            replace(width { 40.dp }, maxWidth)
            assertFinal(name, 0, 0, testWidth, 40)

            replace(maxWidth, width { 50.dp })
            assertFinal(name, 0, 0, 50, 40)

        }
    }

    @Test
    @JsName("maxWidthShouldRespectContainerMargins")
    fun `maxWidth should respect container margins`() {
        snapshotTest {
            box {
                marginLeft { 3.dp } .. marginRight { 5.dp }
                box { name(name) .. maxWidth .. height { 1.dp }}
            }
        }.apply {
            assertFinal(name, 0, 3, testWidth - 8, 1)
        }
    }

    @Test
    @JsName("maxWidthShouldRespectContainerBorder")
    fun `maxWidth should respect container border`() {
        snapshotTest {
            box {
                border(color(0x0), 1.dp)
                box { name(name) .. maxWidth .. height { 1.dp }}
            }
        }.apply {
            assertFinal(name, 1, 1, testWidth - 2, 1)
        }
    }

    @Test
    @JsName("maxWidthShouldRespectContainerPadding")
    fun `maxWidth should respect container padding`() {
        snapshotTest {
            box {
                paddingLeft { 3.dp } .. paddingRight { 5.dp }
                box { name(name) .. maxWidth .. height { 1.dp }}
            }
        }.apply {
            assertFinal(name, 0, 3, testWidth - 8, 1)
        }
    }

}