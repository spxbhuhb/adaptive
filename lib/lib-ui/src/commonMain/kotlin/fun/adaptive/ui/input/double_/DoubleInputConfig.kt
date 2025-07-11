package `fun`.adaptive.ui.input.double_

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction

class DoubleInputConfig(
    val config : DoubleInputViewBackendBuilder
) : AdaptiveInstruction {

    override fun applyTo(subject: Any) {
        // checks for value are there to prevent infinite loops
        if (subject !is DoubleInputViewBackend) return

        with(config) {
            label?.let { if (subject.label != it) subject.label = it }
            disabled?.let { if (subject.isInputDisabled != it) subject.isInputDisabled = it }
            placeholder?.let { if (subject.placeholder != it) subject.placeholder = it }
            decimals?.let { if (subject.decimals != it) subject.decimals = it }
            unit?.let { if (subject.unit != it) subject.unit = it }
        }
    }

    companion object {

        inline fun doubleInputConfig(
            config : DoubleInputViewBackendBuilder.() -> Unit
        ) = DoubleInputConfig(DoubleInputViewBackendBuilder().apply(config))

        inline fun doubleEditorConfig(
            config : DoubleInputViewBackendBuilder.() -> Unit
        ) = DoubleInputConfig(DoubleInputViewBackendBuilder().apply(config))

    }
}