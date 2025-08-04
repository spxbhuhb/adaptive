package `fun`.adaptive.ui.container

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.generated.resources.add
import `fun`.adaptive.ui.generated.resources.content_copy
import `fun`.adaptive.ui.generated.resources.duplicate
import `fun`.adaptive.ui.generated.resources.remove
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.denseIconTheme
import `fun`.adaptive.ui.input.select.item.selectInputOptionCustom
import `fun`.adaptive.ui.input.select.selectInputBackend
import `fun`.adaptive.ui.input.select.selectInputList
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors

@Adaptive
fun <T> editableList(
    items: List<T>,
    itemRenderFun: @Adaptive (T) -> Unit,
): AdaptiveFragment {

    val backend = selectInputBackend {
        options = items
    }

    column {
        fillStrategy.constrain .. maxWidth .. instructions()

        row {
            backgrounds.surfaceVariant .. borderBottom(colors.lightOutline, 1.dp) .. maxWidth .. cornerTopRadius(4.dp)
            actionIcon(Graphics.add, Strings.add, theme = denseIconTheme) { /* viewBackend.addTemplate() */ }
            actionIcon(Graphics.remove, Strings.remove, theme = denseIconTheme) { /* viewBackend.removeTemplate() */ }
            actionIcon(Graphics.content_copy, Strings.duplicate, theme = denseIconTheme) { /* viewBackend.duplicateTemplate() */ } .. alignSelf.end
        }

        column {
            maxSize .. verticalScroll .. padding { 8.dp } .. backgrounds.surface .. cornerBottomRadius(4.dp)
            selectInputList(backend, { selectInputOptionCustom(it, itemRenderFun) })
        }
    }

    return fragment()
}