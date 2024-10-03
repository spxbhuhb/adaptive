package `fun`.adaptive.ui.select

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.alignSelf
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.cornerBottomRadius
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.cornerTopRadius
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.hover
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.paddingLeft
import `fun`.adaptive.ui.api.paddingRight
import `fun`.adaptive.ui.api.position
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.api.zIndex
import `fun`.adaptive.ui.builtin.Res
import `fun`.adaptive.ui.builtin.arrow_drop_down
import `fun`.adaptive.ui.builtin.arrow_drop_up
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.instruction.input.InputPlaceholder
import `fun`.adaptive.ui.instruction.text.ToText
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.utility.firstOrNullIfInstance
import kotlin.collections.plus


@Adaptive
fun <T> select(
    value: T?,
    items: List<T>,
    vararg instructions: AdaptiveInstruction,
    onChange: (T) -> Unit,
): AdaptiveFragment {
    var open = false
    val toText = instructions.firstOrNullIfInstance<ToText<T>>() ?: ToText<T> { it.toString() }

    box {
        height { 44.dp } // keep it 44 so we won't re-layout it even if the select is open

        if (open) {
            box {
                width { 400.dp } .. height { 400.dp } .. zIndex(2) .. backgrounds.surface

                selectTop(value, open, toText, *instructions) .. onClick { open = false }

                column {
                    maxSize .. position(44.dp, 0.dp)
                    for (index in items.indices) {
                        selectItem(index, items[index], items.size, toText) .. onClick {
                            onChange(items[index])
                            open = false
                        }
                    }
                }
            }
        } else {
            box {
                width { 400.dp } .. height { 44.dp }
                selectTop(value, open, toText, *instructions) .. onClick { open = true }
            }
        }
    }

    return fragment()
}

@Adaptive
private fun <T> selectTop(
    selected: T?,
    open: Boolean,
    toText: ToText<T>,
    vararg instructions: AdaptiveInstruction
): AdaptiveFragment {

    val placeholder = instructions.firstOrNullIfInstance<InputPlaceholder>()
    val textColor = if (selected != null) textColors.onSurface else textColors.onSurfaceVariant

    grid(*instructions, if (open) cornerTopRadius(8.dp) else cornerRadius(8.dp)) {
        colTemplate(1.fr, 24.dp) .. height { 44.dp }
        paddingLeft { 16.dp } .. paddingRight(8.dp) .. borders.outline
        alignItems.startCenter

        text(selected?.let { toText.toTextFun(it) } ?: placeholder?.value ?: "") .. textColor
        icon(if (open) Res.drawable.arrow_drop_up else Res.drawable.arrow_drop_down)
    }

    return fragment()
}

@Adaptive
private fun <T> selectItem(
    index: Int,
    value: T,
    size: Int,
    toText: ToText<T>,
    vararg instructions: AdaptiveInstruction
): AdaptiveFragment {
    val hover = hover()

    var styles = instructionsOf(border(colors.outline, top = 0.dp, bottom = 0.dp))

    if (index == size - 1) {
        styles += border(colors.outline, top = 0.dp)
        styles += cornerBottomRadius(8.dp)
    }

    box(*instructions) {
        maxWidth .. height { 44.dp } .. padding(16.dp)
        colors(false, hover) .. styles

        text(toText.toTextFun(value)) .. alignSelf.startCenter
    }

    return fragment()
}