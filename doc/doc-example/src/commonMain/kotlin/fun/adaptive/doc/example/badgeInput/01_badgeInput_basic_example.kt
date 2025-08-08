package `fun`.adaptive.doc.example.badgeInput

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.input.badge.badgeInput
import `fun`.adaptive.ui.input.badge.badgeInputBackend
import `fun`.adaptive.ui.instruction.dp

/**
 * # Empty input without a label
 *
 * - Type into the upper field and hit `ENTER` to add a badge.
 * - Click on the `X` to remove a badge.
 */
@Adaptive
fun badgeBasicInput(): AdaptiveFragment {

    val viewBackend = badgeInputBackend()

    badgeInput(viewBackend) .. width { 300.dp }

    return fragment()
}