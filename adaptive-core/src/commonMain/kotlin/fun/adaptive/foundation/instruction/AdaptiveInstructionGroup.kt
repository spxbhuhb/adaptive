package `fun`.adaptive.foundation.instruction

import `fun`.adaptive.utility.PluginReference

class AdaptiveInstructionGroup(
    private val instructions: List<AdaptiveInstruction>
) : AdaptiveInstruction {

    @PluginReference("arrayConstructor")
    @Suppress("unused") // used by the compiler plugin in generated fragments
    constructor(instructions: Array<out AdaptiveInstruction>) : this(instructions.toList())

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

    override operator fun rangeTo(instruction: AdaptiveInstruction): AdaptiveInstructionGroup {
        return AdaptiveInstructionGroup(this.instructions + instruction)
    }

    override operator fun rangeTo(other: AdaptiveInstructionGroup): AdaptiveInstructionGroup {
        return AdaptiveInstructionGroup(this.instructions + other.instructions)
    }

    operator fun plus(instruction: AdaptiveInstruction): AdaptiveInstructionGroup {
        return AdaptiveInstructionGroup(this.instructions + instruction)
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

    override fun matchOrNull(predicate: (AdaptiveInstruction) -> Boolean): AdaptiveInstruction? {
        if (predicate(this)) return this
        return find(predicate)
    }

    operator fun contains(instruction: AdaptiveInstruction): Boolean =
        find { it == instruction } != null

    fun any(predicate: (AdaptiveInstruction) -> Boolean): Boolean =
        find(predicate) != null

    inline fun <reified T : AdaptiveInstruction> firstOrNullIfInstance(): T? =
        find { it is T } as T?

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

    override fun toString(): String =
        toMutableList().toString()

    override fun hashCode(): Int {
        return instructions.hashCode()
    }

}