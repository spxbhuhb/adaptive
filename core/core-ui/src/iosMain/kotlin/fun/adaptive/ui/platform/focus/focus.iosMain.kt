package `fun`.adaptive.ui.platform.focus

import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.AdaptiveProducer

actual fun focusImpl(binding: AdaptiveStateVariableBinding<Boolean>): AdaptiveProducer<Boolean> {
    return Focus(binding)
}