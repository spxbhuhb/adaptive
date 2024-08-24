/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.platform.media

import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.AdaptiveProducer
import `fun`.adaptive.ui.AbstractAuiAdapter

class MediaMetricsProducer(
    override val binding: AdaptiveStateVariableBinding<MediaMetrics>
) : AdaptiveProducer<MediaMetrics> {

    var uiAdapter = binding.targetFragment.adapter as AbstractAuiAdapter<*, *>

    override var latestValue: MediaMetrics? = uiAdapter.mediaMetrics

    override fun start() {
        uiAdapter.addMediaMetricsProducer(this)
    }

    override fun stop() {
        uiAdapter.removeMediaMetricsProducer(this)
    }

    override fun toString(): String {
        return "MediaMetrics($binding)"
    }

    fun update(metrics: MediaMetrics) {
        latestValue = metrics
        binding.targetFragment.setDirty(binding.indexInTargetState, true)
    }
}
