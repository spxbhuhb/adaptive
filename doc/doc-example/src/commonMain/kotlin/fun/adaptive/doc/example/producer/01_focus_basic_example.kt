package `fun`.adaptive.doc.example.producer

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*

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