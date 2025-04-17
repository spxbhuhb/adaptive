package `fun`.adaptive.ui.fragment.paragraph.model

import `fun`.adaptive.ui.fragment.paragraph.AbstractParagraph

abstract class ParagraphItem {

    abstract val instructionSetIndex: Int

    abstract var width: Double
    abstract var height: Double
    abstract var baseline: Double

    abstract val isWhitespace: Boolean

    abstract fun measure(paragraph: AbstractParagraph<*, *>)

    /**
     * If this paragraph item can merge with [other] then return
     * with the merged item.
     */
    abstract fun merge(paragraph: AbstractParagraph<*, *>, other: ParagraphItem): ParagraphItem?

}