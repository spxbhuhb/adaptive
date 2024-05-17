/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("FunctionName")

package hu.simplexion.adaptive.foundation.testing

import hu.simplexion.adaptive.foundation.*

class TestPlaceholder(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int
) : AdaptiveFragment(adapter, parent, index, 0) {

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun genPatchInternal() = Unit

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "hu.simplexion.adaptive.foundation.testing.TestPlaceholder"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            TestPlaceholder(parent.adapter, parent, index)

    }

}