/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation.instruction

import hu.simplexion.adaptive.foundation.testing.Traceable
import hu.simplexion.adaptive.utility.alsoIfInstance

/**
 * Switch tracing on/off for the subject of this instruction.
 */
class Trace(val on : Boolean = true) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<Traceable> { it.trace = true }
    }
}