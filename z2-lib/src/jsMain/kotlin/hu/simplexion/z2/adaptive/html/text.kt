/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive.html

import hu.simplexion.z2.adaptive.*
import hu.simplexion.z2.adaptive.dom.AdaptiveDOMNodeFragment
import org.w3c.dom.Node

fun Adaptive.text(content: Any?) {
    manualImplementation(AdaptiveText::class, content)
}

class AdaptiveText(
    adapter: AdaptiveAdapter<Node>,
    parent : AdaptiveFragment<Node>,
    index : Int
) : AdaptiveDOMNodeFragment(adapter, parent, index, 1, true) {

    override val receiver = org.w3c.dom.Text()

    private val content: String get() = state[0]?.toString() ?: ""

    override fun genPatchInternal() {
        val closureMask = getThisClosureDirtyMask()

        if (haveToPatch(closureMask, 1)) {
            receiver.data = content
        }
    }

    override fun remove(child: AdaptiveBridge<Node>) {
        throw IllegalStateException()
    }

    override fun replace(oldChild: AdaptiveBridge<Node>, newChild: AdaptiveBridge<Node>) {
        throw IllegalStateException()
    }

    override fun add(child: AdaptiveBridge<Node>) {
        throw IllegalStateException()
    }


}