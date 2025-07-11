package `fun`.adaptive.sandbox.recipe.ui.producer

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.focus
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.hover
import `fun`.adaptive.ui.api.mediaMetrics
import `fun`.adaptive.ui.api.tabIndex
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.instruction.dp

/**
 * # Focus
 *
 *
 * Produces:
 *
 * - `true` if any of the fragment descendants have focus
 * - `false` when none the fragment descendants have focus
 */
@Adaptive
fun focusBasicExample() : AdaptiveFragment {

    val focus = focus()

    column {
        box {
            tabIndex { 0 }
            if (focus) backgroundColor(0xff0000) else backgroundColor(0x00ff00)

            text("click here to get focus")
        }

        if (focus) text("focus")
    }
    return fragment()
}