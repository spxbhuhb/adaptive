/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.dom

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.basic.NodeFragmentFactory
import hu.simplexion.adaptive.utility.vmNowMicro
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.w3c.dom.Node

/**
 * The default adapter for W3C DOM nodes used in browsers.
 */
open class AdaptiveDOMAdapter(
    override val rootContainer: Node = requireNotNull(window.document.body) { "window.document.body is null or undefined" }
) : AdaptiveAdapter {

    override val fragmentFactory = NodeFragmentFactory

    var nextId = 1L

    override val startedAt = vmNowMicro()

    override lateinit var rootFragment: AdaptiveFragment

    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Default

    override var trace = false

    override fun addActual(fragment: AdaptiveFragment) {
        if (fragment is AdaptiveDOMNodeFragment) {
            rootContainer.appendChild(fragment.receiver)
        }
        // FIXME check(fragment is AdaptiveRootFragment)
    }

    override fun removeActual(fragment: AdaptiveFragment) {
        if (fragment is AdaptiveDOMNodeFragment) {
            rootContainer.removeChild(fragment.receiver)
        }
        // FIXME check(fragment is AdaptiveRootFragment)
    }

    override fun newId(): Long =
        nextId++

}