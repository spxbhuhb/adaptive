package `fun`.adaptive.doc.example.selectEditor

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.editor.refEditorDropdown
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.input.select.item.selectInputOptionText
import `fun`.adaptive.ui.input.select.item.selectInputValueText
import `fun`.adaptive.ui.input.select.mapping.RefMapInputMapping
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.AvValue

/**
 * # Manual value reference editor
 *
 * Use [refEditorDropdown](fragment://) to build a [value reference](def://) editor manually.
 *
 * You can use any kind of options and mapping and rendering with this fragment, but you
 * have to pass all of these manually.
 *
 * For common use cases such as using name or path, consider the specialized versions of the fragment.
 */
@Adaptive
fun selectEditorRefManualExample(): AdaptiveFragment {

    val refLabel = "example"
    val template = AvValue("") // spec is just a string
    val formBackend = adatFormBackend(template)

    localContext(formBackend) {
        refEditorDropdown(
            refLabel,
            refManualExampleOptions,
            { it.first },
            RefMapInputMapping(refLabel),
            { selectInputOptionText(it) { second } },
            { selectInputValueText(it) { second } }
        ) { template.refsOrNull } .. width { 200.dp }
    }

    return fragment()
}

private val refManualExampleOptions = listOf(
    UUID<AvValue<*>>("1ecec9fc-c638-42fc-9271-0c9e3a22507a") to "Option 1",
    UUID<AvValue<*>>("2ecec9fc-c638-42fc-9271-0c9e3a22507a") to "Option 2",
    UUID<AvValue<*>>("3ecec9fc-c638-42fc-9271-0c9e3a22507a") to "Option 3",
)