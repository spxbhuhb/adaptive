package `fun`.adaptive.cookbook.ui.select

import `fun`.adaptive.cookbook.Res
import `fun`.adaptive.cookbook.mail
import `fun`.adaptive.cookbook.shared.cornerRadius8
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
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.api.zIndex
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors

@Adaptive
fun selectRecipe() {
    val items = listOf("one", "two", "three")
    var selected = items[0]

    column {
        padding { 16.dp }
        text("Selected: $selected")
        select(selected, items) { selected = it }
    }
}

@Adaptive
fun <T> select(
    value: T,
    items: List<T>,
    vararg instructions: AdaptiveInstruction,
    onChange : (T) -> Unit,
): AdaptiveFragment {
    var open = false

    box {
        height { 44.dp } // keep it 44 so we won't re-layout it even if the select is open

        if (open) {
            box {
                width { 400.dp } .. height { 400.dp } .. zIndex(2) .. backgrounds.surface

                selectTop(value, open) .. onClick { open = false }

                column {
                    maxSize .. position(44.dp, 0.dp)
                    for (index in items.indices) {
                        selectItem(index, items[index], items.size) .. onClick {
                            onChange(items[index])
                            open = false
                        }
                    }
                }
            }
        } else {
            box {
                width { 400.dp } .. borders.outline .. cornerRadius8 .. height { 44.dp }
                paddingLeft { 16.dp } .. paddingRight(8.dp)

                selectTop(value, open) .. onClick { open = true }
            }
        }
    }

    return fragment()
}

@Adaptive
private fun <T> selectTop(selected: T, open: Boolean, vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    val decorations = if (open) {
        instructionsOf(
            paddingLeft { 16.dp }, paddingRight(8.dp), border(colors.outline), cornerTopRadius(8.dp)
        )
    } else {
        instructionsOf()
    }

    grid(*instructions) {
        colTemplate(1.fr, 24.dp) .. height { 44.dp }
        alignItems.startCenter
        decorations

        text(selected)
        icon(Res.drawable.mail)
    }

    return fragment()
}

@Adaptive
private fun <T> selectItem(index: Int, a: T, size: Int, vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    val hover = hover()

    var styles = instructionsOf(border(colors.outline, top = 0.dp, bottom = 0.dp))

    if (index == size - 1) {
        styles += border(colors.outline, top = 0.dp)
        styles += cornerBottomRadius(8.dp)
    }

    box(*instructions) {
        maxWidth .. height { 44.dp } .. padding(16.dp)
        colors(false, hover) .. styles

        text(a) .. alignSelf.startCenter
    }

    return fragment()
}