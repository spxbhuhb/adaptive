/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.dom

import hu.simplexion.adaptive.base.AdaptiveBridge
import org.w3c.dom.Node

/**
 * Base bridge class for W3C DOM Nodes. Web browser components such as
 * H1, Span etc. are descendants of this class.
 */
interface AdaptiveDOMBridge : AdaptiveBridge<Node> {

    override fun remove(child: AdaptiveBridge<Node>) {
        receiver.removeChild(child.receiver)
    }

    override fun replace(oldChild: AdaptiveBridge<Node>, newChild: AdaptiveBridge<Node>) {
        receiver.replaceChild(oldChild.receiver, newChild.receiver)
    }

    override fun add(child: AdaptiveBridge<Node>) {
        receiver.appendChild(child.receiver)
    }

}