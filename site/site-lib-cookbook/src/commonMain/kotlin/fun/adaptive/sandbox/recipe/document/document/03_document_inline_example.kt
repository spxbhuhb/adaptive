package `fun`.adaptive.sandbox.recipe.document.document

import `fun`.adaptive.document.ui.basic.docDocument
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.document.DocumentResourceSet.Companion.inlineDocumentResource

/**
 * # Inline content
 *
 * Use the [inlineDocumentResource](function://DocumentResourceSet) function
 * to pass an inline content.
 *
 * - you might want to use [Markdown](guide://) fragments directly instead
 * - only Markdown is supported at the moment
 */
@Adaptive
fun documentInlineExample(): AdaptiveFragment {

    docDocument(
        inlineDocumentResource(".md", "# Header\n\nJust some inline markdown".encodeToByteArray())
    )

    return fragment()
}