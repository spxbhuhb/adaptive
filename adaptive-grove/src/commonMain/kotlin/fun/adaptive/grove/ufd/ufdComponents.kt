package `fun`.adaptive.grove.ufd

import `fun`.adaptive.grove.resources.components
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.foundation.value.valueFromOrNull
import `fun`.adaptive.grove.resources.noComponents
import `fun`.adaptive.grove.sheet.SheetViewContext
import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.grove.sheet.model.SheetItem
import `fun`.adaptive.grove.sheet.model.SheetSelection
import `fun`.adaptive.grove.sheet.operation.SelectByIndex
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textSmall
import `fun`.adaptive.ui.workspace.Workspace.Companion.wsContext
import `fun`.adaptive.ui.workspace.wsToolPane

@Adaptive
fun ufdComponents(): AdaptiveFragment {

    val context = fragment().wsContext<SheetViewContext>()
    val controller = valueFrom { context.focusedView }
    val selection = valueFromOrNull { controller?.selectionStore }

    wsToolPane(context.pane(ufdComponentsPaneKey)) {
        if (controller != null && selection != null) {
            column {
                maxSize .. scroll .. padding { 4.dp }

                for (item in controller.items) {
                    if (! item.removed) {
                        itemRow(item, selection, controller)
                    }
                }
            }
        } else {
            text(Strings.noComponents)
        }
    }

    return fragment()
}

@Adaptive
fun itemRow(item: SheetItem, selection: SheetSelection, controller: SheetViewController) {
    val hover = hover()
    val selected = item in selection.items
    val textColor = textStyles(selected, hover)

    row {
        rowStyles(selected, hover)

        onClick { controller += SelectByIndex(item.index.value, EventModifier.SHIFT in it.modifiers) }

        text(item.name) .. noSelect .. textColor
        text(item.index.value) .. textSmall .. noSelect .. textColor
    }
}
