package hu.simplexion.adaptive.designer.instruction.model

import hu.simplexion.adaptive.designer.utility.Selection
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.ui.common.instruction.*

fun applyProperties(selection: Selection, data: InstructionEditorData) {
    for (item in selection) {
        if (item.instructionIndex < 0) continue
        val instructions = item.instructions.toMutableList()
        applyProperties(instructions, data)
        item.setStateVariable(item.instructionIndex, instructions.toTypedArray())
        item.setDirty(item.instructionIndex, true)
    }
}

private fun applyProperties(instructions: MutableList<AdaptiveInstruction>, data: InstructionEditorData) {
    applyFrame(instructions, data.frame)
}

private fun applyFrame(instructions: MutableList<AdaptiveInstruction>, frame: Frame) {
    val top = frame.top
    val left = frame.left
    val width = frame.width
    val height = frame.height

    instructions.removeAll { it is Frame || it is Position || it is Width || it is Height || it is Size }

    if (top !== DPixel.NaP && left !== DPixel.NaP && width !== DPixel.NaP && height !== DPixel.NaP) {
        instructions += Frame(top, left, width, height)
        return
    }

    if (top !== DPixel.NaP && left !== DPixel.NaP) {
        instructions += Position(top, left)
    }

    if (top !== DPixel.NaP) {
        instructions += Position(top, DPixel.ZERO)
    }

    if (left !== DPixel.NaP) {
        instructions += Position(DPixel.ZERO, left)
    }

    if (width !== DPixel.NaP && height !== DPixel.NaP) {
        instructions += Size(width, height)
        return
    }

    if (width !== DPixel.NaP) {
        instructions += Width(width)
        return
    }

    if (height !== DPixel.NaP) {
        instructions += Height(width)
    }
}
