package `fun`.adaptive.ui.input.select.item

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.input.select.AbstractSelectInputViewBackend

@Adaptive
fun <OT> selectInputValueText(
    item: AbstractSelectInputViewBackend<*,*,OT>.SelectItem,
    toText : (OT.() -> String)? = null
) {
    row {
        item.valueContainerInstructions()
        text(toText?.invoke(item.option) ?: item) .. item.valueTextInstructions()
    }
}