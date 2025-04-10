package `fun`.adaptive.ui.platform.focus

import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.AdaptiveProducer

expect fun focusImpl(binding: AdaptiveStateVariableBinding<Boolean>): AdaptiveProducer<Boolean>