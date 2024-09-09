package `fun`.adaptive.ui.platform.hover

import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.AdaptiveProducer

expect fun hoverImpl(binding: AdaptiveStateVariableBinding<Boolean>): AdaptiveProducer<Boolean>