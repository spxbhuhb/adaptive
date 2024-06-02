/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation.instruction

/**
 * Give a name to a fragment. Trace checks if the fragment has this instruction
 * and uses it instead of the class name if so.
 */
data class Name(val name : String) : AdaptiveInstruction