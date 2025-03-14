package `fun`.adaptive.ui.select

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.builtin.arrow_drop_down
import `fun`.adaptive.ui.builtin.arrow_drop_up
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.input.InputPlaceholder
import `fun`.adaptive.ui.instruction.text.ToText
import `fun`.adaptive.ui.select.SelectTheme
import `fun`.adaptive.ui.theme.textColors
import kotlin.math.min


@Adaptive
fun <T> select(
    value: T?,
    items: List<T>,
    theme: SelectTheme = SelectTheme.DEFAULT,
    vararg instructions: AdaptiveInstruction,
    onChange: (T) -> Unit,
): AdaptiveFragment {
    var focus = focus()

    var open = false
    if (focus == false) open = false // this is not the same as open = focus!

    val toText = fragment().instructions.firstInstanceOfOrNull<ToText<T>>() ?: ToText<T> { it.toString() }
    val openHeight = min(10, items.size) * theme.itemHeight.value

    box {
        theme.outerContainer

        if (open) {
            box(theme.openContainer) {
                height { openHeight.dp }

                selectTop(value, open, focus, toText, theme, instructions()) .. onClick { open = false }

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
            box(theme.closedContainer) {
                selectTop(value, open, focus, toText, theme, instructions()) .. onClick { open = true }
            }
        }
    }

    return fragment()
}

@Adaptive
private fun <T> selectTop(
    selected: T?,
    open: Boolean,
    focus: Boolean,
    toText: ToText<T>,
    theme: SelectTheme,
    vararg instructions: AdaptiveInstruction,
): AdaptiveFragment {

    val placeholder = fragment().instructions.firstInstanceOfOrNull<InputPlaceholder>()
    val textColor = if (selected != null) textColors.onSurface else textColors.onSurfaceVariant

    grid(instructions()) {
        if (open) cornerTopRadius(8.dp) else cornerRadius(8.dp)
        if (focus) theme.focused else theme.enabled

        text(selected?.let { toText.toTextFun(it) } ?: placeholder?.value ?: "") .. textColor
        icon(if (open) Graphics.arrow_drop_up else Graphics.arrow_drop_down)
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

    box(instructions()) {
        theme.itemColors(value == selected, hover) .. theme.item

        text(toText.toTextFun(value)) .. alignSelf.startCenter
    }

    return fragment()
}