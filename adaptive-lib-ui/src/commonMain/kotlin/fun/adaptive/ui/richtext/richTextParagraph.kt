package `fun`.adaptive.ui.richtext

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.paragraph
import `fun`.adaptive.ui.fragment.paragraph.model.Paragraph
import `fun`.adaptive.ui.fragment.paragraph.model.ParagraphItem

@Adaptive
fun richTextParagraph(
    items: List<ParagraphItem>
): AdaptiveFragment {

    val context = fragment().firstContext<RichText>()

    paragraph(Paragraph(context.instructionSets, items)) .. context.theme.paragraph

    return fragment()
}