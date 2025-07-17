package `fun`.adaptive.grove.ufd

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.foundation.value.valueFromOrNull
import `fun`.adaptive.grove.generated.resources.selectForInstructions
import `fun`.adaptive.grove.sheet.model.SheetSelection
import `fun`.adaptive.grove.ufd.app.GroveUdfModuleMpw.Companion.udfModule
import `fun`.adaptive.grove.ufd.model.LayoutConfig
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.editor.doubleEditor
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.instruction.dp
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
        layoutInstructions(item.fragment.instructions)
    }
}

@Adaptive
fun layoutInstructions(itemInstructions: AdaptiveInstructionGroup) {
    val config = LayoutConfig.fromInstructions(itemInstructions, adapter() as AbstractAuiAdapter<*,*>)
    val form = adatFormBackend(config)

    localContext(form) {
        row {
            gap { 8.dp }

            doubleEditor { config.top } .. width { 58.dp }
            doubleEditor { config.left } .. width { 58.dp }
            doubleEditor { config.width } .. width { 58.dp }
            doubleEditor { config.height } .. width { 58.dp }
        }
    }
}