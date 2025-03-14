package `fun`.adaptive.ui.input

import `fun`.adaptive.general.SelfObservable
import kotlin.properties.Delegates.observable

class InputState(
    invalid : Boolean = false,
    disabled: Boolean = false
) : SelfObservable<InputState>() {

    var invalid by observable(invalid, ::notify)
    var disabled by observable(disabled, ::notify)

}