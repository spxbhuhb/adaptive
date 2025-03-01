package `fun`.adaptive.markdown.transform.entry

import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.grove.hydration.lfm.LfmConst
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.hydration.lfm.LfmMapping
import `fun`.adaptive.markdown.transform.MarkdownTransformConfig
import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.ui.fragment.paragraph.model.Paragraph
import `fun`.adaptive.ui.fragment.paragraph.model.ParagraphItem
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.wireformat.signature.KotlinSignatures

class QuoteEntry(
    override val index: Int,
    override val key: FragmentKey,
    val children: List<TransformEntry>
) : TransformEntry() {

    override fun mapping(config: MarkdownTransformConfig, instructionSets: List<AdaptiveInstructionGroup>): List<LfmMapping> =
        TODO()
//        listOf(
//            EMPTY_INSTRUCTIONS,
//            mapping(PARAGRAPH_SIGNATURE, Paragraph(instructionSets, items ?: emptyList()))
//        )

}