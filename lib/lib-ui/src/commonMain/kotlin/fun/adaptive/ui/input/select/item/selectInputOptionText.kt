package `fun`.adaptive.ui.input.select.item

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.ui.api.afterPatchBatch
import `fun`.adaptive.ui.api.hover
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.input.select.AbstractSelectInputViewBackend
import `fun`.adaptive.ui.support.scroll.scrollIntoView

@Adaptive
fun <OT> selectInputOptionText(
    item: AbstractSelectInputViewBackend<*,*,OT>.SelectItem
) {
    val hover = hover()
    val observed = observe { item }
    if (hover) item.isHovered = hover

    row {
        observed.optionContainerInstructions(hover)
        onClick { observed.toggle() }

        text(observed) .. observed.optionTextInstructions()
    }

    if (observed.isSelected) {
        afterPatchBatch { scrollIntoView(it, item.scrollAlignment) }
    }
}