package `fun`.adaptive.ui.api

import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.Producer
import `fun`.adaptive.ui.platform.Hover

/**
 * Produces:
 *
 * - `true` if the mouse hovers over any of the children of the fragment
 * - `false` when the mouse is outside all children of the fragment
 */
@Producer
fun hover(
    binding: AdaptiveStateVariableBinding<Boolean>? = null,
): Boolean {
    checkNotNull(binding)

    val hover = Hover(binding)

    binding.targetFragment.addProducer(hover)

    return hover.latestValue !!

}