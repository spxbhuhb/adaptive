package `fun`.adaptive.ui.input.select.item

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.input.select.AbstractSelectInputViewBackend

@Adaptive
fun <OT> selectInputOptionIconAndText(
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