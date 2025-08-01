package `fun`.adaptive.ui.handle

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.onPointerEnter
import `fun`.adaptive.ui.api.onPointerLeave
import `fun`.adaptive.ui.api.onPointerMove
import `fun`.adaptive.ui.api.onPrimaryDown
import `fun`.adaptive.ui.api.onPrimaryUp
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.ui.snackbar.infoNotification

@Adaptive
fun handle(
    position: Position,
    moveFun: (toPosition : Position) -> Unit,
    @Adaptive buildFun: () -> Unit
) {
    var active = false
    var startPosition = Position.NaP
    var startX = 0.0
    var startY = 0.0

    box {
        position

        onPrimaryDown { event ->
            event.acquirePointerCapture()
            startPosition = position
            startX = event.clientX
            startY = event.clientY
            active = true
            event.preventDefault()
        }

        onPointerMove { event ->
            if (active) {
                val adapter = adapter() as AbstractAuiAdapter<*,*>

                val newPosition = startPosition.plus(
                    adapter.toDp(event.clientY - startY),
                    adapter.toDp(event.clientX - startX)
                )

                moveFun(newPosition)
                event.preventDefault()
            }
        }

        onPrimaryUp { event ->
            event.releasePointerCapture()
            active = false
            event.preventDefault()
        }

        buildFun()
    }
}