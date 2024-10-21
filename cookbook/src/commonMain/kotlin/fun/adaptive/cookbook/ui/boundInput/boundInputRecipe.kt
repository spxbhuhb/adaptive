package `fun`.adaptive.cookbook.ui.boundInput

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.store.copyStore
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.binding.PropertySelector
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.boundInput
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.editor.theme.editorTheme
import `fun`.adaptive.ui.instruction.dp
import kotlinx.datetime.LocalTime

@Adaptive
fun boundInputRecipe() {
    val data = copyStore { EditData(LocalTime(12, 0)) }
    column {
        text("stuff: ${data.time}")
        timeInput { data.time } .. editorTheme.enabled .. width { 80.dp }
    }
}

@Adaptive
private fun timeInput(
    vararg instructions: AdaptiveInstruction,
    binding: AdaptiveStateVariableBinding<LocalTime>? = null,
    @PropertySelector
    selector: () -> LocalTime
): AdaptiveFragment {
    checkNotNull(binding) { "missing binding in timeInput" }
    boundInput<LocalTime>(
        *instructions,
        binding = binding,
        toString = { it.toString() },
        fromString = { LocalTime.parse(it) },
        invalid = {  }
    )
    return fragment()
}

@Adat
private class EditData(
    val time: LocalTime
)