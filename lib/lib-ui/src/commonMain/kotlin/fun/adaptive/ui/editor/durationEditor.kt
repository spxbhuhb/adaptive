package `fun`.adaptive.ui.editor

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.binding.PropertySelector
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.form.FormViewBackend.Companion.viewBackendFor
import `fun`.adaptive.ui.input.duration.DurationInputViewBackend
import `fun`.adaptive.ui.input.duration.durationInput
import kotlin.time.Duration

@Adaptive
fun durationEditor(
    binding: AdaptiveStateVariableBinding<Duration>? = null,
    @Suppress("unused")
    @PropertySelector
    selector: () -> Duration?,
) : AdaptiveFragment {
    durationInput(
        fragment().viewBackendFor(binding) { value, label, isSecret ->
            DurationInputViewBackend(value, label, isSecret)
        }
    ) .. instructions()
    return fragment()
}