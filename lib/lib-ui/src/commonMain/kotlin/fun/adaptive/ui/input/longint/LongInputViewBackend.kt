package `fun`.adaptive.ui.input.longint

import `fun`.adaptive.ui.input.InputViewBackend

class LongInputViewBackend(
    value: Long? = null,
    label: String? = null,
    isSecret: Boolean = false
) : InputViewBackend<Long, LongInputViewBackend>(
    value, label, isSecret
)