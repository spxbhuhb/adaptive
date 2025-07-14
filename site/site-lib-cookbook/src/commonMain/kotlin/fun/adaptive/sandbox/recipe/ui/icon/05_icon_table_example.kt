package `fun`.adaptive.sandbox.recipe.ui.icon

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.generated.resources.account_circle
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.tableIconTheme

/**
 * # Table icon theme
 *
 * Use `tableIconTheme` to show an icon in table rows.
 */
@Adaptive
fun iconTableExample(): AdaptiveFragment {
    actionIcon(Graphics.account_circle, theme = tableIconTheme)

    return fragment()
}
