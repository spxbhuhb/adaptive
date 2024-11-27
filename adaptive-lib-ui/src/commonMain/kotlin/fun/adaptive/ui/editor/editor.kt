package `fun`.adaptive.ui.editor

import `fun`.adaptive.adat.AdatClass
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
import `fun`.adaptive.ui.checkbox.api.boundCheckbox
import `fun`.adaptive.ui.editor.theme.editorTheme
import `fun`.adaptive.ui.instruction.input.MaxLength
import `fun`.adaptive.ui.select.select
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.wireformat.WireFormat
import `fun`.adaptive.wireformat.WireFormatRegistry
import `fun`.adaptive.wireformat.builtin.EnumWireFormat
import `fun`.adaptive.wireformat.signature.DatetimeSignatures
import `fun`.adaptive.wireformat.signature.KotlinSignatures
import `fun`.adaptive.wireformat.signature.parseTypeSignature
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

    val editorInfo = editorType(binding)

    when (editorInfo.first) {
        EditorType.Simple -> simpleEditor(binding, *instructions)
        EditorType.Enum -> enumEditor(binding, editorInfo.second, *instructions)
        EditorType.Unsupported -> text("! no editor for type ${binding.boundType} !") .. textColors.onSurfaceAngry
    }

    return fragment()
}

private enum class EditorType {
    Simple,
    Enum,
    Unsupported
}

private fun editorType(binding: AdaptiveStateVariableBinding<*>): Pair<EditorType, WireFormat<*>?> =

    when (binding.boundType.first()) {
        'L' -> parsedType(binding.boundType)
        '[' -> EditorType.Unsupported to null
        '*' -> EditorType.Unsupported to null
        '0' -> EditorType.Unsupported to null
        else -> EditorType.Simple to null
    }

private fun parsedType(boundType: String): Pair<EditorType, WireFormat<*>?> =

    when (boundType) {

        DatetimeSignatures.LOCAL_TIME -> EditorType.Simple to null
        DatetimeSignatures.LOCAL_DATE -> EditorType.Simple to null
        DatetimeSignatures.LOCAL_DATE_TIME -> EditorType.Simple to null

        else -> {
            val type = parseTypeSignature(boundType)
            val wireFormat = WireFormatRegistry[type.name]

            when (wireFormat) {
                null -> EditorType.Unsupported to null
                is EnumWireFormat<*> -> EditorType.Enum to wireFormat
                else -> EditorType.Unsupported to null
            }
        }
    }

@Suppress("UNCHECKED_CAST")
@Adaptive
private fun simpleEditor(
    binding: AdaptiveStateVariableBinding<*>,
    vararg instructions: AdaptiveInstruction,
) {

    val focus = focus()

    // we have to use both to support editors without adat class
    val inError = binding.isInError()
    val isTouched = binding.isTouched()
    var invalidInput = false

    val styles = when {
        ! isTouched -> if (focus) editorTheme.focused else editorTheme.enabled
        (invalidInput || inError) && focus -> editorTheme.invalidFocused
        (invalidInput || inError) -> editorTheme.invalidNotFocused
        focus -> editorTheme.focused
        else -> editorTheme.enabled
    }

    when (binding.boundType.removeSuffix("?")) {

        KotlinSignatures.BOOLEAN -> {
            boundCheckbox(*instructions, binding = binding as AdaptiveStateVariableBinding<Boolean>)
        }

        KotlinSignatures.INT -> {
            boundInput<Int>(
                *styles, *instructions,
                binding = binding as AdaptiveStateVariableBinding<Int>,
                toString = { it.toString() },
                fromString = { it.toInt() },
                validityFun = { invalidInput = binding.setProblem(! it) }
            )
        }

        KotlinSignatures.LONG -> {
            boundInput<Long>(
                *styles, *instructions,
                binding = binding as AdaptiveStateVariableBinding<Long>,
                toString = { it.toString() },
                fromString = { it.toLong() },
                validityFun = { invalidInput = binding.setProblem(! it) }
            )
        }

        KotlinSignatures.FLOAT -> {
            boundInput<Float>(
                *styles, *instructions,
                binding = binding as AdaptiveStateVariableBinding<Float>,
                toString = { it.toString() },
                fromString = { it.toFloat() },
                validityFun = { invalidInput = binding.setProblem(! it) }
            )
        }

        KotlinSignatures.DOUBLE -> {
            boundInput<Double>(
                *styles, *instructions,
                binding = binding as AdaptiveStateVariableBinding<Double>,
                toString = { it.toString() },
                fromString = { it.toDouble() },
                validityFun = { invalidInput = binding.setProblem(! it) }
            )
        }

        KotlinSignatures.STRING -> {
            boundInput<String>(
                *styles, *instructions,
                binding = binding as AdaptiveStateVariableBinding<String>,
                toString = { it },
                fromString = { it },
                validityFun = { invalidInput = binding.setProblem(! it) }
            )
        }

        DatetimeSignatures.LOCAL_TIME -> {
            boundInput<LocalTime>(
                *styles, *instructions, width { editorTheme.timeWidth }, MaxLength(5),
                binding = binding as AdaptiveStateVariableBinding<LocalTime>,
                toString = { it.toString().take(5) },
                fromString = { LocalTime.parse(it) },
                validityFun = { invalidInput = binding.setProblem(! it) }
            )
        }

        DatetimeSignatures.LOCAL_DATE -> {
            boundInput<LocalDate>(
                *styles, *instructions, width { editorTheme.dateWidth }, MaxLength(10),
                binding = binding as AdaptiveStateVariableBinding<LocalDate>,
                toString = { it.toString() },
                fromString = { LocalDate.parse(it) },
                validityFun = { invalidInput = binding.setProblem(! it) }
            )
        }

        DatetimeSignatures.LOCAL_DATE_TIME -> {

        }

        else -> {
            text("! no editor for type ${binding.boundType} !") .. textColors.onSurfaceAngry
        }

    }
}

@Adaptive
@Suppress("UNCHECKED_CAST")
private fun <T> enumEditor(
    binding: AdaptiveStateVariableBinding<T>,
    wireFormat: WireFormat<*>?,
    vararg instructions: AdaptiveInstruction,
) {
    checkNotNull(wireFormat as? EnumWireFormat<T>)

    select(binding.value, wireFormat.entries, instructions = instructions) { binding.setValue(it, true) }

}