package `fun`.adaptive.document.ui.direct

import `fun`.adaptive.document.ui.basic.docDocument
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.document.DocumentResourceSet.Companion.inlineDocument

@Adaptive
fun markdown(content: String) : AdaptiveFragment {
    docDocument(inlineDocument(".md", content))
    return fragment()
}