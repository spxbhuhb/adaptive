package `fun`.adaptive.document.ui.basic

import `fun`.adaptive.document.model.DocQuote
import `fun`.adaptive.document.ui.DocRenderContext
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment


@Adaptive
fun docQuote(context : DocRenderContext, element: DocQuote): AdaptiveFragment {
    return fragment()
}