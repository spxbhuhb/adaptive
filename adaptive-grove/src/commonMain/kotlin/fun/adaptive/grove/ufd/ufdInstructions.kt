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
import `fun`.adaptive.ui.workspace.wsToolPane

@Adaptive
fun ufdInstructions() : AdaptiveFragment {

    val context = fragment().wsContext<SheetViewContext>()
    val controller = valueFrom { context.focusedView }
    val selection = valueFromOrNull { controller?.selectionStore }

    wsToolPane(context.pane(ufdInstructionsPaneKey)) {
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