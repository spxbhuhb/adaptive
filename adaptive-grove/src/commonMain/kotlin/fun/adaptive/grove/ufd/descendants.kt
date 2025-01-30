package `fun`.adaptive.grove.ufd

import adaptive_grove.generated.resources.components
import `fun`.adaptive.auto.api.autoCollection
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.grove.sheet.model.SheetViewModel
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.borderRight
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.theme.colors

@Adaptive
fun descendants(viewModel: SheetViewModel) {
    val items = autoCollection(viewModel.fragments) ?: emptyList()

    column {
        maxSize .. borderRight(colors.outline)

        areaTitle(Strings.components)

        for (item in items) {
            text(item.key)
        }
    }
}