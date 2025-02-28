package `fun`.adaptive.ui.fragment.paragraph.items

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.ui.fragment.paragraph.AbstractParagraph
import `fun`.adaptive.ui.fragment.paragraph.model.ParagraphItem

@Adat
class LinkParagraphItem(
    val text: String,
    val href: String,
    override val instructionSetIndex: Int
) : ParagraphItem() {

    override var width: Double = 0.0
    override var height: Double = 0.0
    override var baseline: Double = 0.0

    override val isWhitespace: Boolean
        get() = text.isBlank()

    override fun measure(paragraph: AbstractParagraph<*, *>) {
        paragraph.measureText(this, text, instructionSetIndex)
    }

    override fun merge(paragraph: AbstractParagraph<*, *>, other: ParagraphItem): ParagraphItem? {
        if (other.instructionSetIndex != this.instructionSetIndex) return null
        if (other !is LinkParagraphItem) return null
        if (other.href != href) return null

        return LinkParagraphItem(text + other.text, href, instructionSetIndex).also {
            it.width = width + other.width
            it.height = maxOf(height, other.height)
            it.baseline = maxOf(baseline, other.baseline)
        }
    }

}