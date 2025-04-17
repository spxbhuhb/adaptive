/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.foundation.instruction

interface AdaptiveInstruction {

    /**
     * Apply the instructions to a subject. The instruction has to check if the subject
     * is something it can handle and do nothing if not.
     */
    fun applyTo(subject: Any) = Unit

    /**
     * Execute the instruction if it supports execution. Default is no-op.
     */
    fun execute() = Unit

    /**
     * Instruction precedence:
     *
     * - outer (strongest)
     * - inner
     * - argument (weakest)
     */
    operator fun rangeTo(instruction: AdaptiveInstruction): AdaptiveInstructionGroup {
        return AdaptiveInstructionGroup(listOf(this, instruction))
    }

    /**
     * Instruction precedence:
     *
     * - outer (strongest)
     * - inner
     * - argument (weakest)
     */
    operator fun rangeTo(other: AdaptiveInstructionGroup): AdaptiveInstructionGroup {
        return AdaptiveInstructionGroup(listOf(this, other))
    }

    fun matchOrNull(predicate: (AdaptiveInstruction) -> Boolean) : AdaptiveInstruction? =
        if (predicate(this)) this else null

}