package `fun`.adaptive.foundation.visitor

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction

abstract class FragmentTransformer<in D> : FragmentVisitor<AdaptiveFragment?, D>() {

    override fun visitFragment(fragment: AdaptiveFragment, data: D): AdaptiveFragment? {
        fragment.transformChildren(this, data)
        return fragment
    }

}