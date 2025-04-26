package `fun`.adaptive.ui.editor.instruction

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction

@Adat
class EditorLabel(
    val label: String
) : AdaptiveInstruction {
    companion object {
        fun editorLabel(label: String) = EditorLabel(label)
    }
}