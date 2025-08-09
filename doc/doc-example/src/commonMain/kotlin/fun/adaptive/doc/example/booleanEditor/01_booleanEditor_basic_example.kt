package `fun`.adaptive.doc.example.booleanEditor

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.editor.booleanEditor
import `fun`.adaptive.ui.form.adatFormBackend

/**
 * # Basic
 *
 * - non-nullable is the default (booleanEditor works with non-null Boolean)
 * - label is resolved from the field name by default
 */
@Adaptive
fun booleanEditorBasicExample(): AdaptiveFragment {

    val template = BasicExampleData(true)
    val formBackend = adatFormBackend(template)

    localContext(formBackend) {
        booleanEditor { template.enabled }
    }

    return fragment()
}

@Adat
private class BasicExampleData(
    val enabled: Boolean
)
