package `fun`.adaptive.ui.input.integer

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction

@Adat
class IntInputConfig(
    val label: String?,
    val disabled: Boolean?,
    val placeholder: String?,
    val radix: Int? = null,
    val showRadix10: Boolean? = null
) : AdaptiveInstruction {

    override fun applyTo(subject: Any) {
        // checks for value are there to prevent infinite loops
        if (subject is IntInputViewBackend) {
            label?.let { if (subject.label != it) subject.label = it }
            disabled?.let { if (subject.isInputDisabled != it) subject.isInputDisabled = it }
            placeholder?.let { if (subject.placeholder != it) subject.placeholder = it }
            radix?.let { if (subject.radix != it) subject.radix = it }
            showRadix10?.let { if (subject.showRadix10 != it) subject.showRadix10 = it }
        }
    }

    companion object {
        fun intInputConfig(
            label: String? = null,
            disabled: Boolean? = null,
            placeholder: String? = null,
            radix: Int? = null,
            showRadix10: Boolean? = null
        ) = IntInputConfig(label, disabled, placeholder, radix, showRadix10)
    }
}