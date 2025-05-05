package `fun`.adaptive.sandbox.recipe.ui.form

import `fun`.adaptive.adat.api.hasProblem
import `fun`.adaptive.adat.api.isNotValid
import `fun`.adaptive.adat.api.isValid
import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.document.ui.basic.docDocument
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.document.Documents
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.editor.editor
import `fun`.adaptive.ui.generated.resources.form
import `fun`.adaptive.ui.generated.resources.select_input
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun formRecipe(): AdaptiveFragment {
    column {
        gap { 16.dp } .. verticalScroll .. maxSize
        docDocument(Documents.form)
    }
    return fragment()
}
