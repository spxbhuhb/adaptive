package `fun`.adaptive.site

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.query.firstWithOrNull
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.grove.doc.model.GroveDocSpec
import `fun`.adaptive.grove.doc.ui.DocToolViewBackend
import `fun`.adaptive.grove.doc.ui.ReferenceToolViewBackend
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.select.item.selectInputOptionText
import `fun`.adaptive.ui.input.select.selectInputBackend
import `fun`.adaptive.ui.input.select.selectInputList
import `fun`.adaptive.ui.input.text.textInput
import `fun`.adaptive.ui.input.text.textInputBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.input.FocusFirst
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.support.UiClose
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.value.AvValue
import kotlin.math.min

@Adaptive
fun searchDialog(
    workspace: MultiPaneWorkspace,
    close: UiClose
) {
    val metrics = mediaMetrics()

    val inputBackend = observe { textInputBackend("") }

    val selectBackend = selectInputBackend {
        options = findHits(workspace, inputBackend.inputValue?.lowercase()).map { it.nameLike }
        onChange = { openDocument(it, workspace, close) }
    }

    localContext(close) {
        column {
            width { min(metrics.viewWidth, 600.0).dp }
            height { min(metrics.viewHeight, 600.0).dp }
            fillStrategy.constrain
            cornerRadius { 8.dp }
            border(colors.onSurface, 0.5.dp)
            backgrounds.surfaceVariant
            padding(16.dp)

            onKeydown {
                if (it.isEscape) close.uiClose()
                if (it.isEnter) openDocument(selectBackend.options.firstOrNull(), workspace, close)
            }

            textInput(inputBackend) .. focusFirst

            column {
                maxSize .. verticalScroll .. padding { 16.dp } .. backgrounds.surface

                selectInputList(
                    selectBackend
                ) { selectInputOptionText(it) }
            }
        }
    }
}

private fun findHits(workspace: MultiPaneWorkspace, query: String?): List<AvValue<GroveDocSpec>> =
    if (query == null || query.isBlank()) {
        emptyList()
    } else {
        workspace.toolBackend(ReferenceToolViewBackend::class)
            ?.filterByNamePart(query)
            ?.take(20)
            ?: emptyList()
    }

private fun openDocument(
    name: String?,
    workspace: MultiPaneWorkspace,
    close: UiClose
) {
    if (name == null) return

    workspace.toolBackend(DocToolViewBackend::class)?.openDocument(name, emptySet())

    close.uiClose()

    // FIXME pretty ugly refocusing on the main workspace

    val mpwFragment = workspace.workspaceFragment ?: return
    val mpwContainer = mpwFragment.firstWithOrNull<FocusFirst>() as? AbstractAuiFragment<*> ?: return

    (mpwFragment.adapter as? AbstractAuiAdapter<*, *>)?.focus(mpwContainer)
}