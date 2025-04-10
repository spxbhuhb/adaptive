package `fun`.adaptive.ui.platform.hover

import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.AdaptiveProducer

/**
 * Hover on mobile could be implemented with pen I guess. For now,
 * I'll just use this dummy class.
 */
class Hover(
    override val binding: AdaptiveStateVariableBinding<Boolean>
) : AdaptiveProducer<Boolean> {

    override var latestValue: Boolean? = false

    override val actual: Boolean
        get() = true

    override fun start() {

    }

    override fun stop() {

    }

}