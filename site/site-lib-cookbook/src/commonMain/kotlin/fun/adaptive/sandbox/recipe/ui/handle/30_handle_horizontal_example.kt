package `fun`.adaptive.sandbox.recipe.ui.handle

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.container.editableList
import `fun`.adaptive.ui.handle.handle
import `fun`.adaptive.ui.input.select.SelectInputTheme
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.ui.instruction.layout.SizeStrategy
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.textColors

/**
 * # Horizontal movement only
 *
 * Keep the vertical position static to allow only horizontal movement.
 */
@Adaptive
fun handleHorizonalOnlyExample(): AdaptiveFragment {

    var position = Position(
        top = 0.dp,
        left = 0.dp
    )

    val constraints = SizeStrategy(
        minWidth = 0.dp,
        maxWidth = 100.dp,
        minHeight = 0.dp,
        maxHeight = 0.dp
    )

    box {
        // height and with have to count for surrounding as well
        height { 22.dp } .. width { 122.dp }  .. borders.outline

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