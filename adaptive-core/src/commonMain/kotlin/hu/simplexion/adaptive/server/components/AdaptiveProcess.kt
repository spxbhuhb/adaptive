/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.server.components

import hu.simplexion.adaptive.base.*
import hu.simplexion.adaptive.server.AdaptiveServerBridgeReceiver
import hu.simplexion.adaptive.server.AdaptiveServerFragment

fun Adaptive.process(impl : ProcessBuilder.() -> Unit) {
    manualImplementation(impl)
}

class AdaptiveProcess(
    adapter: AdaptiveAdapter<AdaptiveServerBridgeReceiver>,
    parent: AdaptiveFragment<AdaptiveServerBridgeReceiver>,
    index: Int
) : AdaptiveServerFragment(adapter, parent, index, 1) {

    val command : String
        get() = state[0] as String


    override fun innerMount(bridge: AdaptiveBridge<AdaptiveServerBridgeReceiver>) {

    }

    override fun innerUnmount(bridge: AdaptiveBridge<AdaptiveServerBridgeReceiver>) {

    }

}

class ProcessBuilder()