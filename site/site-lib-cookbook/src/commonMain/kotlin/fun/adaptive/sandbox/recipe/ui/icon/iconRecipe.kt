package `fun`.adaptive.cookbook.recipe.ui.icon

import `fun`.adaptive.cookbook.support.example
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

@Adaptive
fun iconRecipe(): AdaptiveFragment {
    column {
        maxSize .. verticalScroll

        gap { 16.dp }

        example("Icon") {
            icon(Graphics.account_circle)
        }

        example("action icon\n(move mouse over)") {
            actionIcon(Graphics.account_circle)
        }

        example("action icon with tooltip") {
            actionIcon(Graphics.account_circle, tooltip = "Tooltip")
        }

        example("action icon\nprimaryIconTheme") {
            actionIcon(Graphics.account_circle, tooltip = "Tooltip", theme = primaryIconTheme)
        }

        example("action icon\ntableIconTheme") {
            actionIcon(Graphics.account_circle, tooltip = "Tooltip", theme = tableIconTheme)
        }

        example("action icon\ndenseIconTheme") {
            actionIcon(Graphics.account_circle, tooltip = "Tooltip", theme = denseIconTheme)
        }

    }

    return fragment()
}
