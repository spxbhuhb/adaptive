/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive.dom

import hu.simplexion.z2.adaptive.AdaptiveBridge
import org.w3c.dom.Node

open class AdaptiveDOMPlaceholder : AdaptiveBridge<Node> {

    override val receiver = org.w3c.dom.Text()

    override fun remove(child: AdaptiveBridge<Node>) {
        receiver.parentNode?.removeChild(child.receiver)
    }

    override fun replace(oldChild: AdaptiveBridge<Node>, newChild: AdaptiveBridge<Node>) {
        receiver.parentNode?.replaceChild(newChild.receiver, oldChild.receiver)
    }

    override fun add(child: AdaptiveBridge<Node>) {
        receiver.parentNode?.appendChild(child.receiver)
    }

}