/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.base.testing

import hu.simplexion.adaptive.base.AdaptiveBridge

class AdaptiveTestBridge(
    val id: Long
) : AdaptiveBridge<TestNode> {

    override val receiver = TestNode()

    override fun remove(child: AdaptiveBridge<TestNode>) {
        receiver.removeChild(child.receiver)
    }

    override fun replace(oldChild: AdaptiveBridge<TestNode>, newChild: AdaptiveBridge<TestNode>) {
        receiver.replaceChild(oldChild.receiver, newChild.receiver)
    }

    override fun add(child: AdaptiveBridge<TestNode>) {
        receiver.appendChild(child.receiver)
    }

    override fun toString(): String {
        return id.toString()
    }
}