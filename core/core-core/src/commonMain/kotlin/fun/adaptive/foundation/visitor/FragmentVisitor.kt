package `fun`.adaptive.foundation.visitor

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction

abstract class FragmentVisitor<out R, in D> {

    abstract fun visitFragment(fragment: AdaptiveFragment, data: D): R

}