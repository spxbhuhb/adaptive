/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.foundation.instruction

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.utility.firstInstanceOrNull

val emptyInstructions = AdaptiveInstructionGroup(emptyList())

fun instructionsOf(vararg instructions: AdaptiveInstruction) = AdaptiveInstructionGroup(listOf(*instructions))

inline operator fun <reified T : AdaptiveInstruction> List<AdaptiveInstruction>.invoke() {
    firstInstanceOrNull<T>()?.execute()
}

inline fun <reified T : AdaptiveInstruction> AdaptiveFragment.get() : T? =
    instructions.firstInstanceOfOrNull<T>()