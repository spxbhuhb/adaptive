package `fun`.adaptive.ui.input

import `fun`.adaptive.ui.instruction.layout.PopupAlign
import `fun`.adaptive.ui.label.LabelTheme

abstract class InputViewBackendBuilder<VT, BT : InputViewBackend<VT,BT>>(
    var inputValue: VT? = null
) {

    var isNullable : Boolean? = null
    var invalid: Boolean? = null
    var disabled: Boolean? = null
    var secret: Boolean = false
    var labelAlignment: PopupAlign? = null
    var label: String? = null
    var help: String? = null
    var placeholder: String? = null
    var onChange: ((VT?) -> Unit)? = null
    var validateFun: ((VT?) -> Boolean)? = null
    var inputTheme: InputTheme? = null
    var labelTheme: LabelTheme? = null

    abstract fun toBackend() : BT

    open fun setup(backend: BT) {
        isNullable?.let { backend.isNullable = it }
        disabled?.let { backend.isInputDisabled = it }
        invalid?.let { backend.isInConstraintError = it }
        labelAlignment?.let { backend.labelAlignment = it }
        help?.let { backend.help = it }
        placeholder?.let { backend.placeholder = it }
        onChange?.let { backend.onChange = it }
        validateFun?.let { backend.validateFun = it }
        inputTheme?.let { backend.inputTheme = it }
        labelTheme?.let { backend.labelTheme = it }
    }
}