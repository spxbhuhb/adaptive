/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.foundation.internal

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.ops

/**
 * @property  closureSize  The total number of state variables in this closure. This is the sum of the number
 *                         of state variables in [owner] and all [fragments].
 */
class AdaptiveClosure(
    val fragments: Array<AdaptiveFragment>,
    val closureSize: Int
) {
    val owner
        get() = fragments[0]

    private var declarationScopeSize = owner.state.size

    /**
     * Get a state variable by its index in the closure. Walks over the scopes in the
     * closure to find the state and then fetches the variable form that state.
     */
    fun get(stateVariableIndex: Int): Any? {
        if (stateVariableIndex < declarationScopeSize) {
            return owner.state[stateVariableIndex]
        }

        // indices : 0 1 / 2 / 3 4 5 6 (declaration, anonymous 1, ANONYMOUS-2)
        // requested index: 4
        // closure size of ANONYMOUS-2: 7
        // state size of ANONYMOUS-2: 4
        // index of the first variable of ANONYMOUS-2 in the closure: 3 = closure size - state size
        // index of the requested variable of ANONYMOUS-2 in the state of ANONYMOUS-2: requested index - ANONYMOUS-2 index

        for (anonymousScope in fragments) {
            val extendedClosureSize = anonymousScope.thisClosure.closureSize
            if (extendedClosureSize > stateVariableIndex) {
                return anonymousScope.state[stateVariableIndex - (extendedClosureSize - anonymousScope.state.size)]
            }
        }

        invalidIndex("get", stateVariableIndex)
    }

    /**
     * Set a state variable by its index in the closure. Walks over the scopes in the
     * closure to find the state and then sets the variable of that state.
     */
    fun set(stateVariableIndex: Int, value: Any?) {
        if (stateVariableIndex < declarationScopeSize) {
            owner.setStateVariable(stateVariableIndex, value)
            return
        }

        for (anonymousScope in fragments) {
            val extendedClosureSize = anonymousScope.thisClosure.closureSize
            if (extendedClosureSize > stateVariableIndex) {
                anonymousScope.setStateVariable(stateVariableIndex - (extendedClosureSize - anonymousScope.state.size), value)
                return
            }
        }

        invalidIndex("set", stateVariableIndex)
    }

    /**
     * Calculate the complete closure mask (or of fragments masks).
     */
    fun closureDirtyMask(): StateVariableMask {
        var mask = 0
        var position = 0
        for (fragment in fragments) {
            if (fragment.dirtyMask == initStateMask) {
                mask = initStateMask
            } else {
                mask = mask or (fragment.dirtyMask shl position)
            }
            position += fragment.state.size
        }
        return mask
    }

    override fun toString(): String {
        return "$owner"
    }

    fun dump(point: String, index: Int): String {
        val builder = StringBuilder()

        builder.append("\n")

        builder.append("Function: $point\n")
        builder.append("Index: $index\n")

        builder.append("Closure owner: $owner\n")
        builder.append("Closure size: $closureSize\n")
        builder.append("Closure dirty mask: ${closureDirtyMask()}\n")

        builder.append("Closure fragments\n")
        for (fragment in fragments) {
            builder.append("    $fragment    mask:${fragment.dirtyMask.toString(16)}    ${fragment.stateToTraceString()}\n")
        }

        builder.append("Closure content\n")
        for (index in 0 until closureSize) {
            builder.append("    $index: ${get(index)}\n")
        }

        return builder.toString()
    }

    private fun invalidIndex(point: String, index: Int): Nothing {
        ops(
            "invalidStateVariableIndex",
            """
                the code tried to reach a data that is simply not there,
                this might be an error in Adaptive or in your code,
                if this is a manually implemented fragment, check it for bugs,
                otherwise, please open a GitHub issue or contact me on Slack,
                
                ${this.dump(point, index)}
            """
        )
    }
}