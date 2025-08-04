package `fun`.adaptive.ui.input.select

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.generated.resources.keyboard_arrow_down
import `fun`.adaptive.ui.generated.resources.noValueSelected
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.input.decoratedInput
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.SizeBase
import `fun`.adaptive.ui.instruction.layout.SizeStrategy

@Adaptive
fun <INPUT_VALUE_TYPE, ITEM_TYPE, OPTION_TYPE> selectInputDropdown(
    viewBackend: AbstractSelectInputViewBackend<INPUT_VALUE_TYPE, ITEM_TYPE, OPTION_TYPE>,
    option: @Adaptive (option: AbstractSelectInputViewBackend<INPUT_VALUE_TYPE, ITEM_TYPE, OPTION_TYPE>.SelectItem) -> Unit,
    value: @Adaptive (option: AbstractSelectInputViewBackend<INPUT_VALUE_TYPE, ITEM_TYPE, OPTION_TYPE>.SelectItem) -> Unit
): AdaptiveFragment {

    val focus = focus()
    val observed = observe { viewBackend.also { it.withDropdown = true } }

    val theme = viewBackend.selectInputTheme

    decoratedInput(focus, observed) {
        column(instructions()) {
            observed.dropdownSelectedContainerInstructions(focus)

            onKeyDown { event -> observed.onDropdownSelectedKeydown(event) }

            grid {
                theme.valueContainer

                box {
                    if (observed.selectedItems.isNotEmpty()) {
                        value(observed.selectedItems.first())
                    } else {
                        text(Strings.noValueSelected) .. theme.noValue
                    }
                }

                icon(Graphics.keyboard_arrow_down) .. theme.dropdownIcon
            }

            if (! observed.isDisabled) {
                primaryPopup(observed) { hide ->
                    theme.dropdownPopup

                    column {
                        theme.dropdownOptionsContainer .. SizeStrategy(horizontalBase = SizeBase.Content, minWidth = 300.dp)

                        onPointerMove { observed.onPointerMove() }
                        onKeyDown { event -> observed.onListKeydown(event, hide) }

                        for (item in observed.items) {
                            option(item)
                        }
                    }
                }
            }
        }
    }

    return fragment()
}