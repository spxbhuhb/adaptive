package `fun`.adaptive.doc.example.paragraph

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.fragment.paragraph.items.LinkParagraphItem
import `fun`.adaptive.ui.fragment.paragraph.items.TextParagraphItem
import `fun`.adaptive.ui.fragment.paragraph.model.ParagraphViewBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.snackbar.infoNotification
import `fun`.adaptive.ui.support.UiNavigator

/**
 * # Navigator
 *
 * Provide a [UiNavigator](interface://) [local context](guide://) to override default
 * link behavior.
 */
@Adaptive
fun paragraphNavigatorExample(): AdaptiveFragment {

    val backend = ParagraphViewBackend(
        listOf(
            instructionsOf()
        ),
        listOf(
            TextParagraphItem("Hello", 0),
            TextParagraphItem(" ", 0),
            LinkParagraphItem("Adaptive", "https://adaptive.fun", 0),
            TextParagraphItem(" ", 0),
            TextParagraphItem("World!", 0)
        )
    )

    val navigator = UiNavigator {
        infoNotification("Navigating to: $it")
        true
    }

    localContext(navigator) {
        paragraph(backend) .. width { 400.dp }
    }

    return fragment()
}

