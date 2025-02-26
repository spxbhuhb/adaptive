package `fun`.adaptive.ui.fragment.paragraph

abstract class ParagraphItem {

    abstract val instructionSetIndex: Int

    abstract val rawWidth: Double
    abstract val rawHeight: Double

    abstract val whitespace: Boolean

    abstract fun measure()

    /**
     * If this paragraph item can merge with [item] then return
     * with the merged item.
     */
    abstract fun merge(item: ParagraphItem): ParagraphItem?

}