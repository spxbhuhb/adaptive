package `fun`.adaptive.ui.input

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.unsupported
import `fun`.adaptive.general.Observable
import `fun`.adaptive.general.ObservableListener
import `fun`.adaptive.ui.api.alignSelf
import `fun`.adaptive.ui.form.FormViewBackend
import `fun`.adaptive.ui.instruction.input.Secret
import `fun`.adaptive.ui.instruction.layout.OuterAlignment
import `fun`.adaptive.ui.instruction.layout.PopupAlign
import `fun`.adaptive.ui.label.LabelTheme
import `fun`.adaptive.utility.uppercaseFirstChar
import kotlin.properties.Delegates.observable
import kotlin.reflect.KProperty

/**
 * View backend for input fragments.
 *
 * @property  inputValue  The current, type safe value of the input. This might not be what the user
 *                        sees in cases where the input can hold invalid values. See the explanation above.
 *
 * @property  isTouched     When true, at least one of the observable properties has been changed.
 *                        This lets the theme decide on user feedback as you typically do not want
 *                        mandatory or constrained fields to give feedback before the user touches
 *                        them (or tries to submit the value).
 *
 *  @property  isInputDisabled  True when this specific input is disabled.
 *
 *  @property  isFormDisabled   True when the whole form is disabled and in result this input should be disabled as well.
 */
@Suppress("EqualsOrHashCode")
abstract class InputViewBackend<VT, BT : InputViewBackend<VT, BT>>(
    value: VT? = null,
    label: String? = null,
    val isSecret: Boolean = false,
) : Observable<BT> {

    override val listeners = mutableListOf<ObservableListener<BT>>()

    @Suppress("UNCHECKED_CAST")
    override var value : BT
        get() = this as BT
        set(_) = unsupported()

    var inputValue by observable(value, ::notify)

    var isInConstraintError by observable(false, ::notify)
    var isInConversionError by observable(false, ::notify)

    var isInputDisabled by observable(false, ::notify)
    var isFormDisabled by observable(false, ::notify)
    var isPopupOpen by observable(false, ::notify)

    var label by observable(label, ::notify)
    open var labelAlignment by observable(PopupAlign.aboveStart, ::notify)

    var isNullable: Boolean = false
    var onChange: ((VT?) -> Unit)? = null
    var validateFun: ((VT?) -> Boolean)? = null
    var isTouched: Boolean = false

    var inputTheme = InputTheme.DEFAULT
    var labelTheme = LabelTheme.DEFAULT

    var formBackend: FormViewBackend? = null
    var path: List<String> = emptyList()

    val isDisabled : Boolean
        get() = isInputDisabled || isFormDisabled

    val isInvalid: Boolean
        get() = isInConstraintError || isInConversionError

    fun containerThemeInstructions(focus: Boolean) =
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
        // force UI update on observable property change
        return false
    }

    enum class LabelPosition {
        Left, Right, Top, Bottom
    }

    class LabelConfiguration(
        val text: String,
        val labelPosition: LabelPosition,
        val instruction: AdaptiveInstruction
    )

    fun labelConfiguration(focused: Boolean): LabelConfiguration {
        val vertical = labelAlignment.vertical
        val horizontal = labelAlignment.horizontal

        val position: LabelPosition = when (horizontal) {
            OuterAlignment.Before -> LabelPosition.Left
            OuterAlignment.After -> LabelPosition.Right
            else -> {
                when (vertical) {
                    OuterAlignment.Above -> LabelPosition.Top
                    OuterAlignment.Below -> LabelPosition.Bottom
                    else -> unsupported()
                }
            }
        }

        val instruction = when (position) {
            LabelPosition.Left -> {
                when (vertical) {
                    OuterAlignment.Start -> alignSelf.top
                    OuterAlignment.Center -> alignSelf.center
                    OuterAlignment.End -> alignSelf.bottom
                    else -> unsupported()
                }
            }

            LabelPosition.Right -> {
                when (vertical) {
                    OuterAlignment.Start -> alignSelf.top
                    OuterAlignment.Center -> alignSelf.center
                    OuterAlignment.End -> alignSelf.bottom
                    else -> unsupported()
                }
            }

            LabelPosition.Top -> {
                when (horizontal) {
                    OuterAlignment.Start -> alignSelf.start
                    OuterAlignment.Center -> alignSelf.center
                    OuterAlignment.End -> alignSelf.end
                    else -> unsupported()
                }
            }

            LabelPosition.Bottom -> {
                when (horizontal) {
                    OuterAlignment.Start -> alignSelf.start
                    OuterAlignment.Center -> alignSelf.center
                    OuterAlignment.End -> alignSelf.end
                    else -> unsupported()
                }
            }
        }

        return LabelConfiguration(label?.uppercaseFirstChar() ?: "", position, labelThemeInstructions(focused) + instruction)
    }
}