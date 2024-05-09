/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.dom

import hu.simplexion.adaptive.base.AdaptiveAdapter
import hu.simplexion.adaptive.base.AdaptiveBridge
import hu.simplexion.adaptive.base.AdaptiveFragment
import hu.simplexion.adaptive.utility.vmNowMicro
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.w3c.dom.Node

/**
 * The default adapter for W3C DOM nodes used in browsers.
 */
open class AdaptiveDOMAdapter(
    val node: Node = requireNotNull(window.document.body) { "window.document.body is null or undefined" }
) : AdaptiveAdapter<Node> {

    var nextId = 1L

    override val startedAt = vmNowMicro()

    override lateinit var rootFragment: AdaptiveFragment<Node>

    override val rootBridge = AdaptiveDOMPlaceholder().also {
        node.appendChild(it.receiver)
    }

    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Default

    override var trace = false

    override fun createPlaceholder(): AdaptiveBridge<Node> {
        return AdaptiveDOMPlaceholder()
    }

    override fun newId(): Long =
        nextId++

}