package `fun`.adaptive.ui.api

import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.Producer
import `fun`.adaptive.ui.platform.focus.focusImpl
import `fun`.adaptive.ui.platform.hover.hoverImpl
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

/**
 * Produces:
 *
 * - `true` if the mouse hovers over any the fragment descendants
 * - `false` when the mouse is outside all the fragment descendants
 */
@Producer
fun hover(
    binding: AdaptiveStateVariableBinding<Boolean>? = null,
): Boolean {
    checkNotNull(binding)

    val hover = hoverImpl(binding)

    binding.targetFragment.addProducer(hover)

    return hover.latestValue !!

}


/**
 * Produces:
 *
 * - `true` if any of the fragment descendants have focus
 * - `false` when none the fragment descendants have focus
 */
@Producer
fun focus(
    binding: AdaptiveStateVariableBinding<Boolean>? = null,
): Boolean {
    checkNotNull(binding)

    val focus = focusImpl(binding)

    binding.targetFragment.addProducer(focus)

    return focus.latestValue !!

}