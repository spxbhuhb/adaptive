package `fun`.adaptive.markdown.transform.entry

import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.grove.hydration.lfm.LfmConst
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.hydration.lfm.LfmMapping
import `fun`.adaptive.markdown.transform.MarkdownTransformConfig
import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.utility.UUID

abstract class TransformEntry {

    abstract val index: Int
    abstract val key: FragmentKey

    companion object {
        val MARKDOWN_UUID = UUID<LfmDescendant>()
        val EMPTY_INSTRUCTIONS = LfmMapping(0, LfmConst(AdaptiveInstructionGroup.Companion.typeSignature(), emptyInstructions))
    }

    fun toLfmDescendant(config: MarkdownTransformConfig, instructionSets: List<AdaptiveInstructionGroup>) =
        LfmDescendant(
            MARKDOWN_UUID,
            key,
            mapping(config, instructionSets)
        )

    abstract fun mapping(config: MarkdownTransformConfig, instructionSets: List<AdaptiveInstructionGroup>): List<LfmMapping>

    fun mapping(signature: String, value: Any?) =
        LfmMapping(0, LfmConst(signature, value))
}