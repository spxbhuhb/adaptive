package `fun`.adaptive.sandbox.recipe.document.markdown

import `fun`.adaptive.document.ui.direct.markdown
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment

/**
 * # Inline Markdown
 *
 * Use the [markdown](fragment://) fragment and simply pass the text formatted in Markdown.
 */
@Adaptive
fun markdownInlineExample(): AdaptiveFragment {

    markdown("**Hello World!**")

    return fragment()
}