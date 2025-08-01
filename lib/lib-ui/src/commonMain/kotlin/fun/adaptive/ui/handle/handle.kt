package `fun`.adaptive.ui.handle

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.onPointerMove
import `fun`.adaptive.ui.api.onPrimaryDown
import `fun`.adaptive.ui.api.onPrimaryUp
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.layout.Position

@Adaptive
fun handle(
    position: Position,
    moveBy: (topDelta: DPixel, leftDelta: DPixel) -> Unit,
    @Adaptive buildFun: () -> Unit
) {
    var active = false
    var lastX = 0.0
    var lastY = 0.0

    box {
        position

        onPrimaryDown { event ->
            event.acquirePointerCapture()
            lastX = event.clientX
            lastY = event.clientY
            active = true
        }

        onPointerMove { event ->
            if (active) {
                val deltaX = event.clientX - lastX
                val deltaY = event.clientY - lastY
                val adapter = adapter() as AbstractAuiAdapter<*,*>
                moveBy(adapter.toDp(deltaY), adapter.toDp(deltaX))
                lastX = event.clientX
                lastY = event.clientY
            }
        }

        onPrimaryUp { event ->
            event.releasePointerCapture()
            active = false
        }

        buildFun()
    }
}