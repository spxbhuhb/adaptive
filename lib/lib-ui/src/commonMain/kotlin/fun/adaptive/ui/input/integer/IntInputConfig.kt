package `fun`.adaptive.ui.input.integer

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction

class IntInputConfig(
    val config : IntInputViewBackendBuilder
) : AdaptiveInstruction {

    override fun applyTo(subject: Any) {
        // checks for value are there to prevent infinite loops
        if (subject !is IntInputViewBackend) return

        with(config) {
            label?.let { if (subject.label != it) subject.label = it }
            disabled?.let { if (subject.isInputDisabled != it) subject.isInputDisabled = it }
            placeholder?.let { if (subject.placeholder != it) subject.placeholder = it }
            radix?.let { if (subject.radix != it) subject.radix = it }
            showRadix10?.let { if (subject.showRadix10 != it) subject.showRadix10 = it }
            unit?.let { if (subject.unit != it) subject.unit = it }
            validateFun?.let { if (subject.validateFun != it) subject.validateFun = it}
        }
    }

    companion object {

        inline fun intInputConfig(
            config : IntInputViewBackendBuilder.() -> Unit
        ) = IntInputConfig(IntInputViewBackendBuilder().apply(config))

        inline fun intEditorConfig(
            config : IntInputViewBackendBuilder.() -> Unit
        ) = IntInputConfig(IntInputViewBackendBuilder().apply(config))

    }
}