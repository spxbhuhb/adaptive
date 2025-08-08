package `fun`.adaptive.doc.example.selectEditor

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.editor.refEditorNameDropdown
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.value.AvValue

/**
 * # Multiple value reference editors
 *
 * You can use multiple value reference editors in the same form with different labels.
 */
@Adaptive
fun selectEditorRefMultiExample(): AdaptiveFragment {

    val refLabel1 = "example-1"
    val refLabel2 = "example-2"
    val refLabel3 = "example-3"

    val template = AvValue("") // spec is just a string
    val formBackend = adatFormBackend(template)

    localContext(formBackend) {
        column {
            gap { 16.dp }
            refEditorNameDropdown(refLabel1, refMultiExampleOptions1) { template.refsOrNull } .. width { 200.dp }
            refEditorNameDropdown(refLabel2, refMultiExampleOptions2) { template.refsOrNull } .. width { 200.dp }
            refEditorNameDropdown(refLabel3, refMultiExampleOptions3) { template.refsOrNull } .. width { 200.dp }
        }
    }

    return fragment()
}

private val refMultiExampleOptions1 = listOf(
    AvValue(name = "Option 1.1", spec = 11),
    AvValue(name = "Option 1.2", spec = 12),
    AvValue(name = "Option 1.3", spec = 13)
)

private val refMultiExampleOptions2 = listOf(
    AvValue(name = "Option 2.1", spec = 21),
    AvValue(name = "Option 2.2", spec = 22),
    AvValue(name = "Option 2.3", spec = 23)
)

private val refMultiExampleOptions3 = listOf(
    AvValue(name = "Option 3.1", spec = 31),
    AvValue(name = "Option 3.2", spec = 32),
    AvValue(name = "Option 3.3", spec = 33)
)