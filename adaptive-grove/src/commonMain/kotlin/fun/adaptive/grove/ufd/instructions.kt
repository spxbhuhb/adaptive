package `fun`.adaptive.grove.ufd

import adaptive_grove.generated.resources.instructions
import `fun`.adaptive.auto.api.autoItem
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.borderLeft
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.theme.colors

@Adaptive
fun instructions(controller: SheetViewController) {
//    val selection = autoItem(viewModel.selection) ?: viewModel.emptySelection
//    val items = selection.fragments(viewModel)
//
    column {
        maxSize .. borderLeft(colors.outline)

        areaTitle(Strings.instructions)
//
//        for (item in items) {
//            text(item.key)
//        }
    }
}