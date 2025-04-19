package `fun`.adaptive.ui.input

import `fun`.adaptive.ui.instruction.layout.PopupAlign
import `fun`.adaptive.ui.label.LabelTheme

open class InputViewBackendBuilder<T>(
    var inputValue: T?
) {

    var isNullable : Boolean? = null
    var invalid: Boolean? = null
    var disabled: Boolean? = null
    var secret: Boolean = false
    var labelAlignment: PopupAlign? = null
    var label: String? = null
    var onChange: ((T?) -> Unit)? = null
    var validateFun: ((T?) -> Boolean)? = null
    var inputTheme: InputTheme? = null
    var labelTheme: LabelTheme? = null

    open fun toBackend() =
        InputViewBackend(inputValue, label, secret).also {
            setup(it)
        }

    fun setup(backend: InputViewBackend<T>) {
        isNullable?.let { backend.isNullable = it }
        disabled?.let { backend.isDisabled = it }
        invalid?.let { backend.isInConstraintError = it }
        labelAlignment?.let { backend.labelAlignment = it }
        onChange?.let { backend.onChange = it }
        validateFun?.let { backend.validateFun = it }
        inputTheme?.let { backend.inputTheme = it }
        labelTheme?.let { backend.labelTheme = it }
    }
}