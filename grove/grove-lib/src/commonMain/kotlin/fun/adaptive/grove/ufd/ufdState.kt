package `fun`.adaptive.grove.ufd

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.foundation.value.valueFromOrNull
import `fun`.adaptive.grove.generated.resources.selectForState
import `fun`.adaptive.grove.sheet.SheetViewContext
import `fun`.adaptive.grove.sheet.model.SheetSelection
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.flowText
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace.Companion.wsContext
import `fun`.adaptive.ui.mpw.backends.UnitPaneViewBackend
import `fun`.adaptive.ui.mpw.fragments.noContent
import `fun`.adaptive.ui.mpw.fragments.toolPane
import `fun`.adaptive.ui.viewbackend.viewBackend

@Adaptive
fun ufdState(): AdaptiveFragment {

    val viewBackend = viewBackend(UnitPaneViewBackend::class)

    val context = fragment().wsContext<SheetViewContext>()
    val controller = observe { context.focusedView }
    val selection = valueFromOrNull { controller?.selectionStore }

    val isEmpty = selection == null || selection.items.isEmpty()

    toolPane(viewBackend) {
        if (isEmpty) {
            noContent(Strings.selectForState)
        } else {
            state(selection)
        }
    }

    return fragment()
}

@Adaptive
private fun state(selection : SheetSelection) {
    for (item in selection.items) {
        flowText(item.fragment.instructions.toMutableList().joinToString("\n"))
    }
}