package `fun`.adaptive.ui.input

import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.ui.form.FormViewBackend
import `fun`.adaptive.ui.instruction.input.Secret
import `fun`.adaptive.ui.label.LabelTheme
import kotlin.properties.Delegates.observable
import kotlin.reflect.KProperty

/**
 * View backend for input fragments.
 *
 * @property  inputValue  The current, type safe value of the input. This might not be what the user
 *                        sees in cases where the input can hold invalid values. See explanation above.
 *
 * @property  isTouched     When true, at least one of the observable properties has been changed.
 *                        This lets the theme decide on user feedback as you typically do not want
 *                        mandatory or constrained fields to give feedback before the user touches
 *                        them (or tries to submit the value).
 */
class InputViewBackend<T>(
    value: T? = null,
    invalid: Boolean = false,
    disabled: Boolean = false,
    label: String? = null,
    val isSecret: Boolean = false,
) : SelfObservable<InputViewBackend<T>>() {

    var inputValue by observable(value, ::notify)

    var isInConstraintError by observable(invalid, ::notify)
    var isInConversionError by observable(false, ::notify)

    var isDisabled by observable(disabled, ::notify)
    var isPopupOpen by observable(false, ::notify)

    var label by observable(label, ::notify)

    var onChange: ((T?) -> Unit)? = null
    var validateFun: ((T?) -> Boolean)? = null
    var isTouched: Boolean = false

    var inputTheme = InputTheme.DEFAULT
    var labelTheme = LabelTheme.DEFAULT

    var formBackend: FormViewBackend? = null
    var path: List<String> = emptyList()

    val isInvalid: Boolean
        get() = isInConstraintError || isInConversionError

    fun themeInstructions(focus: Boolean) =
        when {
            isDisabled -> inputTheme.disabled
            isInvalid && isTouched -> if (focus) inputTheme.invalidFocused else inputTheme.invalidNotFocused
            else -> if (focus) inputTheme.focused else inputTheme.enabled
        }.let {
            if (isSecret) it + Secret() else it
        }

    fun labelThemeInstructions(focus: Boolean) =
        when {
            isDisabled -> labelTheme.disabled
            isInvalid && isTouched -> if (focus || isPopupOpen) labelTheme.invalidFocused else labelTheme.invalidNotFocused
            else -> if (focus || isPopupOpen) labelTheme.focused else labelTheme.enabled
        }

    init {
        addListener { onChange?.invoke(it.inputValue) }
    }

    override fun <PT> notify(property: KProperty<*>, oldValue: PT, newValue: PT) {

        if (property.name == ::inputValue.name) {
            isTouched = true

            val valid = validateFun?.invoke(inputValue) ?: true

            if (! valid && ! isInConstraintError) isInConstraintError = true
            if (valid && isInConstraintError) isInConstraintError = false

            formBackend?.onInputValueChange(this)
        }

        super.notify(property, oldValue, newValue)
    }

    override fun equals(other: Any?): Boolean {
        return false
    }
}