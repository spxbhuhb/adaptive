package `fun`.adaptive.sandbox.recipe.ui.icon

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.generated.resources.account_circle
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.primaryIconTheme

/**
 * # Primary icon theme
 *
 * Use `primaryIconTheme` to show an icon with the primary color.
 */
@Adaptive
fun iconPrimaryExample(): AdaptiveFragment {

    actionIcon(Graphics.account_circle, theme = primaryIconTheme)

    return fragment()
}
