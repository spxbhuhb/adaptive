/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.support.navigation

import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.AdaptiveProducer
import `fun`.adaptive.foundation.producer.Producer
import `fun`.adaptive.ui.AbstractAuiAdapter

@Producer
fun navSegment(
    binding: AdaptiveStateVariableBinding<String>? = null,
): String {
    checkNotNull(binding)

    val producer = NavSegmentProducer(binding)

    binding.targetFragment.addProducer(producer)

    return producer.latestValue !!
}

class NavSegmentProducer(
    override val binding: AdaptiveStateVariableBinding<String>
) : AdaptiveProducer<String> {

    var uiAdapter = binding.targetFragment.adapter as AbstractAuiAdapter<*, *>

    override var latestValue: String? = ""

    override fun start() {
        latestValue = uiAdapter.navSupport.consume(binding.targetFragment)
    }

    override fun stop() {
        uiAdapter.navSupport.removeNode(binding.targetFragment)
    }

    override fun toString(): String {
        return "NavSegment($binding)"
    }

}
