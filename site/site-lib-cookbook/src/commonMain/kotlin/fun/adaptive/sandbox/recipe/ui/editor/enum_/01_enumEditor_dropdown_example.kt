package `fun`.adaptive.sandbox.recipe.ui.editor.enum_

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.sandbox.support.ExampleEnum
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.editor.enumEditorDropdown
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.instruction.dp

/**
 * # Dropdown
 *
 * - Uses a select dropdown (text only) to let the user select from the enum values.
 * - Translates the enum names with [resolveString](function://AdaptiveFragment).
 */
@Adaptive
fun enumEditorDropdownExample(): AdaptiveFragment {

    val template = BasicExampleData(ExampleEnum.V1)
    val formBackend = adatFormBackend(template)

    localContext(formBackend) {
        enumEditorDropdown(ExampleEnum.entries) { template.enumValue } .. width { 200.dp }
    }

    return fragment()
}

@Adat
private class BasicExampleData(
    val enumValue: ExampleEnum
)