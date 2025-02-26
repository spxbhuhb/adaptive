package `fun`.adaptive.ui.fragment.paragraph.items

import `fun`.adaptive.ui.fragment.paragraph.AbstractParagraph
import `fun`.adaptive.ui.fragment.paragraph.model.ParagraphItem

open class TextParagraphItem(
    val text: String,
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
        if (other !is TextParagraphItem) return null

        return TextParagraphItem(text + other.text, instructionSetIndex).also {
            it.width = width + other.width
            it.height = maxOf(height, other.height)
            it.baseline = maxOf(baseline, other.baseline)
        }
    }

}