package `fun`.adaptive.grove.ufd

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.foundation.value.valueFromOrNull
import `fun`.adaptive.grove.resources.instructions
import `fun`.adaptive.grove.resources.selectItemsForInstructions
import `fun`.adaptive.grove.sheet.SheetViewContext
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.workspace.Workspace.Companion.wsContext

@Adaptive
fun ufdInstructions() : AdaptiveFragment {

    val controller = valueFrom { fragment().wsContext<SheetViewContext>().focusedView }
    val selection = valueFromOrNull { controller?.selectionStore }

    column {
        maxSize .. borderLeft(colors.outline)

        areaTitle(Strings.instructions)

        if (selection != null && selection.items.isNotEmpty()) {
            for (item in selection.items) {
                flowText(item.fragment.instructions.toMutableList().joinToString("\n"))
            }
        } else {
            text(Strings.selectItemsForInstructions)
        }
    }

    return fragment()
}