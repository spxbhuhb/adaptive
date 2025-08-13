package `fun`.adaptive.ui.icon

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.generated.resources.actions
import `fun`.adaptive.ui.generated.resources.error
import `fun`.adaptive.ui.generated.resources.moreActions
import `fun`.adaptive.ui.generated.resources.more_vert
import `fun`.adaptive.ui.menu.*

/**
 * Shows [priorityActions](property://ActionIconRowBackend) in a row, followed by a "More actions" icon which opens a
 * [contextMenu](fragment://) on click with [otherActions](property://ActionIconRowBackend).
 *
 * When both lists are empty, nothing is shown.
 *
 * When [otherActions](property://ActionIconRowBackend) is empty, the "More actions" icon is hidden.
 */
@Adaptive
fun <T> actionIconRow(
    backend : ActionIconRowBackend<T>,
) : AdaptiveFragment {

    if (backend.isNotEmpty()) {

        row(instructions()) {
            for (action in backend.priorityActions.mapNotNull { it as? MenuItem<T> }) {
                if (action.isVisible(fragment())) {
                    actionIcon(
                        icon = action.icon ?: Graphics.error,
                        tooltip = action.label,
                        theme = denseIconTheme,
                        actionHandler = { backend.actionHandler(action) }
                    )
                }
            }

            if (backend.otherActions.any { it.isVisible(fragment()) }) {
                withPrimaryMenu(backend.menuBackend) {
                    actionIcon(Graphics.more_vert, Strings.moreActions, theme = denseIconTheme)
                }
            }
        }

    }

    return fragment()
}