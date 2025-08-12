package `fun`.adaptive.doc.example.icon

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.generated.resources.account_circle
import `fun`.adaptive.ui.generated.resources.close
import `fun`.adaptive.ui.generated.resources.success
import `fun`.adaptive.ui.icon.ActionIconRowBackend
import `fun`.adaptive.ui.icon.actionIconRow
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.snackbar.successNotification

/**
 * # Action icon row
 *
 * Use [actionIconRow](fragment://) to render a row of action icons and a "More actions" icon.
 *
 * - Priority actions are displayed as clickable icons.
 * - Additional actions are grouped under a “More actions” icon (popup menu).
 * - Selection is handled via the provided [actionHandler](property://ActionIconRowBackend) callback.
 * - When [priorityActions](property://ActionIconRowBackend) is empty, no priority icons are shown.
 * - When [otherActions](property://ActionIconRowBackend) is empty, no "More actions" icon is shown.
 */
@Adaptive
fun iconActionRowExample() : AdaptiveFragment {

    val backend = ActionIconRowBackend(
        // Priority actions: shown as icons in the row
        priorityActions = listOf(
            MenuItem(Graphics.account_circle, label = "Profile", data = Unit),
            MenuItem(Graphics.success, label = "Confirm", data = Unit),
        ),
        // Other actions: available under the popup menu
        otherActions = listOf(
            MenuItem(Graphics.close, label = "Close", data = Unit),
            MenuItem(null, label = "Disabled action", data = Unit, inactive = true),
        )
    ) {
        successNotification("Selected: ${it.label}")
    }

    actionIconRow(backend)

    return fragment()
}
