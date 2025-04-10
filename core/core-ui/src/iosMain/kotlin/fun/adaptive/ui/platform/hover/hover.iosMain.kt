package `fun`.adaptive.ui.platform.hover

import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.AdaptiveProducer

actual fun hoverImpl(binding: AdaptiveStateVariableBinding<Boolean>): AdaptiveProducer<Boolean> {
    return Hover(binding)
}