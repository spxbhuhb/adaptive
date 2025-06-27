package `fun`.adaptive.grove.ufd

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.foundation.value.valueFromOrNull
import `fun`.adaptive.grove.generated.resources.openForComponents
import `fun`.adaptive.grove.sheet.SheetViewBackend
import `fun`.adaptive.grove.sheet.model.SheetItem
import `fun`.adaptive.grove.sheet.model.SheetSelection
import `fun`.adaptive.grove.sheet.operation.SelectByIndex
import `fun`.adaptive.grove.ufd.app.GroveUdfModuleMpw.Companion.udfModule
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.theme.textSmall
import `fun`.adaptive.ui.mpw.backends.UnitPaneViewBackend
import `fun`.adaptive.ui.mpw.fragments.noContent
import `fun`.adaptive.ui.mpw.fragments.toolPane
import `fun`.adaptive.ui.viewbackend.viewBackend

@Adaptive
fun ufdComponents(): AdaptiveFragment {

    val viewBackend = viewBackend(UnitPaneViewBackend::class)

    val sheetBackend = observe { fragment().udfModule.focusedSheet }
    val selection = valueFromOrNull { sheetBackend?.selectionStore } ?: SheetSelection(emptyList())

    val isEmpty = (sheetBackend == null)

    toolPane(viewBackend) {
        if (isEmpty) {
            noContent(Strings.openForComponents)
        } else {
            componentList(sheetBackend, selection)
        }
    }

    return fragment()
}

@Adaptive
fun componentList(controller: SheetViewBackend, selection: SheetSelection) {
    column {
        maxSize .. scroll .. padding { 4.dp }

        for (item in controller.items) {
            if (! item.removed) {
                itemRow(item, selection, controller)
            }
        }
    }
}

@Adaptive
fun itemRow(item: SheetItem, selection: SheetSelection, controller: SheetViewBackend) {
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
