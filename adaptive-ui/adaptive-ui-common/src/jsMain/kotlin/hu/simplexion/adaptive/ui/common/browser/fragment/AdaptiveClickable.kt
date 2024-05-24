/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.fragment

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.foundation.internal.BoundFragmentFactory
import hu.simplexion.adaptive.foundation.internal.BoundSupportFunction
import hu.simplexion.adaptive.foundation.structural.AdaptiveAnonymous
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.logic.alsoIfReceiver
import hu.simplexion.adaptive.utility.checkIfInstance
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener

open class AdaptiveClickable(
    adapter: AdaptiveAdapter,
    parent : AdaptiveFragment,
    index : Int
) : AdaptiveFragment(adapter, parent, index, 0, 3), EventListener {

    val onClick : BoundSupportFunction
        get() = state[1].checkIfInstance()

    val fragmentFactory : BoundFragmentFactory
        get() = state[2].checkIfInstance()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? {
        return AdaptiveAnonymous(adapter, this, declarationIndex, 0, fragmentFactory).apply { create() }
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment) {

    }

    override fun genPatchInternal(): Boolean = true

    override fun addActual(fragment: AdaptiveFragment, anchor: AdaptiveFragment?) {
        fragment.alsoIfReceiver<HTMLElement> {
            it.addEventListener("click", this)
        }
        super.addActual(fragment, anchor)
    }

    override fun removeActual(fragment: AdaptiveFragment) {
        fragment.alsoIfReceiver<HTMLElement> {
            it.removeEventListener("click", this)
        }
        super.removeActual(fragment)
    }

    override fun handleEvent(event: Event) {
        onClick.invoke()
    }

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveClickable"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveClickable(parent.adapter, parent, index)

    }


}