package `fun`.adaptive.sandbox.recipe.ui.editor.status

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.editor.statusEditorSingle
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.value.AvValue

/**
 * # Toggle a single status
 *
 * - label is the status name translated with [resolveString](function://AdaptiveFragment)
 */
@Adaptive
fun statusEditorSingleExample(): AdaptiveFragment {

    val status = "disabled"
    val template = AvValue("") // spec is just a string
    val formBackend = adatFormBackend(template)

    localContext(formBackend) {
        statusEditorSingle(status) { template.statusOrNull } .. width { 200.dp }
    }

    return fragment()
}