package `fun`.adaptive.doc.example.enumEditor

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.doc.support.ExampleEnum
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.editor.enumEditorList
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.instruction.dp

/**
 * # List
 *
 * - Uses a select list (text and checkbox) to let the user select from the enum values.
 * - Translates the enum names with [resolveString](function://AdaptiveFragment).
 */
@Adaptive
fun enumEditorListExample(): AdaptiveFragment {

    val template = ListExampleData(ExampleEnum.V3)
    val formBackend = adatFormBackend(template)

    localContext(formBackend) {
        enumEditorList(ExampleEnum.entries) { template.enumValue } .. width { 200.dp }
    }

    return fragment()
}

@Adat
private class ListExampleData(
    val enumValue: ExampleEnum
)