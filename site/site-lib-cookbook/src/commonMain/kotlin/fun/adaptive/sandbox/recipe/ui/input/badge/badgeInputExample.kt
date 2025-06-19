package `fun`.adaptive.sandbox.recipe.ui.input.badge

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.sandbox.support.examplePane
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.input.badge.badgeInput
import `fun`.adaptive.ui.input.badge.badgeInputBackend
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun badgeInputExample() : AdaptiveFragment {

    examplePane("badge input, empty, no label") {
        badgeInput(badgeInputBackend()) .. width { 300.dp }
    }

    examplePane("badge input, initialized, no label") {
        badgeInput(badgeInputBackend { inputValue = setOf("Hello", "World!")}) .. width { 300.dp }
    }

    examplePane("badge input, with label, `Hello World!` is unremovable") {
        badgeInput(
            badgeInputBackend {
                inputValue = setOf("Hello World!")
                label = "Badges"
                unremovable = setOf("Hello World!")
            }
        ) .. width { 300.dp }
    }

    return fragment()
}