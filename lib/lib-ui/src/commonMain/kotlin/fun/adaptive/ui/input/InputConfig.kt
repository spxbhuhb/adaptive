package `fun`.adaptive.ui.input

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction

@Adat
class InputConfig(
    val label: String?,
    val disabled: Boolean?,
    val placeholder: String?
) : AdaptiveInstruction {

    override fun applyTo(subject: Any) {
        // checks for value are there to prevent infinite loops
        if (subject is InputViewBackend<*, *>) {
            label?.let { if (subject.label != it) subject.label = it }
            disabled?.let { if (subject.isInputDisabled != it) subject.isInputDisabled = it }
            placeholder?.let { if (subject.placeholder != it) subject.placeholder = it }
        }
    }

    companion object {
        fun inputConfig(
            label: String? = null,
            disabled: Boolean? = null,
            placeholder: String? = null
        ) = InputConfig(label, disabled, placeholder)
    }
}