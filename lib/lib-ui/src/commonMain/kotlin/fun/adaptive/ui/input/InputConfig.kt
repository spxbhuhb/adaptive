package `fun`.adaptive.ui.input

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction

@Adat
class InputConfig(
    val label: String?,
    val disabled: Boolean?
) : AdaptiveInstruction {

    override fun applyTo(subject: Any) {
        if (subject is InputViewBackend<*, *>) {
            label?.let { subject.label = it }
            disabled?.let { subject.isInputDisabled = it }
        }
    }

    companion object {
        fun inputConfig(
            label: String? = null,
            disabled: Boolean? = null
        ) = InputConfig(label, disabled)
    }
}