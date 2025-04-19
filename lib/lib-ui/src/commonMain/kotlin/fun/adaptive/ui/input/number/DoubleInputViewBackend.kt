package `fun`.adaptive.ui.input.number

import `fun`.adaptive.ui.input.InputViewBackend
import kotlin.properties.Delegates.observable

class DoubleInputViewBackend(
    value: Double? = null,
    label: String? = null,
    isSecret: Boolean = false
) : InputViewBackend<Double, DoubleInputViewBackend>(
    value, label, isSecret
) {

    var decimals by observable(2, ::notify)

}