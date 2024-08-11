/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.common.platform

import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.AdaptiveProducer
import `fun`.adaptive.foundation.producer.Producer
import `fun`.adaptive.resource.ThemeQualifier
import `fun`.adaptive.ui.common.AbstractCommonAdapter

/**
 * Contains information about the current media.
 */
data class MediaMetrics(
    val viewWidth: Double,
    val viewHeight: Double,
    val theme: ThemeQualifier,
    val manualTheme: ThemeQualifier?
) {
    val isSmall
        get() = viewWidth < 600.0 || viewHeight < 600.0

    val isMedium
        get() = ! isSmall && ! isLarge

    val isLarge
        get() = viewWidth > 1024.0 || viewHeight > 1024.0

    val isLight
        get() = (manualTheme == ThemeQualifier.LIGHT || (manualTheme == null && theme == ThemeQualifier.LIGHT))

    val isDark
        get() = (manualTheme == ThemeQualifier.DARK || (manualTheme == null && theme == ThemeQualifier.DARK))

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

    binding.targetFragment.addProducer(metrics)

    return metrics.latestValue !!
}

class MediaMetricsProducer(
    override val binding: AdaptiveStateVariableBinding<MediaMetrics>
) : AdaptiveProducer<MediaMetrics> {

    var uiAdapter = binding.targetFragment.adapter as AbstractCommonAdapter<*, *>

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
