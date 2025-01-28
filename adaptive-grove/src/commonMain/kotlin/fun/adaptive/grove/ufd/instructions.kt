package `fun`.adaptive.grove.ufd

import adaptive_grove.generated.resources.instructions
import `fun`.adaptive.auto.api.autoCollection
import `fun`.adaptive.auto.api.autoItem
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.grove.sheet.SheetViewModel
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.borderLeft
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.theme.colors

@Adaptive
fun instructions(viewModel: SheetViewModel) {
    val allDescendants = autoCollection(viewModel.fragments) ?: emptyList()
    val selection = autoItem(viewModel.selection) ?: SheetViewModel.emptySelection
    val items = selection.selected(allDescendants)

    column {
        maxSize .. borderLeft(colors.outline)

        areaTitle(Strings.instructions)

        for (item in items) {
            text(item.key)
        }
    }
}