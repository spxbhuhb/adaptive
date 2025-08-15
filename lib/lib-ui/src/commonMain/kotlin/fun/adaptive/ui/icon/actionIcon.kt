package `fun`.adaptive.ui.icon

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.support.UiEventHandler

@Adaptive
fun actionIcon(
    icon: GraphicsResourceSet,
    tooltip: String? = null,
    theme: IconTheme = onSurfaceIconTheme,
    actionFeedbackText: String? = null,
    actionFeedbackIcon: GraphicsResourceSet? = null,
    releaseFocus : Boolean = true,
    actionHandler: UiEventHandler? = null
): AdaptiveFragment {
    val focus = focus()
    val hover = hover()

    val border = theme.border(focus)
    val background = theme.background(hover)
    val svgColors = theme.svgColors(hover)

    box(theme.actionIconContainer, background, border) {
        if (actionHandler != null) {
            instructions().addAll(
                onClick(feedbackText = actionFeedbackText, feedbackIcon = actionFeedbackIcon) { e -> actionHandler(e); if (releaseFocus) e.releaseFocus(); },
                onEnter(feedbackText = actionFeedbackText, feedbackIcon = actionFeedbackIcon) { e -> actionHandler(e); if (releaseFocus) e.releaseFocus(); }
            )
        } else {
            instructions()
        }

        svg(icon, theme.actionIcon, svgColors, alignSelf.center)

        if (tooltip != null) {
            hoverPopup(theme.tooltip) {
                text(tooltip) .. theme.tooltipText
            }
        }
    }

    return fragment()
}