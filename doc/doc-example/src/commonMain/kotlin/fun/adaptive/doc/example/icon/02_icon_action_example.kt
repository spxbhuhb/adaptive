package `fun`.adaptive.doc.example.icon

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.generated.resources.account_circle
import `fun`.adaptive.ui.generated.resources.success
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.snackbar.successNotification

/**
 * # Action icon
 *
 * Use [actionIcon](fragment://) for an icon that:
 *
 * - executes an action on click / on `Enter`
 * - may have a tooltip to describe the action (optional)
 * - changes background on hover
 * - focusable, adds border on focus
 *
 * Parameters for the action (all are optional):
 *
 * - [actionHandler](parameter://actionIcon)
 * - [actionFeedbackText](parameter://actionIcon)
 * - [actionFeedbackIcon](parameter://actionIcon)
 */
@Adaptive
fun iconActionExample(): AdaptiveFragment {
    actionIcon(
        Graphics.account_circle,
        tooltip = "Tooltip",
        actionFeedbackText = "Action feedback",
        actionFeedbackIcon = Graphics.success,
    ) {
        successNotification("Action executed")
    }

    return fragment()
}
