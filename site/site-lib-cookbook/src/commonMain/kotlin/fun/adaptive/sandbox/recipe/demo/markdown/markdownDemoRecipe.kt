package `fun`.adaptive.cookbook.recipe.demo.markdown

import `fun`.adaptive.cookbook.generated.resources.markdown_demo
import `fun`.adaptive.document.ui.basic.docDocument
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.document.Documents
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.borders

@Adaptive
fun markdownDemoRecipe() : AdaptiveFragment {
    column {
        maxHeight .. verticalScroll .. padding { 16.dp } .. width { 600.dp } .. gap { 16.dp }
        borders.friendly

        docDocument(Documents.markdown_demo)
    }

    return fragment()
}