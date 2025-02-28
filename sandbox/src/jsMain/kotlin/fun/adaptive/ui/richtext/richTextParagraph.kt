package `fun`.adaptive.ui.richtext

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.paragraph
import `fun`.adaptive.ui.fragment.paragraph.model.Paragraph
import `fun`.adaptive.ui.fragment.paragraph.model.ParagraphItem

@Adaptive
fun richTextParagraph(
    items: List<ParagraphItem>
) {
    val context = fragment().firstContext<RichTextContext>()
    paragraph(Paragraph(context.instructionSets, items)) .. context.theme.paragraph
}