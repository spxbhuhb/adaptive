package `fun`.adaptive.grove.ufd

import adaptive_grove.generated.resources.components
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.grove.sheet.SheetViewController.Companion.sheetViewController
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

@Adaptive
fun structure() : AdaptiveFragment {

    val controller = fragment().sheetViewController()
    val selection = valueFrom { controller.selectionStore }

    grid {
        maxSize .. borderRight(colors.outline)
        rowTemplate(udfTheme.headerHeight, 1.fr)

        areaTitle(Strings.components)

        column {
            maxSize .. scroll .. padding { 4.dp }

            for (item in controller.items) {
                if (! item.removed) {
                    itemRow(item, selection, controller)
                }
            }
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
