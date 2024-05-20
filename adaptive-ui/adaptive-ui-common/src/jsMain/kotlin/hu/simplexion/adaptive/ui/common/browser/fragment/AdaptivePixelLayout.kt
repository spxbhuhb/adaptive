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

open class AdaptivePixel(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AdaptiveBrowserFragment(adapter, parent, declarationIndex, 0, 2) {

    override val receiver : HTMLDivElement = document.createElement("div") as HTMLDivElement

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment {
        return AdaptiveAnonymous(adapter, this, declarationIndex, 0, fragmentFactory(1)).apply { create() }
    }

    override fun genPatchInternal(): Boolean = true

    override fun addActual(fragment: AdaptiveFragment, anchor: AdaptiveFragment?) {
        if (trace) trace("before-addActual")

        check(fragment is AdaptiveBrowserFragment)

        fragment.receiver.applyIfInstance<HTMLElement> {
            val rect = fragment.instructions.firstOrNullIfInstance<BoundingRect>() ?: DEFAULT_RECT
            with (style) {
                position = "absolute"
                top = "${rect.y}px"
                left = "${rect.x}px"
                width = "${rect.width}px"
                height = "${rect.height}px"
            }
        }

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
        with(receiver.style) {
            position = "relative"
            width = "400.px"
            height = "400.px"
        }
        parent?.addActual(this, null)
    }

    override fun afterUnmount() {
        parent?.removeActual(this)
    }

    companion object : AdaptiveFragmentCompanion {

        val DEFAULT_RECT = BoundingRect(100f, 100f, 100f, 100f)

        override val fragmentType = "$commonUI:AdaptivePixel"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptivePixel(parent.adapter, parent, index)

    }

}