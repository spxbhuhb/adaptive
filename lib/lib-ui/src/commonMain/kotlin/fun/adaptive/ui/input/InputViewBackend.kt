package `fun`.adaptive.ui.input

import `fun`.adaptive.general.SelfObservable
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
 * The [constraintError] property stores the result of constrain-based validation.
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
 * The solution is the [conversionError] property which is true when the value cannot be converted
 * into a type safe instance.
 *
 * @property  inputValue  The current, type safe value of the input. This might not be what the user
 *                        sees in cases where the input can hold invalid values. See explanation above.
 *
 * @property  touched     When true, at least one of the observable properties has been changed.
 *                        This lets the theme decide on user feedback as you typically do not want
 *                        mandatory or constrained fields to give feedback before the user touches
 *                        them (or tries to submit the value).
 */
class InputViewBackend<T>(
    value: T? = null,
    invalid: Boolean = false,
    disabled: Boolean = false,
    label: String? = null
) : SelfObservable<InputViewBackend<T>>() {

    var inputValue by observable(value, ::notify)

    var constraintError by observable(invalid, ::notify)
    var conversionError by observable(false, ::notify)

    var disabled by observable(disabled, ::notify)
    var popupOpen by observable(false, ::notify)

    var label by observable(label, ::notify)

    var onChange: ((T?) -> Unit)? = null
    var validateFun: ((T?) -> Boolean)? = null
    var touched : Boolean = false

    var inputTheme = InputTheme.DEFAULT
    var labelTheme = LabelTheme.DEFAULT

    val isInvalid : Boolean
        get() = constraintError || conversionError

    fun themeInstructions(focus: Boolean) =
        when {
            disabled -> inputTheme.disabled
            isInvalid && touched -> if (focus) inputTheme.invalidFocused else inputTheme.invalidNotFocused
            else -> if (focus) inputTheme.focused else inputTheme.enabled
        }

    fun labelThemeInstructions(focus: Boolean) =
        when {
            disabled -> labelTheme.disabled
            isInvalid && touched -> if (focus || popupOpen) labelTheme.invalidFocused else labelTheme.invalidNotFocused
            else -> if (focus || popupOpen) labelTheme.focused else labelTheme.enabled
        }

    init {
        addListener { onChange?.invoke(it.inputValue) }
    }

    override fun <PT> notify(property: KProperty<*>, oldValue: PT, newValue: PT) {
        touched = true

        super.notify(property, oldValue, newValue)

        if (property.name == ::inputValue.name) {
            val valid = ! (validateFun?.invoke(inputValue) ?: true)
            if (valid != constraintError) constraintError = valid
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is InputViewBackend<*>) return false
        if (other !== this) return false
        return this.hashCode() == other.hashCode()
    }

    override fun hashCode(): Int {
        var result = touched.hashCode()
        result = 31 * result + (onChange?.hashCode() ?: 0)
        result = 31 * result + (validateFun?.hashCode() ?: 0)
        result = 31 * result + inputTheme.hashCode()
        result = 31 * result + labelTheme.hashCode()
        result = 31 * result + constraintError.hashCode()
        result = 31 * result + conversionError.hashCode()
        result = 31 * result + disabled.hashCode()
        result = 31 * result + popupOpen.hashCode()
        result = 31 * result + isInvalid.hashCode()
        result = 31 * result + (inputValue?.hashCode() ?: 0)
        result = 31 * result + (label?.hashCode() ?: 0)
        return result
    }
}