package `fun`.adaptive.doc.example.selectEditor

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.editor.selectMappingEditorList
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.input.select.item.selectInputOptionCheckbox
import `fun`.adaptive.ui.instruction.dp

/**
 * # Mapping variant
 *
 * Use [selectMappingEditorList](fragment://) or [selectMappingEditorDropdown](fragment://)
 * to map between input values and options.
 */
@Adaptive
fun selectEditorMappingExample(): AdaptiveFragment {

    val template = SelectEditorMappingExampleData()
    val formBackend = adatFormBackend(template)
    val options = listOf("First", "Second", "Third")

    localContext(formBackend) {
        selectMappingEditorList(
            options = options,
            { options.indexOf(it) },
            { selectInputOptionCheckbox(it) }
        ) { template.index } .. width { 200.dp }
    }

    return fragment()
}

@Adat
class SelectEditorMappingExampleData(
    val index: Int = 0
)