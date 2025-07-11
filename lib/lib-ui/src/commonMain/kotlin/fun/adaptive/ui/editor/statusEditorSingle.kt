package `fun`.adaptive.ui.editor

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.binding.PropertySelector
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.resource.resolve.resolveString
import `fun`.adaptive.ui.form.FormViewBackend.Companion.viewBackendFor
import `fun`.adaptive.ui.input.status.StatusInputSingleViewBackend
import `fun`.adaptive.ui.input.status.statusInputSingle
import `fun`.adaptive.value.AvStatus

@Adaptive
fun statusEditorSingle(
    status : AvStatus,
    binding: AdaptiveStateVariableBinding<Set<AvStatus>?>? = null,
    @Suppress("unused")
    @PropertySelector
    selector: () -> Set<AvStatus>?,
) : AdaptiveFragment {
    statusInputSingle(
        fragment().viewBackendFor(binding) { value, label, isSecret ->
            StatusInputSingleViewBackend(status, value, fragment().resolveString(status), isSecret)
        }
    ) .. instructions()
    return fragment()
}