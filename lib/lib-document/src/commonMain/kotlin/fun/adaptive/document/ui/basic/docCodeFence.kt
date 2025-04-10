package `fun`.adaptive.document.ui.basic

import `fun`.adaptive.document.model.DocCodeFence
import `fun`.adaptive.document.ui.DocRenderContext
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.codefence.codeFence

@Adaptive
fun docCodeFence(context : DocRenderContext, element: DocCodeFence): AdaptiveFragment {

    val style = if (element.style >= 0) {
        context.styles[element.style]
    } else {
        context.theme.codeFence
    }

    codeFence(element.code, element.language) .. style

    return fragment()
}
