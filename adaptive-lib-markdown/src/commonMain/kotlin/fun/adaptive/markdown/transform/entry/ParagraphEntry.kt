package `fun`.adaptive.markdown.transform.entry

import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.grove.hydration.lfm.LfmMapping
import `fun`.adaptive.markdown.transform.MarkdownTransformConfig
import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.ui.fragment.paragraph.model.ParagraphItem

class ParagraphEntry(
    override val index: Int,
    override val key: FragmentKey,
    val items: List<ParagraphItem>
) : TransformEntry() {

    override fun mapping(
        config: MarkdownTransformConfig,
        instructionSets: List<AdaptiveInstructionGroup>
    ): List<LfmMapping> =

        listOf(
            EMPTY_INSTRUCTIONS,
            mapping(typeSignature<List<ParagraphItem>>(), items)
        )

}