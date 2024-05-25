/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation.instruction

import hu.simplexion.adaptive.foundation.testing.Traceable
import hu.simplexion.adaptive.utility.alsoIfInstance

/**
 * Set trace for the given fragment.
 *
 * @patterns The point names to look for. Default is everything, but you can easily
 *           specify a single point like: `Trace(Regex("layout"))`
 */
class Trace(vararg val patterns : Regex = arrayOf(Regex(".*"))) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<Traceable> { it.tracePatterns = patterns }
    }
}