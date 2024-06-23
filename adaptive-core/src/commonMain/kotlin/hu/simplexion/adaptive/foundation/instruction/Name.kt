/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation.instruction

fun name(name : String) = Name(name)

/**
 * Give a name to a fragment. Trace checks if the fragment has this instruction
 * and uses it instead of the class name if so.
 */
data class Name(val name: String) : AdaptiveInstruction {
    companion object {
        val ANONYMOUS = Name("<anonymous>")
    }
}