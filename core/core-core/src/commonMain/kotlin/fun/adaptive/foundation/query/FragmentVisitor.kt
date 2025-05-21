package `fun`.adaptive.foundation.query

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction

abstract class FragmentVisitor<out R, in D> {

    abstract fun visitFragment(fragment: AdaptiveFragment, data: D): R

    open fun <T : Any?> visitStateVariable(fragment: AdaptiveFragment, index: Int, value: T, data: D): T {
        return value
    }

    open fun <T : AdaptiveInstruction> visitInstruction(fragment: AdaptiveFragment, instruction: T, data: D): T? {
        return instruction
    }

}