package `fun`.adaptive.doc.example.selectEditor

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.editor.refEditorNameDropdown
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.value.AvValue

/**
 * # Value reference editor with names
 *
 * Use [refEditorNameDropdown](fragment://) to select a [value reference](def://) from a
 * list of [AvValue](class://) instances.
 *
 * Label of the field is the localized reference label (or the reference label if there is no localization).
 */
@Adaptive
fun selectEditorRefNameExample(): AdaptiveFragment {

    val refLabel = "example"
    val template = AvValue("") // spec is just a string
    val formBackend = adatFormBackend(template)

    localContext(formBackend) {
        refEditorNameDropdown(refLabel, refPathExampleOptions) { template.refsOrNull } .. width { 200.dp }
    }

    return fragment()
}

private val refPathExampleOptions = listOf(
    AvValue(name = "Option 1", spec = 1),
    AvValue(name = "Option 2", spec = 2),
    AvValue(name = "Option 3", spec = 3)
)