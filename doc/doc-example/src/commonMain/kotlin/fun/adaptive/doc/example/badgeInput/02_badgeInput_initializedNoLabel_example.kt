package `fun`.adaptive.doc.example.badgeInput

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.input.badge.badgeInput
import `fun`.adaptive.ui.input.badge.badgeInputBackend
import `fun`.adaptive.ui.instruction.dp

/**
 * # Initialized input without a label
 *
 * This input is initialized with two badges: "Hello" and "World!"
 *
 * - The original (initialization) badges are removable.
 * - Type into the upper field and hit `ENTER` to add a badge.
 * - Click on the `X` to remove a badge.
 */
@Adaptive
fun badgeInputInitializedNoLabel(): AdaptiveFragment {

    val viewBackend = badgeInputBackend {
        inputValue = setOf("Hello", "World!")
    }

    badgeInput(viewBackend) .. width { 300.dp }

    return fragment()
}