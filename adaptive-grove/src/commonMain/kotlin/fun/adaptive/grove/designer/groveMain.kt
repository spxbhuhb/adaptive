package `fun`.adaptive.grove.designer

import `fun`.adaptive.adat.api.update
import `fun`.adaptive.adat.store.copyStore
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.grove.designer.instruction.instructions
import `fun`.adaptive.grove.designer.overlay.overlay
import `fun`.adaptive.grove.designer.palette.palette
import `fun`.adaptive.grove.designer.utility.emptySelection2
import `fun`.adaptive.grove.designer.utility.selectionOf
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.instruction.layout.Position

val blueBackground = backgroundColor(0x0000ff)
val greenBackground = backgroundColor(0x00ff00)
val redBackground = backgroundColor(0xff0000)
val orangeBorder = border(color(0xFFA500), 1.dp)

val canvasBackground = backgroundColor(0xf8f8f8)
val canvasBorder = border(color(0xd0d0d0), 1.dp)

@Adaptive
fun groveMain() {
    val selection = copyStore { emptySelection2() }
    val target = copyStore { emptySelection2() }
    var lastPosition: Position? = null

    grid {
        maxSize .. gapWidth { 12.dp }
        colTemplate(120.dp, 1.fr, 360.dp)

        palette()

        box {
            maxSize .. canvasBorder .. canvasBackground .. noSelect

            // FIXME convert screen x and y to dp
            onPrimaryDown { event ->
                lastPosition = event.position
                selection.update(selectionOf(event))
                target.update(emptySelection2())
            }

            onMove { event ->
                if (lastPosition == null) return@onMove
                val newPosition = event.position
                selection.move(lastPosition !!, newPosition)
                lastPosition = newPosition
                target.update(selectionOf(event))
            }

            onPrimaryUp {
                lastPosition = null
                selection.place(target)
                target.update(emptySelection2())
            }

            box {
                maxSize

                box { frame(100.dp, 100.dp, 12.dp, 12.dp) .. blueBackground .. padding { 1.dp } }
                box { frame(100.dp, 150.dp, 23.dp, 23.dp) .. greenBackground .. padding { 2.dp } }
                box { frame(200.dp, 150.dp, 34.dp, 34.dp) .. redBackground .. padding { 3.dp } }

                row { frame(10.dp, 10.dp, 20.dp, 20.dp) .. orangeBorder }
            }

            overlay(selection, target)
        }

        instructions(selection)
    }
}
