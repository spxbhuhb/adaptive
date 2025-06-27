package `fun`.adaptive.grove.ufd

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.foundation.value.valueFromOrNull
import `fun`.adaptive.grove.generated.resources.selectForInstructions
import `fun`.adaptive.grove.sheet.model.SheetSelection
import `fun`.adaptive.grove.ufd.app.GroveUdfModuleMpw.Companion.udfModule
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.flowText
import `fun`.adaptive.ui.mpw.backends.UnitPaneViewBackend
import `fun`.adaptive.ui.mpw.fragments.noContent
import `fun`.adaptive.ui.mpw.fragments.toolPane
import `fun`.adaptive.ui.viewbackend.viewBackend

@Adaptive
fun ufdInstructions(): AdaptiveFragment {

    val viewBackend = viewBackend(UnitPaneViewBackend::class)

    val sheetBackend = observe { fragment().udfModule.focusedSheet }
    val selection = valueFromOrNull { sheetBackend?.selectionStore } ?: SheetSelection(emptyList())

    val isEmpty = selection.items.isEmpty()

    toolPane(viewBackend) {
        if (isEmpty) {
            noContent(Strings.selectForInstructions)
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