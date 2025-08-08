package `fun`.adaptive.doc.example.handle

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.handle.handle
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.ui.instruction.layout.SizeStrategy
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders

/**
 * # Limit available positions
 *
 * Limit the actual position by adding logic to `moveFun`.
 *
 * This example limits the movements of the handle inside the box by coercing
 * the position into the given limits.
 *
 * Note the connection between the handle size (20.dp), the constraint (100.dp) and
 * the containing box size (122.dp). If you want to keep your handle inside the box
 * you must get these numbers right.
 */
@Adaptive
fun handleBoxedExample(): AdaptiveFragment {

    var position = Position(
        top = 0.dp,
        left = 0.dp
    )

    val constraints = SizeStrategy(
        minWidth = 0.dp,
        maxWidth = 100.dp,
        minHeight = 0.dp,
        maxHeight = 100.dp
    )

    box {
        // size has to count for surrounding as well
        size(122.dp) .. borders.outline

        handle(
            position = position,
            moveFun = { toPosition -> position = toPosition.coerce(constraints) }
        ) {
            box {
                size(20.dp) .. backgrounds.friendly
            }
        }
    }

    return fragment()
}