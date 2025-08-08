package `fun`.adaptive.doc.example.handle

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.handle.handle
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.ui.theme.backgrounds

/**
 * # Basic handle
 *
 * Use the [handle](fragment://) fragment to create a draggable handle.
 *
 * As the user moves the handle, `moveFun` is called, which in turn updated the position.
 *
 * > [!IMPORTANT]
 * >
 * > 1. You must put the handle into a box to let it move, otherwise the layout overrides the position.
 * > 2. Without restricting the position the handle **CAN MOVE OUT** of the box.
 * >
 */
@Adaptive
fun handleBasicExample(): AdaptiveFragment {

    var position = Position(
        top = 0.dp,
        left = 0.dp
    )

    box {
        handle(
            position = position,
            moveFun = { toPosition -> position = toPosition }
        ) {
            box {
                size(20.dp) .. backgrounds.friendly
            }
        }
    }

    return fragment()
}