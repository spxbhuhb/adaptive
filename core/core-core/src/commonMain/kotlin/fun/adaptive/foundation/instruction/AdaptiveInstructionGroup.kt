package `fun`.adaptive.foundation.instruction

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.metadata.AdatClassMetadata
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.utility.PluginReference

@Adat
class AdaptiveInstructionGroup(
    private val instructions: List<AdaptiveInstruction>
) : AdaptiveInstruction, AdatClass {

    @PluginReference("arrayConstructor")
    @Suppress("unused") // used by the compiler plugin in generated fragments
    // mapNotNull handles cases when there are no instructions and the state variable used contains null
    // TODO think about null instructions in AdaptiveInstructionGroup constructor, maybe optimise it
    constructor(instructions: Array<out AdaptiveInstruction?>) : this(instructions.mapNotNull { it })

    /**
     * Apply the all the instructions in this group to a subject. The instruction has
     * to check if the subject is something it can handle and do nothing if not.
     */
    override fun applyTo(subject: Any) {
        for (instruction in instructions) {
            instruction.applyTo(subject)
        }
    }

    // ------------------------------------------------------------------
    // Instruction tree build
    // ------------------------------------------------------------------

    /**
     * Instruction precedence:
     *
     * - outer (strongest)
     * - inner
     * - argument (weakest)
     */
    override operator fun rangeTo(instruction: AdaptiveInstruction): AdaptiveInstructionGroup {
        return AdaptiveInstructionGroup(this.instructions + instruction)
    }

    /**
     * Instruction precedence:
     *
     * - outer (strongest)
     * - inner
     * - argument (weakest)
     */
    override operator fun rangeTo(other: AdaptiveInstructionGroup): AdaptiveInstructionGroup {
        return AdaptiveInstructionGroup(this.instructions + other.instructions)
    }

    operator fun plus(instruction: AdaptiveInstruction): AdaptiveInstructionGroup {
        return AdaptiveInstructionGroup(this.instructions + instruction)
    }

    fun removeAll(condition: (AdaptiveInstruction) -> Boolean): AdaptiveInstructionGroup {
        val newInstructions = mutableListOf<AdaptiveInstruction>()

        for (instruction in instructions) {
            if (! condition(instruction)) {
                if (instruction is AdaptiveInstructionGroup) {
                    instruction.removeAll(condition)
                        .takeIf { it.isNotEmpty() }
                        ?.let { newInstructions += it }
                } else {
                    newInstructions += instruction
                }
            }
        }

        return AdaptiveInstructionGroup(newInstructions)
    }

    // ------------------------------------------------------------------
    // Instruction tree lookup
    // ------------------------------------------------------------------

    fun find(predicate: (AdaptiveInstruction) -> Boolean): AdaptiveInstruction? {
        for (instruction in instructions) {
            instruction.matchOrNull(predicate)?.let { return it }
        }
        return null
    }

    fun findLast(predicate: (AdaptiveInstruction) -> Boolean): AdaptiveInstruction? {
        var index = instructions.size - 1
        while (index >= 0) {
            instructions[index --].matchOrNull(predicate)?.let { return it }
        }
        return null
    }

    override fun matchOrNull(predicate: (AdaptiveInstruction) -> Boolean): AdaptiveInstruction? {
        if (predicate(this)) return this
        return find(predicate)
    }

    operator fun contains(instruction: AdaptiveInstruction): Boolean =
        find { it == instruction } != null

    fun any(predicate: (AdaptiveInstruction) -> Boolean): Boolean =
        find(predicate) != null

    inline fun <reified T : AdaptiveInstruction> firstInstanceOfOrNull(): T? =
        find { it is T } as T?

    inline fun <reified T : AdaptiveInstruction> firstInstanceOf(): T =
        find { it is T } as T

    inline fun <reified T : AdaptiveInstruction> lastInstanceOfOrNull(): T? =
        findLast { it is T } as T?

    fun isNotEmpty(): Boolean {
        if (instructions.isEmpty()) return false

        for (instruction in instructions) {
            if (instruction is AdaptiveInstructionGroup) {
                if (instruction.isNotEmpty()) return true
            } else {
                return true
            }
        }

        return false
    }

    fun isEmpty(): Boolean = ! isNotEmpty()

    val size: Int
        get() {
            var size = 0
            forEach {
                if (it is AdaptiveInstructionGroup) {
                    size += it.size
                } else {
                    size += 1
                }
            }
            return size
        }

    // ------------------------------------------------------------------
    // Instruction tree filter
    // ------------------------------------------------------------------

    fun filter(
        out: MutableList<AdaptiveInstruction> = mutableListOf(),
        predicate: (AdaptiveInstruction) -> Boolean,
    ): List<AdaptiveInstruction> {
        for (instruction in instructions) {
            instruction.matchOrNull(predicate)?.let { out += it }
        }
        return out
    }

    inline fun <reified T> filterIsInstance(): List<T> {
        @Suppress("UNCHECKED_CAST")
        return filter { it is T } as List<T>
    }

    // ------------------------------------------------------------------
    // Instruction tree traversal
    // ------------------------------------------------------------------

    fun forEach(action: (AdaptiveInstruction) -> Unit) {
        instructions.forEach {
            if (it is AdaptiveInstructionGroup) {
                it.forEach(action)
            } else {
                action(it)
            }
        }
    }

    /**
     * Create a **flat** list of all the instructions in this group and any groups it contains.
     *
     * The result **does not** contain any instances of [AdaptiveInstructionGroup].
     */
    fun toMutableList(out: MutableList<AdaptiveInstruction> = mutableListOf()): MutableList<AdaptiveInstruction> {
        instructions.forEach {
            if (it is AdaptiveInstructionGroup) {
                it.toMutableList(out)
            } else {
                out.add(it)
            }
        }
        return out
    }

    // ------------------------------------------------------------------
    // Utility
    // ------------------------------------------------------------------

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is AdaptiveInstructionGroup) return false
        return instructions == other.instructions
    }

    override fun toString(): String {
        return toMutableList().toString()
    }

    override fun hashCode(): Int {
        return instructions.hashCode()
    }

    // ------------------------------------------------------------------
    // Adat support
    // ------------------------------------------------------------------

    override val adatCompanion: AdatCompanion<AdaptiveInstructionGroup>
        get() = AdaptiveInstructionGroup

    override fun genGetValue(index: Int): Any? =
        when (index) {
            0 -> instructions
            else -> throw IndexOutOfBoundsException()
        }

    companion object : AdatCompanion<AdaptiveInstructionGroup> {

        override val wireFormatName: String
            get() = "fun.adaptive.foundation.instruction.AdaptiveInstructionGroup"

        override val adatMetadata = AdatClassMetadata(
            version = 1,
            name = wireFormatName,
            flags = AdatClassMetadata.IMMUTABLE,
            properties = listOf(
                AdatPropertyMetadata(
                    "instructions",
                    index = 0,
                    flags = AdatPropertyMetadata.VAL or AdatPropertyMetadata.IMMUTABLE_VALUE,
                    signature = "Lkotlin.collections.List<*>;"
                )
            )
        )

        override val adatWireFormat: AdatClassWireFormat<AdaptiveInstructionGroup>
            get() = AdatClassWireFormat(this, adatMetadata)

        override fun newInstance(values: Array<Any?>): AdaptiveInstructionGroup {
            @Suppress("UNCHECKED_CAST")
            return AdaptiveInstructionGroup(values[0] as List<AdaptiveInstruction>)
        }
    }
}