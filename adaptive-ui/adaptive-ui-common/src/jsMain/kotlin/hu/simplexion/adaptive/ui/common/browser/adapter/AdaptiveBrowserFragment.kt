/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.adapter

import hu.simplexion.adaptive.foundation.*
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.internal.BoundFragmentFactory
import hu.simplexion.adaptive.foundation.internal.initStateMask
import deprecated.css.AdaptiveCssStyle
import hu.simplexion.adaptive.utility.checkIfInstance
import kotlinx.dom.hasClass
import org.w3c.dom.HTMLElement
import org.w3c.dom.Node

abstract class AdaptiveBrowserFragment(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    instructionsIndex: Int,
    stateSize : Int
) : AdaptiveFragment(adapter, parent, declarationIndex, instructionsIndex, stateSize) {

    abstract val receiver : Node

    fun fragmentFactory(index : Int) : BoundFragmentFactory =
        state[index].checkIfInstance()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun beforeMount() {
        parent?.addActual(this, null) ?: adapter.addActual(this, null)
    }

    override fun afterUnmount() {
        parent?.removeActual(this) ?: adapter.removeActual(this)
    }
}