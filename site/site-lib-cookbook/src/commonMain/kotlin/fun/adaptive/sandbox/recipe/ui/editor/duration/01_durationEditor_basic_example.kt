package `fun`.adaptive.sandbox.recipe.ui.editor.duration

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.editor.durationEditor
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.instruction.dp
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

/**
 * # Duration editor
 */
@Adaptive
fun durationEditorBasicExample(): AdaptiveFragment {

    val template = BasicExampleData(123.minutes)
    val formBackend = adatFormBackend(template)

    localContext(formBackend) {
        durationEditor { template.durationValue } .. width { 200.dp }
    }

    return fragment()
}

@Adat
private class BasicExampleData(
    val durationValue: Duration
)