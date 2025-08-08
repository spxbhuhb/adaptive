package `fun`.adaptive.doc.example.doubleEditor

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.editor.doubleEditor
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.instruction.dp

/**
 * # Basic
 *
 * - nullability comes from the Adat class metadata
 * - label is automatically resolved from the field name, if cannot be resolved, uses the field name
 * - 2 decimals
 */
@Adaptive
fun doubleEditorBasic(): AdaptiveFragment {

    val template = BasicExampleData(12.3)
    val formBackend = adatFormBackend(template)

    localContext(formBackend) {
        doubleEditor { template.doubleValue } .. width { 200.dp }
    }

    return fragment()
}

@Adat
private class BasicExampleData(
    val doubleValue: Double
)