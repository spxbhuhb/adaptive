package `fun`.adaptive.foundation.query

import `fun`.adaptive.foundation.AdaptiveFragment

abstract class FragmentVisitor {

    fun visitFragment(fragment: AdaptiveFragment) {
        fragment.acceptChildren(this)
    }

}