/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.server.adaptive

import hu.simplexion.z2.adaptive.AdaptiveBridge

open class AdaptiveServerPlaceholder : AdaptiveBridge<AdaptiveServerBridgeReceiver> {

    override val receiver = AdaptiveServerBridgeReceiver()

    override fun remove(child: AdaptiveBridge<AdaptiveServerBridgeReceiver>) {

    }

    override fun replace(oldChild: AdaptiveBridge<AdaptiveServerBridgeReceiver>, newChild: AdaptiveBridge<AdaptiveServerBridgeReceiver>) {

    }

    override fun add(child: AdaptiveBridge<AdaptiveServerBridgeReceiver>) {

    }

}