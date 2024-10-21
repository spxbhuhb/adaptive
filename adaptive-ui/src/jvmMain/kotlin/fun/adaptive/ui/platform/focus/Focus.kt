package `fun`.adaptive.ui.platform.focus

import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.AdaptiveProducer

/**
 * Focus on mobile could be implemented I guess. For now,
 * I'll just use this dummy class.
 */
class Focus(
    override val binding: AdaptiveStateVariableBinding<Boolean>,
) : AdaptiveProducer<Boolean> {

    override var latestValue: Boolean? = false

    override val actual: Boolean
        get() = true

    override fun start() {

    }

    override fun stop() {

    }

}