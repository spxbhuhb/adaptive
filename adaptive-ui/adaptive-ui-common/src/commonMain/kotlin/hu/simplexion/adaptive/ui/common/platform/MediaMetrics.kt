/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.platform

import hu.simplexion.adaptive.foundation.binding.AdaptiveStateVariableBinding
import hu.simplexion.adaptive.foundation.producer.AdaptiveProducer
import hu.simplexion.adaptive.foundation.producer.Producer
import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter

/**
 * Contains information about the current media.
 */
class MediaMetrics(
    val viewWidth: Double,
    val viewHeight: Double,
) {
    val isSmall
        get() = viewWidth < 600.0 || viewHeight < 600.0

    val isMedium
        get() = ! isSmall && ! isLarge

    val isLarge
        get() = viewWidth > 1024.0 || viewHeight > 1024.0
}

/**
 * Set the value of the given state variable to the current media metrics.
 * If the metrics change (for example, the browser window is resized), updates
 * the state variable automatically according to the rules provided.
 */
@Producer
fun mediaMetrics(
    binding: AdaptiveStateVariableBinding<MediaMetrics>? = null,
): MediaMetrics {
    checkNotNull(binding)

    val metrics = MediaMetricsProducer(binding)

    binding.sourceFragment.addProducer(metrics)

    return metrics.latestValue
}

class MediaMetricsProducer(
    val binding: AdaptiveStateVariableBinding<MediaMetrics>
) : AdaptiveProducer {

    var uiAdapter = binding.sourceFragment.adapter as AbstractCommonAdapter<*, *>

    var latestValue = uiAdapter.mediaMetrics

    override fun replaces(other: AdaptiveProducer): Boolean =
        other is MediaMetricsProducer && other.binding == this.binding

    override fun start() {
        val adapter = binding.sourceFragment.adapter as AbstractCommonAdapter<*, *>
        adapter.addMediaMetricsProducer(this)
    }

    override fun stop() {
        val adapter = binding.sourceFragment.adapter as AbstractCommonAdapter<*, *>
        adapter.removeMediaMetricsProducer(this)
    }

    override fun hasValueFor(stateVariableIndex: Int) =
        binding.indexInTargetState == stateVariableIndex

    override fun value() =
        latestValue

    override fun toString(): String {
        return "MediaMetrics($binding)"
    }

    fun update(metrics: MediaMetrics) {
        latestValue = metrics
        binding.targetFragment.setDirty(binding.indexInTargetState, true)
    }
}
