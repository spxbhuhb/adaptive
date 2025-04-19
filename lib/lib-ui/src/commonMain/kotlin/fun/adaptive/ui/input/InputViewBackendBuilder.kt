package `fun`.adaptive.ui.input

import `fun`.adaptive.ui.instruction.layout.PopupAlign
import `fun`.adaptive.ui.label.LabelTheme

abstract class InputViewBackendBuilder<VT, BT : InputViewBackend<VT,BT>>(
    var inputValue: VT?
) {

    var isNullable : Boolean? = null
    var invalid: Boolean? = null
    var disabled: Boolean? = null
    var secret: Boolean = false
    var labelAlignment: PopupAlign? = null
    var label: String? = null
    var onChange: ((VT?) -> Unit)? = null
    var validateFun: ((VT?) -> Boolean)? = null
    var inputTheme: InputTheme? = null
    var labelTheme: LabelTheme? = null

    abstract fun toBackend() : BT

    open fun setup(backend: BT) {
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