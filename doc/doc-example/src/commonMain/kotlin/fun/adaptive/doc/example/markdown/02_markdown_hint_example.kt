package `fun`.adaptive.doc.example.markdown

import `fun`.adaptive.document.ui.direct.markdownHint
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment

/**
 * # Markdown hint
 *
 * Use the [markdownHint](fragment://) fragment to render a hint.
 *
 * This uses the [hint](property://DocumentTheme) document theme which is aimed for
 * rendering small UI hints.
 */
@Adaptive
fun markdownHintExample(): AdaptiveFragment {

    markdownHint("Hello World!")

    return fragment()
}