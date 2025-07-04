package `fun`.adaptive.ui.input.text

import `fun`.adaptive.ui.input.InputViewBackend
import kotlin.properties.Delegates.observable

class TextInputViewBackend(
    value: String? = null,
    label: String? = null,
    isSecret: Boolean = false
) : InputViewBackend<String, TextInputViewBackend>(
    value, label, isSecret
) {

    var multiline: Boolean = false

    fun areaInstructions(focus: Boolean) =
        when {
            focus -> inputTheme.textAreaFocused
            else -> inputTheme.textAreaNonFocused
        }

}