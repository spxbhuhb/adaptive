package `fun`.adaptive.grove.designer.instruction

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.grove.designer.fragment.dPixelInput
import `fun`.adaptive.grove.designer.instruction.styles.*
import `fun`.adaptive.ui.common.fragment.column
import `fun`.adaptive.ui.common.fragment.flowBox
import `fun`.adaptive.ui.common.fragment.row
import `fun`.adaptive.ui.common.fragment.text
import `fun`.adaptive.ui.common.instruction.*

@Adaptive
fun surrounding(label: String, surrounding: Surrounding) {
    column {
        maxWidth
        row(*instructionTitle) { text(label, *instructionLabel) }
        flowBox {
            maxWidth
            gap(10.dp)
            paddingLeft { 8.dp }

            row(*valueField) {
                text("top", *valueLabel)
                dPixelInput(*dpEditorInstructions) { surrounding.top }
            }
            row(*valueField) {
                text("right", *valueLabel)
                dPixelInput(*dpEditorInstructions) { surrounding.right }
            }
            row(*valueField) {
                text("bottom", *valueLabel)
                dPixelInput(*dpEditorInstructions) { surrounding.bottom }
            }
            row(*valueField) {
                text("left", *valueLabel)
                dPixelInput(*dpEditorInstructions) { surrounding.left }
            }
        }
    }
}