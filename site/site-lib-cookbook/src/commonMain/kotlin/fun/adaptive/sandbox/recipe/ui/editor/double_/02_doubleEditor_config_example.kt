package `fun`.adaptive.sandbox.recipe.ui.editor.double_

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.api.properties
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.editor.doubleEditor
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.input.double_.DoubleInputConfig.Companion.doubleEditorConfig
import `fun`.adaptive.ui.instruction.dp

/**
 * # Config
 *
 * - override defaults with `doubleInputConfig`
 */
@Adaptive
fun doubleEditorConfig(): AdaptiveFragment {

    val template = ConfigExampleData(12.3)
    val formBackend = adatFormBackend(template)

    localContext(formBackend) {
        doubleEditor { template.doubleValue } .. width { 200.dp } .. doubleEditorConfig {
            label = "Overridden label"
            placeholder = "Overridden placeholder"
            decimals = 3
            unit = "m²"
        }
    }

    return fragment()
}

@Adat
private class ConfigExampleData(
    val doubleValue : Double
){
    override fun descriptor() {
        properties {
            doubleValue maximum 100.0
        }
    }
}