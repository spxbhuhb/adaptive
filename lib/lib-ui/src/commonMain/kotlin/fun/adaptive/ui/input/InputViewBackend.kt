package `fun`.adaptive.ui.input

import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.ui.instruction.input.Secret
import `fun`.adaptive.ui.label.LabelTheme
import kotlin.properties.Delegates.observable
import kotlin.reflect.KProperty

/**
 * View backend for input fragments.
 *
 * ## Invalid inputs
 *
 * ### Constrain Error
 *
 * The [isInConstraintError] property stores the result of constrain-based validation.
 *
 * There are different ways to validate an input, which one is used depends on the
 * actual situation:
 *
 * - Adat metadata-based validation
 * - manual validation, passed in [validateFun]
 * - form level validation by whatever means
 *
 * ### Conversion Error
 *
 * For many inputs it is possible that the actual value on the UI cannot be converted into
 * the proper type.
 *
 * For example, when you have a numeric input, you want the user to be able to paste invalid
 * values into it. You don't want to refuse those as it would feel very strange for the user.
 * Instead, you let the user paste the invalid value and edit it to make it valid.
 *
 * Let's say the user pastes "v=123" into a numeric field and then wants to delete "v=". This
 * is a perfectly valid expectation.
 *
 * However, those invalid values cannot be stored as [inputValue] is type safe.
 *
 * The solution is the [isInConversionError] property which is true when the value cannot be converted
 * into a type safe instance.
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

    val isInvalid: Boolean
        get() = isInConstraintError || isInConversionError

    fun themeInstructions(focus: Boolean) =
        when {
            isDisabled -> inputTheme.disabled
            isInvalid && isTouched -> if (focus) inputTheme.invalidFocused else inputTheme.invalidNotFocused
            else -> if (focus) inputTheme.focused else inputTheme.enabled
        }.let {
            if (isSecret) println("invalid : $isInvalid $isInConstraintError")
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
        isTouched = true

        if (property.name == ::inputValue.name) {
            val valid = validateFun?.invoke(inputValue) ?: true
            if (! valid && ! isInConstraintError) isInConstraintError = true
            if (valid && isInConstraintError) isInConstraintError = false
        }

        super.notify(property, oldValue, newValue)
    }

    override fun equals(other: Any?): Boolean {
        return false
    }
}