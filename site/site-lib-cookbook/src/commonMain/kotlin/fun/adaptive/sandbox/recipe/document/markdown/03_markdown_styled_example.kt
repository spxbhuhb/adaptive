package `fun`.adaptive.sandbox.recipe.document.markdown

import `fun`.adaptive.document.ui.DocumentTheme
import `fun`.adaptive.document.ui.direct.markdown
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.textColors

/**
 * # Styled Markdown
 *
 * Pass a [DocumentTheme](class://) to [markdown](fragment://) to use different
 * styles.
 */
@Adaptive
fun markdownStyledExample(): AdaptiveFragment {

    markdown("**Hello World!**", exampleTheme)

    return fragment()
}

private val exampleTheme = DocumentTheme().apply {
    bold = instructionsOf(
        textColors.onSuccessSurface,
        backgrounds.successSurface
    )
}