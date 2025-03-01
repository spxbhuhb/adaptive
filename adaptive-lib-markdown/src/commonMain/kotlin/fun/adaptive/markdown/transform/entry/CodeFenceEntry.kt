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

class CodeFenceEntry(
    override val index: Int,
    override val key: FragmentKey,
    val content: String? = null,
    val language: String? = null
) : TransformEntry() {

    override fun mapping(
        config: MarkdownTransformConfig,
        instructionSets: List<AdaptiveInstructionGroup>
    ): List<LfmMapping> =

        listOf(
            EMPTY_INSTRUCTIONS,
            mapping(KotlinSignatures.STRING, content),
            mapping(KotlinSignatures.STRING, language)
        )

}