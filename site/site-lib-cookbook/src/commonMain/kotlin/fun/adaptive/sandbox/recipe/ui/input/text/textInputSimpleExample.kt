package `fun`.adaptive.sandbox.recipe.ui.input.text

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.input.text.textInput2
import `fun`.adaptive.ui.input.text.textInputBackend
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun textInputSimpleExample(): AdaptiveFragment {
    val backend = textInputBackend("Hello World!") {}
    val observed = observe { backend }

    row {
        gap { 16.dp }
        textInput2(backend) .. width { 200.dp }
        text("Input value: ${observed.inputValue}")
    }

    return fragment()
}
