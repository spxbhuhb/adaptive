package `fun`.adaptive.ui.popup

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.icon.icon

@Adaptive
fun feedbackPopupContent(
    feedbackText: String?,
    feedbackIcon: GraphicsResourceSet?
): AdaptiveFragment {
    row {
        PopupTheme.default.feedbackContainer

        if (feedbackText != null) {
            text(feedbackText) .. PopupTheme.default.feedbackText

        }
        if (feedbackIcon != null) {
            icon(feedbackIcon)
        }
    }

    return fragment()
}