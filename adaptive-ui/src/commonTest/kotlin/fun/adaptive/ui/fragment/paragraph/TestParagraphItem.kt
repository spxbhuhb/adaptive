package `fun`.adaptive.ui.fragment.paragraph

import `fun`.adaptive.ui.fragment.paragraph.model.ParagraphItem

class TestParagraphItem(
    override var width: Double,
    override var height: Double,
    override var baseline: Double = 0.0,
    override val instructionSetIndex: Int = 0,
    override val isWhitespace: Boolean = false,
    val mergeFun: (other: ParagraphItem) -> ParagraphItem? = { null }
) : ParagraphItem() {

    override fun measure(paragraph: AbstractParagraph<*, *>) {

    }

    override fun merge(paragraph: AbstractParagraph<*, *>, item: ParagraphItem): ParagraphItem? {
        return this.mergeFun(item)
    }

}