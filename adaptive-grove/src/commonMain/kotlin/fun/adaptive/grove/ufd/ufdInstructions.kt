package `fun`.adaptive.grove.ufd

import adaptive_grove.generated.resources.instructions
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.grove.sheet.SheetViewController.Companion.sheetViewController
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.borderLeft
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.flowText
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.theme.colors

@Adaptive
fun ufdInstructions() : AdaptiveFragment {

    val controller = fragment().sheetViewController()
    val selection = valueFrom { controller.selectionStore }

    column {
        maxSize .. borderLeft(colors.outline)

        areaTitle(Strings.instructions)

        for (item in selection.items) {
            flowText(item.fragment.instructions.toMutableList().joinToString("\n"))
        }
    }

    return fragment()
}