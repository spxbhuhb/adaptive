package `fun`.adaptive.markdown.transform

import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.grove.hydration.lfm.LfmConst
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.hydration.lfm.LfmMapping
import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.ui.fragment.paragraph.model.Paragraph
import `fun`.adaptive.ui.fragment.paragraph.model.ParagraphItem
import `fun`.adaptive.utility.UUID

class TransformEntry(
    val index: Int,
    val key: FragmentKey,
    val items: List<ParagraphItem>? = null,
    val children: List<TransformEntry>? = null
) {

    companion object {
        val MARKDOWN_UUID = UUID<LfmDescendant>()
        val EMPTY_INSTRUCTIONS = LfmMapping(0, LfmConst(AdaptiveInstructionGroup.typeSignature(), emptyInstructions))
        val PARAGRAPH_SIGNATURE = typeSignature<Paragraph>()
    }

    fun toLfmDescendant(instructionSets: List<AdaptiveInstructionGroup>) =
        LfmDescendant(MARKDOWN_UUID, key, listOf(EMPTY_INSTRUCTIONS, mapping(instructionSets)))

    fun mapping(instructionSets: List<AdaptiveInstructionGroup>): LfmMapping =
        when (key) {
            "aui:paragraph" -> paragraphMapping(instructionSets)
            else -> TODO()
        }

    fun paragraphMapping(instructionSets: List<AdaptiveInstructionGroup>): LfmMapping =
        LfmMapping(0, LfmConst(PARAGRAPH_SIGNATURE, Paragraph(instructionSets, items ?: emptyList())))

}