/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.fragment

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.foundation.structural.AdaptiveAnonymous
import hu.simplexion.adaptive.ui.common.browser.adapter.AdaptiveBrowserFragment
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.instruction.BoundingRect
import hu.simplexion.adaptive.utility.applyIfInstance
import hu.simplexion.adaptive.utility.firstOrNullIfInstance
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

open class AdaptiveStack(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AdaptiveBrowserFragment(adapter, parent, declarationIndex, 0, 2) {

    override val receiver: HTMLDivElement = document.createElement("div") as HTMLDivElement

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment {
        return AdaptiveAnonymous(adapter, this, declarationIndex, 0, fragmentFactory(1)).apply { create() }
    }

    override fun genPatchInternal(): Boolean = true

    override fun addAnchor(fragment: AdaptiveFragment, higherAnchor: AdaptiveFragment?) {
        (document.createElement("div") as HTMLDivElement).also {
            it.style.display = "contents"
            it.id = fragment.id.toString()
            if (higherAnchor != null) {
                checkNotNull(document.getElementById(higherAnchor.id.toString())) { "missing higher anchor" }.appendChild(it)
            } else {
                receiver.appendChild(it)
            }
        }
    }

    override fun removeAnchor(fragment: AdaptiveFragment) {
        checkNotNull(document.getElementById(fragment.id.toString())) { "missing anchor" }.remove()
    }

    override fun addActual(fragment: AdaptiveFragment, anchor: AdaptiveFragment?) {
        if (trace) trace("before-addActual")

        check(fragment is AdaptiveBrowserFragment)

        if (anchor == null) {
            receiver.appendChild(fragment.receiver)
        } else {
            checkNotNull(document.getElementById(anchor.id.toString())) { "missing anchor" }
                .appendChild(fragment.receiver)
        }

        if (trace) trace("after-addActual")
    }

    override fun removeActual(fragment: AdaptiveFragment) {
        if (trace) trace("before-removeActual")

        check(fragment is AdaptiveBrowserFragment)

        (fragment.receiver as HTMLElement).remove()

        if (trace) trace("after-removeActual")
    }

    override fun beforeMount() {
        parent?.addActual(this, null)
    }

    override fun afterUnmount() {
        parent?.removeActual(this)
    }

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveStack"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveStack(parent.adapter, parent, index)

    }

}