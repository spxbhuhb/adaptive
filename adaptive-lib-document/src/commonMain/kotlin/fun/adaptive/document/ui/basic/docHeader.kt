package `fun`.adaptive.document.ui.basic

import `fun`.adaptive.document.model.DocHeader
import `fun`.adaptive.document.ui.DocRenderContext
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.paragraph
import `fun`.adaptive.ui.fragment.paragraph.model.Paragraph

@Adaptive
fun docHeader(context : DocRenderContext, element: DocHeader): AdaptiveFragment {

    val style = if (element.style >= 0) {
        context.styles[element.style]
    } else {
        val theme = context.theme

        when (element.level) {
            1 -> theme.h1
            2 -> theme.h2
            3 -> theme.h3
            4 -> theme.h4
            5 -> theme.h5
            else -> theme.hN
        }
    }

    paragraph(
        Paragraph(
            context.styles,
            context.paragraphItems(element.children)
        )
    ) .. style

    return fragment()
}