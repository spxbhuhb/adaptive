package `fun`.adaptive.ui.input.select.item

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.checkbox.CheckboxTheme
import `fun`.adaptive.ui.checkbox.checkbox
import `fun`.adaptive.ui.input.select.AbstractSelectInputViewBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.support.scroll.scrollIntoView

@Adaptive
fun <OT> selectInputOptionCheckbox(
    item: AbstractSelectInputViewBackend<*,*,OT>.SelectItem,
    toText : (OT.() -> String)? = null
) {
    val hover = hover()
    val observed = observe { item }
    if (hover) item.isHovered = hover

    row {
        observed.optionContainerInstructions(hover)
        onClick { observed.toggle() }

        box {
            size(24.dp, 24.dp) .. alignItems.center
            checkbox(observed.isSelected, theme = CheckboxTheme.small) {  }
        }
        text(toText?.invoke(observed.option) ?: observed) .. observed.optionTextInstructions()
    }

    if (observed.isSelected) {
        afterPatchBatch { scrollIntoView(it, item.scrollAlignment) }
    }
}