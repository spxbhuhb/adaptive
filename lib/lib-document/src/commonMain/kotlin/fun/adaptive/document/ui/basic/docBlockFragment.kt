package `fun`.adaptive.document.ui.basic

import `fun`.adaptive.document.model.DocBlockFragment
import `fun`.adaptive.document.ui.DocRenderContext
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.fragment


@Adaptive
fun docBlockFragment(context : DocRenderContext, element: DocBlockFragment): AdaptiveFragment {

    val style = if (element.style >= 0) {
        context.styles[element.style]
    } else {
        context.theme.blockFragment
    }

    actualize(element.url.removePrefix("actualize://")) .. style

    return fragment()
}