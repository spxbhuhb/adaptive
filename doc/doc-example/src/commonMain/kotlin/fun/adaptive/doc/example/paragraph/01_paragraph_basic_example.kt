package `fun`.adaptive.doc.example.paragraph

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.fragment.paragraph.items.LinkParagraphItem
import `fun`.adaptive.ui.fragment.paragraph.items.TextParagraphItem
import `fun`.adaptive.ui.fragment.paragraph.model.ParagraphViewBackend

/**
 * # Basic paragraph
 *
 * Items are non-breakable. The view backend must be built in such a way that words
 * are separated.
 */
@Adaptive
fun paragraphBasicExample(): AdaptiveFragment {

    val backend = ParagraphViewBackend(
        listOf(
            instructionsOf(),
            instructionsOf(boldFont)
        ),
        listOf(
            TextParagraphItem("Hello", 1),
            TextParagraphItem(" ", 0),
            LinkParagraphItem("Adaptive", "https://adaptive.fun", 0),
            TextParagraphItem(" ", 0),
            TextParagraphItem("World!", 0)
        )
    )

    paragraph(backend)

    return fragment()
}