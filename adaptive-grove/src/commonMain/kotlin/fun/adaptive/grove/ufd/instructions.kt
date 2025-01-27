package `fun`.adaptive.grove.ufd

import adaptive_grove.generated.resources.instructions
import `fun`.adaptive.auto.api.autoCollection
import `fun`.adaptive.auto.api.autoItem
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.borderLeft
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.theme.colors

@Adaptive
fun instructions(viewModel: UfdViewModel) {
    val allDescendants = autoCollection(viewModel.fragments) ?: emptyList()
    val selection = autoItem(viewModel.selection) ?: viewModel.emptySelection

    val items = allDescendants.filter { it.uuid in selection.selected.map { it.uuid } }

    column {
        maxSize .. borderLeft(colors.outline)

        areaTitle(Strings.instructions)

        for (item in items) {
            text(item.key)
        }
    }
}