package `fun`.adaptive.document.ui.basic

import `fun`.adaptive.document.model.DocRule
import `fun`.adaptive.document.ui.DocRenderContext
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.box


@Adaptive
fun docRule(context : DocRenderContext, element: DocRule): AdaptiveFragment {

    val style = if (element.style >= 0) {
        context.styles[element.style]
    } else {
        context.theme.rule
    }

    box { style }

    return fragment()
}