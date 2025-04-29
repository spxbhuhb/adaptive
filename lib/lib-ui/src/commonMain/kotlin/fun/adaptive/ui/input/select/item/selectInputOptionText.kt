package `fun`.adaptive.ui.input.select.item

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.api.hover
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.input.select.AbstractSelectInputViewBackend

@Adaptive
fun <OT> selectInputOptionText(
    item: AbstractSelectInputViewBackend<*,*,OT>.SelectItem
) {
    val hover = hover()
    val observed = valueFrom { item }

    row {
        observed.optionContainerInstructions(hover)
        onClick { observed.toggle() }

        text(observed) .. observed.optionTextInstructions()
    }
}