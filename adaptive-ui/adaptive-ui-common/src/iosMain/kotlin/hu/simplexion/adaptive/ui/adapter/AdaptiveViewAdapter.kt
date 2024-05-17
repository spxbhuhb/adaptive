/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.adapter

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.basic.ViewFragmentFactory
import hu.simplexion.adaptive.utility.vmNowMicro
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import platform.UIKit.UILabel
import platform.UIKit.UIView

open class AdaptiveViewAdapter(
    override val rootContainer: UIView,
    override val trace: Boolean = false
) : AdaptiveAdapter {

    override val fragmentFactory = ViewFragmentFactory

    var nextId = 1L

    override val startedAt = vmNowMicro()

    override lateinit var rootFragment: AdaptiveFragment

    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Main

    override fun addActual(fragment: AdaptiveFragment) {
        if (fragment is AdaptiveUIViewFragment) {
            rootContainer.addSubview(fragment.receiver)
        }
        // TODO check if the fragment is root, throw exc otherwise
    }

    override fun removeActual(fragment: AdaptiveFragment) {
        if (fragment is AdaptiveUIViewFragment) {
            fragment.receiver.removeFromSuperview()
        }
        // TODO check if the fragment is root, throw exc otherwise
    }

    override fun createPlaceholder(parent: AdaptiveFragment, index: Int): AdaptiveFragment {
        return AdaptiveUIViewPlaceholder(UILabel(), this, parent, index)
    }

    override fun newId(): Long =
        nextId ++

}