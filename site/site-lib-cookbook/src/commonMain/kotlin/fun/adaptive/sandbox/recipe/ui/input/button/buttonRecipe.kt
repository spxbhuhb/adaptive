package `fun`.adaptive.sandbox.recipe.ui.input.button

import `fun`.adaptive.document.ui.basic.docDocument
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.document.Documents
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.generated.resources.button
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun buttonRecipe(): AdaptiveFragment {
    column {
        gap { 16.dp } .. verticalScroll .. maxSize
        docDocument(Documents.button)
    }

    return fragment()
}