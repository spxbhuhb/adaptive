package `fun`.adaptive.grove.ufd

import adaptive_grove.generated.resources.components
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.value.adaptiveValue
import `fun`.adaptive.grove.sheet.model.SheetItem
import `fun`.adaptive.grove.sheet.model.SheetSelection
import `fun`.adaptive.grove.sheet.model.SheetSelection.Companion.emptySelection
import `fun`.adaptive.grove.sheet.model.SheetViewModel
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textSmall

@Adaptive
fun descendants(viewModel: SheetViewModel) {
    val selection = adaptiveValue { viewModel.selectionStore } ?: emptySelection

    grid {
        maxSize .. borderRight(colors.outline)
        rowTemplate(33.dp, 1.fr)

        areaTitle(Strings.components)

        column {
            maxSize .. scroll .. padding { 4.dp }

            for (item in viewModel.items) {
                if (! item.removed) {
                    itemRow(item, selection, viewModel)
                }
            }
        }
    }
}

@Adaptive
fun itemRow(item: SheetItem, selection: SheetSelection, viewModel: SheetViewModel) {
    val hover = hover()
    val selected = item in selection.items
    val textColor = textStyles(selected, hover)

    row {
        rowStyles(selected, hover)

        onClick { viewModel.select(item, EventModifier.SHIFT in it.modifiers) }

        text(item.model.key) .. noSelect .. textColor
        text(item.index) .. textSmall .. noSelect .. textColor
    }
}
