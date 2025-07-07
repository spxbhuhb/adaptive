package `fun`.adaptive.sandbox.recipe.ui.text

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.fragment.paragraph.items.TextParagraphItem
import `fun`.adaptive.ui.fragment.paragraph.model.ParagraphViewBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.textColors

/**
 * # Long paragraph
 *
 * The paragraph arranges the items to fill the available width.
 */
@Adaptive
fun paragraphLongExample(): AdaptiveFragment {

    val backend = ParagraphViewBackend(
        listOf(
            instructionsOf(),
        ),
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
            .split(" ")
            .map { TextParagraphItem(it, 0) }
            .flatMap { listOf(it, TextParagraphItem(" ", 0)) }

    )

    paragraph(backend) .. width { 400.dp }

    return fragment()
}

