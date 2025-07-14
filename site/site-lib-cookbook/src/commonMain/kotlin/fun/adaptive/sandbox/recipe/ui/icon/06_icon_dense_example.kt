package `fun`.adaptive.sandbox.recipe.ui.icon

import `fun`.adaptive.sandbox.support.example
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.generated.resources.account_circle
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.denseIconTheme
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.icon.primaryIconTheme
import `fun`.adaptive.ui.icon.tableIconTheme
import `fun`.adaptive.ui.instruction.dp

/**
 * # Dense theme
 *
 * Use `denseIconTheme` to show a smaller action icon.
 */
@Adaptive
fun iconDenseExample(): AdaptiveFragment {

    actionIcon(Graphics.account_circle, theme = denseIconTheme)

    return fragment()
}
