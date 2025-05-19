package `fun`.adaptive.sandbox.recipe.ui.tree

import `fun`.adaptive.document.ui.basic.docDocument
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.document.Documents
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.generated.resources.tree
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun treeRecipe(): AdaptiveFragment {
    column {
        gap { 16.dp } .. verticalScroll .. maxSize
        docDocument(Documents.tree)
    }

    return fragment()
}


