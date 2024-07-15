package hu.simplexion.adaptive.designer

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.adat.store.copyStore
import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.instruction.instructionsOf
import hu.simplexion.adaptive.foundation.rangeTo
import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.fragment.*
import hu.simplexion.adaptive.ui.common.fragment.layout.AbstractContainer
import hu.simplexion.adaptive.ui.common.instruction.*

@Adat
class InstructionEditorData(
    val padding: Padding = Padding.NONE,
    val margin: Margin = Margin.NONE
) : AdatClass<InstructionEditorData>

val blueBackground = backgroundColor(0x0000ff)
val greenBackground = backgroundColor(0x00ff00)
val redBackground = backgroundColor(0xff0000)

fun intersectInstructions(hits: List<AbstractCommonFragment<*>>): InstructionEditorData {
    return InstructionEditorData(padding = Padding(10.dp))
}

@Adaptive
fun designerMain() {
    var selection = listOf<AbstractCommonFragment<*>>()

    grid {
        maxSize
        colTemplate(1.fr, 360.dp)

        box {
            maxSize

            onClick { selection = hits(it.fragment as AbstractContainer<*, *>, it.x, it.y) }

            box { frame(100.dp, 100.dp, 20.dp, 20.dp) .. blueBackground }
            box { frame(100.dp, 150.dp, 20.dp, 20.dp) .. greenBackground }
            box { frame(200.dp, 150.dp, 20.dp, 20.dp) .. redBackground }
        }

        instructionPanel(selection)
    }
}

@Adaptive
fun instructionPanel(selection: List<AbstractCommonFragment<*>>) {
    val data = copyStore {
        if (selection.isEmpty()) {
            InstructionEditorData()
        } else {
            intersectInstructions(selection)
        }
    }

    if (selection.isNotEmpty()) {
        column {
            maxSize
            gapHeight { 8.dp }
            surroundingEditor("padding", data.padding)
            surroundingEditor("margin", data.margin)
        }
    }
}

@Adaptive
fun surroundingEditor(label: String, surrounding: Surrounding) {
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
