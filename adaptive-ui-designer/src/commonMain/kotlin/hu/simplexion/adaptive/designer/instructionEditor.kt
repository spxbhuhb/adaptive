package hu.simplexion.adaptive.designer

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.instruction.instructionsOf
import hu.simplexion.adaptive.ui.common.fragment.*
import hu.simplexion.adaptive.ui.common.instruction.*

class InstructionEditorData(
    val padding: Padding = Padding.NONE
)

@Adaptive
fun instructionEditor() {
    grid {
        maxSize
        colTemplate(1.fr, 360.dp)

        box {
            maxSize
        }

        column {
            maxSize
            gapHeight { 8.dp }
            surroundingEditor("padding")
            surroundingEditor("margin")
        }
    }
}

@Adaptive
fun surroundingEditor(label: String) {
    val p = Padding.NONE

    column {
        maxWidth
        row(*instructionTitle) { text(label, *instructionLabel) }
        flowBox {
            maxWidth
            gap(10.dp)
            paddingLeft { 8.dp }

            row(*valueField) {
                text("top", *valueLabel)
                dPixelInput(*dpEditorInstructions) { p.top }
            }
            row(*valueField) {
                text("right", *valueLabel)
                dPixelInput(*dpEditorInstructions) { p.right }
            }
            row(*valueField) {
                text("bottom", *valueLabel)
                dPixelInput(*dpEditorInstructions) { p.bottom }
            }
            row(*valueField) {
                text("left", *valueLabel)
                dPixelInput(*dpEditorInstructions) { p.left }
            }
        }
    }
}

val instructionTitle = instructionsOf(
    backgroundColor(Color(0xe0e0e0u)), maxWidth, marginBottom { 8.dp }, paddingLeft { 8.dp }, cornerRadius(2.dp)
)

val instructionLabel = instructionsOf(
    fontSize(14.sp), smallCaps
)

val valueField = instructionsOf(
    AlignItems.startCenter,
    gapWidth(8.dp)
)

val valueLabel = instructionsOf(
    fontSize(12.sp), smallCaps
)

val dpEditorInstructions = instructionsOf(
    textColor(0x000000u),
    cornerRadius(2.dp),
    border(Color(0xa0a0a0u), 1.dp),
    height(24.dp),
    fontSize(12.sp),
    padding(left = 4.dp, right = 4.dp),
    width(48.dp)
)
