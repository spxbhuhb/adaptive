/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation.instruction

import hu.simplexion.adaptive.utility.firstOrNullIfInstance

inline operator fun <reified T : AdaptiveInstruction> Array<out AdaptiveInstruction>.invoke() {
    firstOrNullIfInstance<T>()?.execute()
}