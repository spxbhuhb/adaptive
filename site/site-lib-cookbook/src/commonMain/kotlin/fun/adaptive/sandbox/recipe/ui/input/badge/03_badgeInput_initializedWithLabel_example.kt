package `fun`.adaptive.sandbox.recipe.ui.input.badge

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.input.badge.badgeInput
import `fun`.adaptive.ui.input.badge.badgeInputBackend
import `fun`.adaptive.ui.instruction.dp

/**
 * # Set a badge unremovable
 *
 * - This input is initialized with two badges: "Hello" and "World!"
 * - You can remove "World!", but not "Hello".
 * - Set `unremovable` to make some badges unremovable.
 */
@Adaptive
fun badgeInputInitializedUnremovable(): AdaptiveFragment {

    val viewBackend = badgeInputBackend {
        inputValue = setOf("Hello", "World!")
        label = "Badges"
        unremovable = setOf("Hello")
    }

    badgeInput(viewBackend) .. width { 300.dp }

    return fragment()
}