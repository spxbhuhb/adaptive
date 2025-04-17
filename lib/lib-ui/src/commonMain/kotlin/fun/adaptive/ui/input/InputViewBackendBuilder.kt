package `fun`.adaptive.ui.input

import `fun`.adaptive.ui.label.LabelTheme

class InputViewBackendBuilder<T>(
    var inputValue: T?
) {

    var invalid: Boolean = false
    var disabled: Boolean = false
    var label: String? = null
    var onChange: ((T?) -> Unit)? = null
    var validateFun: ((T?) -> Boolean)? = null
    var theme: InputTheme = InputTheme.DEFAULT
    var labelTheme: LabelTheme = LabelTheme.DEFAULT

    fun toBackend() = InputViewBackend(
        inputValue,
        invalid,
        disabled,
        label
    ).also {
        it.onChange = onChange
        it.validateFun = validateFun
        it.inputTheme = theme
        it.labelTheme = labelTheme
    }

}