package `fun`.adaptive.grove.ufd

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.foundation.value.valueFromOrNull
import `fun`.adaptive.grove.resources.selectForInstructions
import `fun`.adaptive.grove.sheet.SheetViewContext
import `fun`.adaptive.grove.sheet.model.SheetSelection
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.flowText
import `fun`.adaptive.ui.workspace.model.Workspace.Companion.wsContext
import `fun`.adaptive.ui.workspace.wsNoContent
import `fun`.adaptive.ui.workspace.wsToolPane

@Adaptive
fun ufdInstructions() : AdaptiveFragment {

    val context = fragment().wsContext<SheetViewContext>()
    val controller = valueFrom { context.focusedView }
    val selection = valueFromOrNull { controller?.selectionStore }

    val pane = context.pane(ufdInstructionsPaneKey)
    val isEmpty = selection == null || selection.items.isEmpty()

    wsToolPane(pane) {
        if (isEmpty) {
            wsNoContent(Strings.selectForInstructions)
        } else {
            instructionList(selection)
        }
    }

    return fragment()
}

@Adaptive
fun instructionList(selection : SheetSelection) {
    for (item in selection.items) {
        flowText(item.fragment.instructions.toMutableList().joinToString("\n"))
    }
}