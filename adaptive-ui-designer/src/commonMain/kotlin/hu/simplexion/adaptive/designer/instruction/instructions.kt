package hu.simplexion.adaptive.designer.instruction

import hu.simplexion.adaptive.adat.store.copyStore
import hu.simplexion.adaptive.designer.instruction.model.InstructionEditorData
import hu.simplexion.adaptive.designer.instruction.model.intersect
import hu.simplexion.adaptive.designer.utility.Selection
import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.ui.common.fragment.column
import hu.simplexion.adaptive.ui.common.instruction.dp
import hu.simplexion.adaptive.ui.common.instruction.gapHeight
import hu.simplexion.adaptive.ui.common.instruction.maxSize

@Adaptive
fun instructions(selection: Selection) {

    val data = copyStore {
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
            surrounding("padding", data.padding)
            surrounding("margin", data.margin)
        }
    }
}
