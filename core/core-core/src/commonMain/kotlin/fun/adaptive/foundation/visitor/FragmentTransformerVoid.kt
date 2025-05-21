package `fun`.adaptive.foundation.visitor

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.wireformat.json.elements.JsonObject

abstract class FragmentTransformerVoid : FragmentTransformer<Nothing?>() {

    open fun visitFragment(fragment : AdaptiveFragment): AdaptiveFragment? {
        fragment.transformChildren(this, null)
        return fragment
    }

    final override fun visitFragment(fragment: AdaptiveFragment, data: Nothing?): AdaptiveFragment? =
        visitFragment(fragment)

}