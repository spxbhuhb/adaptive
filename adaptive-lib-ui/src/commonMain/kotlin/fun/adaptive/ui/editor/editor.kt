package `fun`.adaptive.ui.editor

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.binding.PropertySelector
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.api.boundInput
import `fun`.adaptive.ui.api.focus
import `fun`.adaptive.ui.editor.theme.editorTheme
import `fun`.adaptive.wireformat.signature.KotlinSignatures

@Suppress("UNCHECKED_CAST")
@Adaptive
fun <T> editor(
    vararg instructions: AdaptiveInstruction,
    binding: AdaptiveStateVariableBinding<T>? = null,
    @Suppress("unused")
    @PropertySelector
    selector: () -> T,
): AdaptiveFragment {
    checkNotNull(binding)

    val focus = focus()
    var invalidInput = false

    val styles = when {
        invalidInput && focus -> editorTheme.invalidFocused
        invalidInput -> editorTheme.invalidNotFocused
        focus -> editorTheme.focused
        else -> editorTheme.enabled
    }

    when (binding.boundType) {
        KotlinSignatures.INT -> {
            boundInput<Int>(
                *styles, *instructions,
                binding = binding as AdaptiveStateVariableBinding<Int>,
                toString = { it.toString() },
                fromString = { it.toInt() },
                invalid = { invalidInput = it }
            )
        }

        KotlinSignatures.LONG -> {
            boundInput<Long>(
                *styles, *instructions,
                binding = binding as AdaptiveStateVariableBinding<Long>,
                toString = { it.toString() },
                fromString = { it.toLong() },
                invalid = { invalidInput = it }
            )
        }

        KotlinSignatures.FLOAT -> {
            boundInput<Float>(
                *styles, *instructions,
                binding = binding as AdaptiveStateVariableBinding<Float>,
                toString = { it.toString() },
                fromString = { it.toFloat() },
                invalid = { invalidInput = it }
            )
        }

        KotlinSignatures.DOUBLE -> {
            boundInput<Double>(
                *styles, *instructions,
                binding = binding as AdaptiveStateVariableBinding<Double>,
                toString = { it.toString() },
                fromString = { it.toDouble() },
                invalid = { invalidInput = it }
            )
        }

        KotlinSignatures.STRING -> {
            boundInput<String>(
                *styles, *instructions,
                binding = binding as AdaptiveStateVariableBinding<String>,
                toString = { it },
                fromString = { it },
                invalid = { invalidInput = it }
            )
        }
    }

    return fragment()
}