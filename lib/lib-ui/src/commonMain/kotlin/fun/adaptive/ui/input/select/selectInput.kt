package `fun`.adaptive.ui.input.select

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.checkbox.CheckboxTheme
import `fun`.adaptive.ui.checkbox.checkbox
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.input.decoratedInput
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun <T> selectInput(
    viewBackend: SingleSelectInputViewBackend<T>,
    @Adaptive
    _fixme_option: (option: AbstractSelectInputViewBackend.SelectItem<T,T>) -> Unit
) : AdaptiveFragment {
    val focus = focus()
    val observed = valueFrom { viewBackend }

    decoratedInput(focus, observed) {
        column(instructions()) {
            viewBackend.optionListContainerInstructions(focus)

            onKeydown { event -> viewBackend.onKeydown(event) }

            for (item in observed.items) {
                _fixme_option(item)
            }
        }
    }

    return fragment()
}

@Adaptive
fun <T> selectInputItemText(
    item: AbstractSelectInputViewBackend.SelectItem<T,*>
) {
    val observed = valueFrom { item }

    row {
        observed.optionContainerInstructions()
        onClick { observed.toggle() }

        text(observed) .. observed.optionTextInstructions()
    }
}

@Adaptive
fun <T> selectInputItemIconAndText(
    item: AbstractSelectInputViewBackend.SelectItem<T,*>
) {
    val observed = valueFrom { item }

    row {
        observed.optionContainerInstructions()
        onClick { observed.toggle() }

        icon(item.icon()) .. observed.optionIconInstructions()
        text(observed) .. observed.optionTextInstructions()
    }
}

@Adaptive
fun <T> selectInputItemCheckbox(
    item: AbstractSelectInputViewBackend.SelectItem<T,*>
) {
    val observed = valueFrom { item }

    row {
        observed.optionContainerInstructions()
        onClick { observed.toggle() }

        box {
            size(24.dp, 24.dp) .. alignItems.center
            checkbox(observed.isSelected, theme = CheckboxTheme.small) {  }
        }
        text(observed) .. observed.optionTextInstructions()
    }
}