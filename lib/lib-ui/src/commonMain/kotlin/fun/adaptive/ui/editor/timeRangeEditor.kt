package `fun`.adaptive.ui.editor

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.binding.PropertySelector
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.lib.util.datetime.TimeRange
import `fun`.adaptive.ui.form.FormViewBackend.Companion.viewBackendFor
import `fun`.adaptive.ui.input.timerange.TimeRangeInputViewBackend
import `fun`.adaptive.ui.input.timerange.timeRangeInput

@Adaptive
fun timeRangeEditor(
    binding: AdaptiveStateVariableBinding<TimeRange>? = null,
    @Suppress("unused")
    @PropertySelector
    selector: () -> TimeRange?,
) : AdaptiveFragment {
    timeRangeInput(
        fragment().viewBackendFor(binding) { value, label, isSecret ->
            TimeRangeInputViewBackend(value, label, isSecret)
        }
    ) .. instructions()
    return fragment()
}