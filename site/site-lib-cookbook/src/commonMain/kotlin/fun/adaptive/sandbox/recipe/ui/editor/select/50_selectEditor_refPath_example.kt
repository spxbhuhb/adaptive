package `fun`.adaptive.sandbox.recipe.ui.editor.select

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.editor.refEditorPathDropdown
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.util.PathAndValue

/**
 * # Value reference editor with paths
 *
 * Use [refEditorPathDropdown](fragment://) to select a [value reference](def://) form a
 * list of [PathAndValue](class://) instances.
 */
@Adaptive
fun selectEditorRefPathExample(): AdaptiveFragment {

    val refLabel = "example"
    val template = AvValue("") // spec is just a string
    val formBackend = adatFormBackend(template)

    localContext(formBackend) {
        refEditorPathDropdown(refLabel, refPathExampleOptions) { template.refsOrNull } .. width { 200.dp }
    }

    return fragment()
}

private val refPathExampleOptions = listOf(
    PathAndValue(listOf("a", "b", "Option 1"), AvValue("Option 1")),
    PathAndValue(listOf("a", "b", "Option 2"), AvValue("Option 1")),
    PathAndValue(listOf("a", "b", "Option 3"), AvValue("Option 1")),
)