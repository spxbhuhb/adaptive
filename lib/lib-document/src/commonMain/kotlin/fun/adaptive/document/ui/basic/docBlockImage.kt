package `fun`.adaptive.document.ui.basic

import `fun`.adaptive.document.model.DocBlockImage
import `fun`.adaptive.document.ui.DocRenderContext
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.image.ImageResourceSet.Companion.remoteImage
import `fun`.adaptive.ui.api.image
import `fun`.adaptive.ui.api.maxSize


@Adaptive
fun docBlockImage(context : DocRenderContext, element: DocBlockImage): AdaptiveFragment {

    val style = if (element.style >= 0) {
        context.styles[element.style]
    } else {
        context.theme.blockImage
    }

    image(remoteImage(element.url)) .. style .. maxSize

    return fragment()
}