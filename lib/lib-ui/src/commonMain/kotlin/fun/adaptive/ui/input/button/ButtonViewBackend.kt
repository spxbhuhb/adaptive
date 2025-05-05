package `fun`.adaptive.ui.input.button

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.ui.input.InputViewBackend
import `fun`.adaptive.ui.instruction.event.OnClick
import `fun`.adaptive.ui.instruction.event.UIEvent
import kotlin.properties.Delegates.observable

class ButtonViewBackend(
    label: String? = null
) : InputViewBackend<Unit, ButtonViewBackend>(
    Unit, label, false
) {

    var isSubmit by observable(false, ::notify)
    var isDanger by observable(false, ::notify)

    var buttonTheme = ButtonTheme.default

    fun outerContainerInstructions(focus: Boolean): AdaptiveInstruction =
        when {
            isDisabled -> {
                buttonTheme.outerContainerBase
            }

            focus -> when {
                isDanger -> buttonTheme.focusDangerOuterContainer
                isSubmit -> buttonTheme.focusSubmitOuterContainer
                else -> buttonTheme.outerContainerBase
            }

            else -> when {
                isDanger -> buttonTheme.dangerOuterContainer
                isSubmit -> buttonTheme.submitOuterContainer
                else -> buttonTheme.outerContainerBase
            }
        }


    fun innerContainerInstructions(focus: Boolean): AdaptiveInstruction =
        when {
            isDisabled -> buttonTheme.disabledInnerContainer

            focus -> when {
                isDanger -> buttonTheme.focusDangerInnerContainer
                isSubmit -> buttonTheme.focusSubmitInnerContainer
                else -> buttonTheme.focusNormalInnerContainer
            }

            else -> when {
                isDanger -> buttonTheme.dangerInnerContainer
                isSubmit -> buttonTheme.submitInnerContainer
                else -> buttonTheme.normalInnerContainer
            }
        }

    fun textThemeInstructions(focus: Boolean): AdaptiveInstruction =
        when {
            isDisabled -> {
                buttonTheme.disabledText
            }

            focus -> when {
                isDanger -> buttonTheme.focusDangerText
                isSubmit -> buttonTheme.focusSubmitText
                else -> buttonTheme.focusNormalText
            }

            else -> when {
                isDanger -> buttonTheme.dangerText
                isSubmit -> buttonTheme.submitText
                else -> buttonTheme.normalText
            }
        }

    fun iconThemeInstructions(focus: Boolean): AdaptiveInstructionGroup =
        when {
            isDisabled -> {
                buttonTheme.disabledIcon
            }

            focus -> when {
                isDanger -> buttonTheme.focusDangerIcon
                isSubmit -> buttonTheme.focusSubmitIcon
                else -> buttonTheme.focusNormalIcon
            }

            else -> when {
                isDanger -> buttonTheme.dangerIcon
                isSubmit -> buttonTheme.submitIcon
                else -> buttonTheme.normalIcon
            }
        }

    fun onKeydown(event: UIEvent) {
        if (event.keyInfo?.key == "Enter") {
            event.preventDefault()
            event.stopPropagation()
            event.fragment.instructions.firstInstanceOfOrNull<OnClick>()?.handler?.invoke(event)
        }
    }
}