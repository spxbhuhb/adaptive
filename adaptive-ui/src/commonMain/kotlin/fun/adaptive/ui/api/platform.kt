package `fun`.adaptive.ui.api

import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.Producer
import `fun`.adaptive.ui.platform.media.MediaMetrics
import `fun`.adaptive.ui.platform.media.MediaMetricsProducer

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