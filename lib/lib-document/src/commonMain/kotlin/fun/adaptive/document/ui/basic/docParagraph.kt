package `fun`.adaptive.document.ui.basic

import `fun`.adaptive.document.model.DocParagraph
import `fun`.adaptive.document.ui.DocRenderContext
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.paragraph
import `fun`.adaptive.ui.fragment.paragraph.model.Paragraph

@Adaptive
fun docParagraph(context: DocRenderContext, element: DocParagraph): AdaptiveFragment {

    val style = when {
        element.style >= 0 -> context.styles[element.style]
        element.standalone -> context.theme.paragraph
        else -> context.theme.innerParagraph
    }

    paragraph(
        Paragraph(
            context.styles,
            context.paragraphItems(element.content)
        )
    ) .. style

    return fragment()
}
