/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.basic

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.foundation.structural.AdaptiveAnonymous
import hu.simplexion.adaptive.ui.adapter.IOSLayoutFragment
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.instruction.BoundingRect
import platform.UIKit.UIView

open class AdaptivePixel(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : IOSLayoutFragment(adapter, parent, declarationIndex, 0, 2) {

    override val receiver : UIView = UIView()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment {
        return AdaptiveAnonymous(adapter, this, declarationIndex, 0, fragmentFactory(1)).apply { create() }
    }

    override fun genPatchInternal(): Boolean = true

    override fun addActual(fragment: AdaptiveFragment, anchor: AdaptiveFragment?) {
        if (trace) trace("before-addActual")

        check(fragment is IOSLayoutFragment)

        val rect = fragment.uiInstructions.frame ?: DEFAULT_RECT
        fragment.setFrame(rect.x, rect.y, rect.width, rect.height)

        receiver.addSubview(fragment.receiver)

        if (trace) trace("after-addActual")
    }

    override fun removeActual(fragment: AdaptiveFragment) {
        if (trace) trace("before-removeActual")

        check(fragment is IOSLayoutFragment)

        fragment.receiver.removeFromSuperview()

        if (trace) trace("after-removeActual")
    }

    companion object : AdaptiveFragmentCompanion {

        val DEFAULT_RECT = BoundingRect(100f, 100f, 100f, 100f)

        override val fragmentType = "$commonUI:AdaptivePixel"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptivePixel(parent.adapter, parent, index)

    }

}