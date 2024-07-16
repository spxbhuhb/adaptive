package hu.simplexion.adaptive.designer

import hu.simplexion.adaptive.designer.instruction.instructions
import hu.simplexion.adaptive.designer.overlay.overlay
import hu.simplexion.adaptive.designer.palette.palette
import hu.simplexion.adaptive.designer.utility.hits
import hu.simplexion.adaptive.designer.utility.noHit
import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.rangeTo
import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.fragment.box
import hu.simplexion.adaptive.ui.common.fragment.grid
import hu.simplexion.adaptive.ui.common.instruction.*

val blueBackground = backgroundColor(0x0000ff)
val greenBackground = backgroundColor(0x00ff00)
val redBackground = backgroundColor(0xff0000)

val canvasBackground = backgroundColor(0xf8f8f8)
val canvasBorder = border(color(0xd0d0d0), 1.dp)

@Adaptive
fun designerMain() {
    var selection = listOf<AbstractCommonFragment<*>>()

    grid {
        maxSize .. gapWidth { 12.dp }
        colTemplate(120.dp, 1.fr, 360.dp)

        palette()

        box {
            maxSize .. canvasBorder .. canvasBackground

            onClick { selection = hits(it.fragment, it.x, it.y) }

            box {
                maxSize .. noHit

                box { frame(100.dp, 100.dp, 12.dp, 12.dp) .. blueBackground .. padding { 1.dp } }
                box { frame(100.dp, 150.dp, 23.dp, 23.dp) .. greenBackground .. padding { 2.dp } }
                box { frame(200.dp, 150.dp, 34.dp, 34.dp) .. redBackground .. padding { 3.dp } }
            }

            overlay(selection)
        }

        instructions(selection)
    }
}
