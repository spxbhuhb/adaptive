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
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.api.zIndex
import `fun`.adaptive.ui.builtin.Res
import `fun`.adaptive.ui.builtin.arrow_drop_down
import `fun`.adaptive.ui.builtin.arrow_drop_up
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.input.api.inputTheme
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.instruction.input.InputPlaceholder
import `fun`.adaptive.ui.instruction.text.ToText
import `fun`.adaptive.ui.select.theme.SelectTheme
import `fun`.adaptive.ui.select.theme.selectTheme
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.utility.firstOrNullIfInstance
import kotlin.collections.plus
import kotlin.math.max
import kotlin.math.min


@Adaptive
fun <T> select(
    value: T?,
    items: List<T>,
    theme: SelectTheme = selectTheme,
    vararg instructions: AdaptiveInstruction,
    onChange: (T) -> Unit,
): AdaptiveFragment {
    var open = false
    val toText = instructions.firstOrNullIfInstance<ToText<T>>() ?: ToText<T> { it.toString() }
    val openHeight = min(10, items.size) * theme.itemHeight

    box {
        height { theme.itemHeight.dp } // keep it fixed so we won't re-layout it even if the select is open

        if (open) {
            box(*theme.openContainer) {
                height { openHeight.dp }

                selectTop(value, open, toText, theme, *instructions) .. onClick { open = false }

                column {
                    theme.itemsContainer
                    height { openHeight.dp }

                    for (index in items.indices) {
                        selectItem(items[index], value, toText, theme) .. onClick {
                            onChange(items[index])
                            open = false
                        }
                    }
                }
            }
        } else {
            box(*theme.closedContainer) {
                selectTop(value, open, toText, theme, *instructions) .. onClick { open = true }
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
    theme: SelectTheme,
    vararg instructions: AdaptiveInstruction,
): AdaptiveFragment {

    val placeholder = instructions.firstOrNullIfInstance<InputPlaceholder>()
    val textColor = if (selected != null) textColors.onSurface else textColors.onSurfaceVariant

    grid(*instructions, if (open) cornerTopRadius(8.dp) else cornerRadius(8.dp)) {
        theme.active

        text(selected?.let { toText.toTextFun(it) } ?: placeholder?.value ?: "") .. textColor
        icon(if (open) Res.drawable.arrow_drop_up else Res.drawable.arrow_drop_down)
    }

    return fragment()
}

@Adaptive
private fun <T> selectItem(
    value: T,
    selected: T?,
    toText: ToText<T>,
    theme: SelectTheme,
    vararg instructions: AdaptiveInstruction,
): AdaptiveFragment {

    val hover = hover()

    box(*instructions) {
        theme.itemColors(value == selected, hover) .. theme.item

        text(toText.toTextFun(value)) .. alignSelf.startCenter
    }

    return fragment()
}