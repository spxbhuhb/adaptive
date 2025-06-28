package `fun`.adaptive.sandbox.support

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.ui.api.alignSelf
import `fun`.adaptive.ui.input.InputViewBackend
import `fun`.adaptive.ui.input.inputHint


@Adaptive
fun exampleFeedback(inputViewBackend: InputViewBackend<*,*>) : AdaptiveFragment {
    val observed = observe { inputViewBackend }
    inputHint("Value: ${observed.inputValue}") .. alignSelf.bottom
    return fragment()
}