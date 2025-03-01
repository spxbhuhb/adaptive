package `fun`.adaptive.document.ui

import `fun`.adaptive.document.model.DocDocument
import `fun`.adaptive.document.model.DocInlineElement
import `fun`.adaptive.document.model.DocLink
import `fun`.adaptive.document.model.DocText
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.ui.fragment.paragraph.items.LinkParagraphItem
import `fun`.adaptive.ui.fragment.paragraph.items.TextParagraphItem
import `fun`.adaptive.ui.fragment.paragraph.model.ParagraphItem
import `fun`.adaptive.utility.isWhiteSpace
import `fun`.adaptive.utility.words

class DocRenderContext(
    val document: DocDocument,
    val styles: List<AdaptiveInstructionGroup>,
    val theme: DocumentTheme
) {

    fun paragraphItems(elements: List<DocInlineElement>): List<ParagraphItem> =
        elements.flatMap { element ->
            when (element) {
                is DocText -> element.expand()
                is DocLink -> element.expand()
                else -> throw IllegalStateException("Unsupported inline element: $element")
            }
        }

    fun DocText.expand(): List<TextParagraphItem> {
        if (text.isEmpty()) return emptyList()

        val words = text.words()
        if (words.isEmpty()) return listOf(TextParagraphItem(" ", style))

        val out = mutableListOf<TextParagraphItem>()

        if (text.first().isWhiteSpace) {
            out += TextParagraphItem(" ", style)
        }

        words.forEach {
            out += TextParagraphItem(it, style)
            out += TextParagraphItem(" ", style)
        }

        if (out.isNotEmpty() && ! text.last().isWhiteSpace) {
            out.removeLast()
        }

        return out
    }

    fun DocLink.expand(): List<LinkParagraphItem> {
        if (text.isEmpty()) return emptyList()

        val words = text.words()
        if (words.isEmpty()) return listOf(LinkParagraphItem(" ", url, style))

        val out = mutableListOf<LinkParagraphItem>()

        if (text.first().isWhiteSpace) {
            out += LinkParagraphItem(" ", url, style)
        }

        words.forEach {
            out += LinkParagraphItem(it, url, style)
            out += LinkParagraphItem(" ", url, style)
        }

        if (out.isNotEmpty() && ! text.last().isWhiteSpace) {
            out.removeLast()
        }

        return out
    }
}