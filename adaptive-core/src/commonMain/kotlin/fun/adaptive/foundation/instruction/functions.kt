/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.foundation.instruction

import `fun`.adaptive.utility.firstOrNullIfInstance

fun instructionsOf(vararg instructions: AdaptiveInstruction) = arrayOf(*instructions)

inline operator fun <reified T : AdaptiveInstruction> Array<out AdaptiveInstruction>.invoke() {
    firstOrNullIfInstance<T>()?.execute()
}