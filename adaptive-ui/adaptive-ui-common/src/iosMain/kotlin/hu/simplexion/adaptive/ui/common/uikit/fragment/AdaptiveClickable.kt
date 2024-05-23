/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.uikit.fragment

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.foundation.internal.BoundFragmentFactory
import hu.simplexion.adaptive.foundation.internal.BoundSupportFunction
import hu.simplexion.adaptive.foundation.structural.AdaptiveAnonymous
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.fragment.alsoIfReceiver
import hu.simplexion.adaptive.ui.common.uikit.adapter.GestureTarget
import hu.simplexion.adaptive.utility.checkIfInstance
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UITapGestureRecognizer
import platform.UIKit.UIView

open class AdaptiveClickable(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AdaptiveFragment(adapter, parent, index, 0, 3) {

    val onClick: BoundSupportFunction
        get() = state[1].checkIfInstance()

    val fragmentFactory: BoundFragmentFactory
        get() = state[2].checkIfInstance()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? {
        return AdaptiveAnonymous(adapter, this, declarationIndex, 0, fragmentFactory).apply { create() }
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment) {

    }

    override fun genPatchInternal(): Boolean = true

    @OptIn(ExperimentalForeignApi::class)
    override fun addActual(fragment: AdaptiveFragment, anchor: AdaptiveFragment?) {
        fragment.alsoIfReceiver<UIView> {
            it.addGestureRecognizer(UITapGestureRecognizer(GestureTarget(onClick), NSSelectorFromString("viewTapped")))
            it.setUserInteractionEnabled(true)
        }
        super.addActual(fragment, anchor)
    }

    override fun removeActual(fragment: AdaptiveFragment) {
        fragment.alsoIfReceiver<UIView> {
            // TODO remove the gesture recognizer
        }
        super.removeActual(fragment)
    }

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveClickable"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveClickable(parent.adapter, parent, index)

    }


}