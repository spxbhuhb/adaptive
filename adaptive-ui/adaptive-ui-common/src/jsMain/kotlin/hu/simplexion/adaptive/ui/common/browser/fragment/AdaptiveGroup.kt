/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.fragment

import hu.simplexion.adaptive.foundation.*
import hu.simplexion.adaptive.foundation.structural.AdaptiveAnonymous
import hu.simplexion.adaptive.ui.common.browser.adapter.AdaptiveBrowserFragment
import hu.simplexion.adaptive.ui.common.browser.fragment.AdaptivePixel.Companion.DEFAULT_RECT
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.instruction.BoundingRect
import hu.simplexion.adaptive.utility.applyIfInstance
import hu.simplexion.adaptive.utility.checkIfInstance
import hu.simplexion.adaptive.utility.firstOrNullIfInstance
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

open class AdaptiveGroup(
    adapter: AdaptiveAdapter,
    parent : AdaptiveFragment,
    index : Int
) : AdaptiveBrowserFragment(adapter, parent, index, 0, 1) {

    override val receiver : HTMLDivElement = document.createElement("div") as HTMLDivElement

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment {
        return AdaptiveAnonymous(adapter, this, declarationIndex, 0, state[1].checkIfInstance()).apply { create() }
    }

    override fun genPatchInternal(): Boolean = true

    override fun addActual(fragment: AdaptiveFragment, anchor: AdaptiveFragment?) {
        if (trace) trace("before-addActual")

        check(fragment is AdaptiveBrowserFragment)

        receiver.appendChild(fragment.receiver)

        if (trace) trace("after-addActual")
    }

    override fun removeActual(fragment: AdaptiveFragment) {
        if (trace) trace("before-removeActual")

        check(fragment is AdaptiveBrowserFragment)

        receiver.removeChild(fragment.receiver)

        if (trace) trace("after-removeActual")
    }

    override fun beforeMount() {
        parent?.addActual(this, null)
    }

    override fun afterUnmount() {
        parent?.removeActual(this)
    }

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveGroup"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveGroup(parent.adapter, parent, index)

    }

}