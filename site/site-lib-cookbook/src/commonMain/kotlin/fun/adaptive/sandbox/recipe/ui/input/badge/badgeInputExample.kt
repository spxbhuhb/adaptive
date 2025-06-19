package `fun`.adaptive.sandbox.recipe.ui.input.badge

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.sandbox.support.examplePane
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.input.badge.badgeInput
import `fun`.adaptive.ui.input.badge.badgeInputBackend
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun badgeInputExample(): AdaptiveFragment {

    column {
        gap { 24.dp }

        examplePane(
            "Empty input without label",
            """
            * Type into the upper field and hit `ENTER` to add a badge.
            * Click on the `X` to remove a badge.
        """.trimIndent()
        ) {
            badgeInput(badgeInputBackend()) .. width { 300.dp }
        }

        examplePane(
            "Initialized input without label",
            """
            This input is initialized with two badges: "Hello" and "World!"
            * The original (initialization) badges are removable.
            * Type into the upper field and hit enter to add a badge.
            * Click on the `X` to remove a badge.
        """.trimIndent()
        ) {
            badgeInput(badgeInputBackend { inputValue = setOf("Hello", "World!") }) .. width { 300.dp }
        }

        examplePane(
            "Initialized input with label and with an unremovable badge",
            """
            This input is initialized with two badges: "Hello" and "World!"
            * You can remove "World!", but not "Hello".
        """.trimIndent()
        ) {
            badgeInput(
                badgeInputBackend {
                    inputValue = setOf("Hello", "World!")
                    label = "Badges"
                    unremovable = setOf("Hello")
                }
            ) .. width { 300.dp }
        }
    }

    return fragment()
}