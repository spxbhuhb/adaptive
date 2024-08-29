package `fun`.adaptive.foundation.query

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction

/**
 * Find the first instruction of a given class [T].
 *
 * @throws NoSuchElementException
 */
inline fun <reified T : AdaptiveInstruction> AdaptiveFragment.firstInstruction() :T =
    (instructions.firstOrNull { it is T } as? T) ?: throw NoSuchElementException()