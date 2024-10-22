package `fun`.adaptive.ui.editor

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.binding.PropertySelector
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.boundInput
import `fun`.adaptive.ui.api.focus
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.editor.theme.editorTheme
import `fun`.adaptive.ui.instruction.input.MaxLength
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.wireformat.signature.DatetimeSignatures
import `fun`.adaptive.wireformat.signature.KotlinSignatures
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

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
    var invalidInput = binding.isInError()

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
                validityFun = { binding.setProblem(!it) }
            )
        }

        KotlinSignatures.LONG -> {
            boundInput<Long>(
                *styles, *instructions,
                binding = binding as AdaptiveStateVariableBinding<Long>,
                toString = { it.toString() },
                fromString = { it.toLong() },
                validityFun = { binding.setProblem(!it) }
            )
        }

        KotlinSignatures.FLOAT -> {
            boundInput<Float>(
                *styles, *instructions,
                binding = binding as AdaptiveStateVariableBinding<Float>,
                toString = { it.toString() },
                fromString = { it.toFloat() },
                validityFun = { binding.setProblem(!it) }
            )
        }

        KotlinSignatures.DOUBLE -> {
            boundInput<Double>(
                *styles, *instructions,
                binding = binding as AdaptiveStateVariableBinding<Double>,
                toString = { it.toString() },
                fromString = { it.toDouble() },
                validityFun = { binding.setProblem(!it) }
            )
        }

        KotlinSignatures.STRING -> {
            boundInput<String>(
                *styles, *instructions,
                binding = binding as AdaptiveStateVariableBinding<String>,
                toString = { it },
                fromString = { it },
                validityFun = { binding.setProblem(!it) }
            )
        }

        DatetimeSignatures.LOCAL_TIME -> {
            boundInput<LocalTime>(
                *styles, *instructions, width { editorTheme.timeWidth }, MaxLength(5),
                binding = binding as AdaptiveStateVariableBinding<LocalTime>,
                toString = { it.toString().take(5) },
                fromString = { LocalTime.parse(it) },
                validityFun = { binding.setProblem(!it) }
            )
        }

        DatetimeSignatures.LOCAL_DATE -> {
            boundInput<LocalDate>(
                *styles, *instructions, width { editorTheme.dateWidth }, MaxLength(10),
                binding = binding as AdaptiveStateVariableBinding<LocalDate>,
                toString = { it.toString() },
                fromString = { LocalDate.parse(it) },
                validityFun = { binding.setProblem(!it) }
            )
        }

        else -> {
            text("! no editor for type ${binding.boundType} !") .. textColors.onSurfaceAngry
        }

    }

    return fragment()
}