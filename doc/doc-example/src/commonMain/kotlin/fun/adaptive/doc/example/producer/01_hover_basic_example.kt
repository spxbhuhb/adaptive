package `fun`.adaptive.doc.example.producer

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*

/**
 * # Hover
 *
 * Produces:
 *
 * - `true` if the mouse hovers over any the fragment descendants
 * - `false` when the mouse is outside all the fragment descendants
 */
@Adaptive
fun hoverBasicExample() : AdaptiveFragment{
    val hover = hover()

    column {
        box {
            if (hover) backgroundColor(0xff0000) else backgroundColor(0x00ff00)
            text("move here to get hover")
        }

        if (hover) text("hover")
    }

    return fragment()
}