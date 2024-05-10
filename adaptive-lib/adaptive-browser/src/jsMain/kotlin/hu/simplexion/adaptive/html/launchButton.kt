/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.html

import hu.simplexion.adaptive.base.*
import hu.simplexion.adaptive.dom.AdaptiveDOMNodeFragment
import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.Node

@Adaptive
fun launchButton(title: String, onClick: suspend () -> Unit) {
    manualImplementation(AdaptiveButton::class, title, onClick)
}

class AdaptiveLaunchButton(
    adapter: AdaptiveAdapter<Node>,
    parent: AdaptiveFragment<Node>,
    index: Int
) : AdaptiveDOMNodeFragment(adapter, parent, index, 2, true) {

    private val label: String get() = state[0] as String
    private val onClick: AdaptiveSupportFunction get() = state[1] as AdaptiveSupportFunction

    override val receiver = document.createElement("button") as HTMLButtonElement

    override fun genPatchInternal() {
        val closureMask = getThisClosureDirtyMask()

        if (haveToPatch(closureMask, 1)) {
            receiver.innerText = label
        }

        if (haveToPatch(closureMask, 2)) {
            receiver.onclick = {
                CoroutineScope(Dispatchers.Default).launch {
                    onClick.invokeSuspend()
                }
                onClick.declaringFragment.patch()
            }
        }
    }

}