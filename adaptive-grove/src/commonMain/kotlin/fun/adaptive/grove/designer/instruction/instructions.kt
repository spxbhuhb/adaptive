package `fun`.adaptive.grove.designer.instruction

import `fun`.adaptive.adat.store.copyStore
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.grove.designer.fragment.dPixelInput
import `fun`.adaptive.grove.designer.instruction.model.InstructionEditorData
import `fun`.adaptive.grove.designer.instruction.model.applyProperties
import `fun`.adaptive.grove.designer.instruction.model.intersect
import `fun`.adaptive.grove.designer.instruction.styles.*
import `fun`.adaptive.grove.designer.utility.Selection
import `fun`.adaptive.ui.common.fragment.column
import `fun`.adaptive.ui.common.fragment.flowBox
import `fun`.adaptive.ui.common.fragment.row
import `fun`.adaptive.ui.common.fragment.text
import `fun`.adaptive.ui.common.instruction.*


@Adaptive
fun instructions(selection: Selection) {

    val data = copyStore({ applyProperties(selection, it) }) {
        if (selection.isEmpty()) {
            InstructionEditorData()
        } else {
            intersect(selection)
        }
    }

    if (selection.isNotEmpty()) {
        column {
            maxSize
            gapHeight { 8.dp }

            positionAndSize(data.frame)
            surrounding("padding", data.padding)
            surrounding("margin", data.margin)
        }
    }
}

@Adaptive
fun section(label: String, @Adaptive content: () -> Unit) {
    column {
        maxWidth
        row(*instructionTitle) { text(label, *instructionLabel) }
        content()
    }
}

@Adaptive
fun positionAndSize(frame: Frame) {
    section("position and size") {
        flowBox {
            maxWidth
            gap(10.dp)
            paddingLeft { 8.dp }

            row(*valueField) {
                text("Top", *valueLabel)
                dPixelInput(*dpEditorInstructions) { frame.top }
            }
            row(*valueField) {
                text("Left", *valueLabel)
                dPixelInput(*dpEditorInstructions) { frame.left }
            }
            row(*valueField) {
                text("Width", *valueLabel)
                dPixelInput(*dpEditorInstructions) { frame.width }
            }
            row(*valueField) {
                text("Height", *valueLabel)
                dPixelInput(*dpEditorInstructions) { frame.height }
            }
        }
    }
}