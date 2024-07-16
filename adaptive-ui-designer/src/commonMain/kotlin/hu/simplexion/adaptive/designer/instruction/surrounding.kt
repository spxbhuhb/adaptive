package hu.simplexion.adaptive.designer.instruction

import hu.simplexion.adaptive.designer.fragment.dPixelInput
import hu.simplexion.adaptive.designer.instruction.styles.*
import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.ui.common.fragment.column
import hu.simplexion.adaptive.ui.common.fragment.flowBox
import hu.simplexion.adaptive.ui.common.fragment.row
import hu.simplexion.adaptive.ui.common.fragment.text
import hu.simplexion.adaptive.ui.common.instruction.*

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