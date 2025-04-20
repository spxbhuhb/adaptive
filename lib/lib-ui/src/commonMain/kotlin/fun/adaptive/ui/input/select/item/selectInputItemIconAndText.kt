package `fun`.adaptive.ui.input.select.item

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.checkbox.CheckboxTheme
import `fun`.adaptive.ui.checkbox.checkbox
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.input.select.AbstractSelectInputViewBackend
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun <OT> selectInputItemIconAndText(
    item: AbstractSelectInputViewBackend<*,*,OT>.SelectItem
) {
    val observed = valueFrom { item }

    row {
        observed.optionContainerInstructions()
        onClick { observed.toggle() }

        icon(item.icon()) .. observed.optionIconInstructions()
        text(observed) .. observed.optionTextInstructions()
    }
}