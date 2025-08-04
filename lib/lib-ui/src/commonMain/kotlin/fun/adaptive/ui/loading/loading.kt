package `fun`.adaptive.ui.loading

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.graphics.canvas.api.canvas
import `fun`.adaptive.graphics.canvas.api.draw
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.canvas.drawLoadingWireFrame
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.colors

@Adaptive
fun <T> loading(
    data: T?,
    _fixme_content: @Adaptive (T) -> Unit
) {
    val borderColor = colors.outline.opaque(0.5)
    val wireColor = colors.outline.opaque(0.5)

    if (data == null) {
        column(instructions()) {
            fillStrategy.constrain .. gap { 16.dp }

            canvas {
                maxWidth .. height { 41.dp } .. border(borderColor) .. cornerRadius { 4.dp }
                draw { drawLoadingWireFrame(wireColor, 13.0) }
            }

            canvas {
                maxWidth .. height { 242.dp } .. border(borderColor) .. cornerRadius { 4.dp }
                draw { drawLoadingWireFrame(wireColor, 48.0) }
            }
        }
    } else {
        _fixme_content(data)
    }
}