package `fun`.adaptive.ui.input.integer

import `fun`.adaptive.ui.input.InputViewBackend
import kotlin.properties.Delegates.observable

class IntInputViewBackend(
    value: Int? = null,
    label: String? = null,
    isSecret: Boolean = false
) : InputViewBackend<Int, IntInputViewBackend>(
    value, label, isSecret
)