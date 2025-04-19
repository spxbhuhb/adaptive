package `fun`.adaptive.ui.input.bool

import `fun`.adaptive.ui.input.InputViewBackend
import `fun`.adaptive.ui.instruction.layout.PopupAlign
import kotlin.properties.Delegates.observable

class BooleanInputViewBackend(
    value: Boolean? = null,
    label: String? = null,
    isSecret: Boolean = false
) : InputViewBackend<Boolean, BooleanInputViewBackend>(
    value, label, isSecret
) {

    override var labelAlignment by observable(PopupAlign.beforeCenter, ::notify)

    var booleanInputTheme = BooleanInputTheme.default

}