package `fun`.adaptive.ui.workspace

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors

@Adaptive
fun wsMain(workspace: Workspace) {
    val config = valueFrom { workspace.mainSplit }

    splitPane(
        config,
        { wsTop(workspace) },
        { box { maxWidth .. height { 4.dp } .. backgrounds.friendly } },
        { wsBottom(workspace) }
    ) .. maxSize
}

@Adaptive
private fun wsTop(workspace: Workspace) {
    val config = valueFrom { workspace.topSplit }

    splitPane(
        config,
        { wsLeft(workspace) },
        { box { maxHeight .. width { 4.dp } .. backgrounds.friendly } },
        { wsCenterRight(workspace) }
    ) .. maxSize
}

@Adaptive
private fun wsLeft(workspace: Workspace) {
    val config = valueFrom { workspace.leftSplit }

    splitPane(
        config,
        { wsPane(workspace, WorkspacePanePosition.LeftTop) },
        { box { maxWidth .. height { 4.dp } .. backgrounds.friendly } },
        { wsPane(workspace, WorkspacePanePosition.LeftMiddle) }
    ) .. maxSize
}

@Adaptive
private fun wsCenterRight(workspace: Workspace) {
    val config = valueFrom { workspace.centerRightSplit }

    splitPane(
        config,
        { wsPane(workspace, WorkspacePanePosition.Center) },
        { box { maxSize .. backgrounds.friendly } },
        { wsRight(workspace) }
    ) .. maxSize
}

@Adaptive
private fun wsRight(workspace: Workspace) {
    val config = valueFrom { workspace.rightSplit }

    splitPane(
        config,
        { wsPane(workspace, WorkspacePanePosition.RightTop) },
        { box { maxSize .. backgrounds.friendly } },
        { wsPane(workspace, WorkspacePanePosition.RightMiddle) }
    ) .. maxSize
}

@Adaptive
private fun wsBottom(workspace: Workspace) {
    val config = valueFrom { workspace.bottomSplit }

    splitPane(
        config,
        { wsPane(workspace, WorkspacePanePosition.LeftBottom) },
        { box { maxSize .. backgrounds.friendly } },
        { wsPane(workspace, WorkspacePanePosition.RightBottom) }
    ) .. maxSize
}

@Adaptive
private fun wsPane(workspace: Workspace, position: WorkspacePanePosition): AdaptiveFragment {

    val paneUuid = valueFrom {
        when (position) {
            WorkspacePanePosition.RightTop ->  { workspace.rightTop }
            WorkspacePanePosition.RightMiddle ->  { workspace.rightMiddle }
            WorkspacePanePosition.RightBottom ->  { workspace.rightBottom }
            WorkspacePanePosition.LeftTop ->  { workspace.leftTop }
            WorkspacePanePosition.LeftMiddle ->  { workspace.leftMiddle }
            WorkspacePanePosition.LeftBottom ->  { workspace.leftBottom }
            WorkspacePanePosition.Center -> { workspace.center }
        }
    }

    val pane = workspace.panes.firstOrNull { it.uuid == paneUuid }

    box(instructions()) {
        maxSize .. backgroundColor { colors.onSurfaceFriendly.opaque(0.2f) }
        if (pane != null) {
            wsPaneContent(pane)
        } else {
            text("no content")
        }
    }

    return fragment()
}

@Adaptive
private fun wsPaneContent(pane : WorkspacePane) {
    box {
        maxSize
        actualize(pane.key)
    }
}

@Adaptive
fun wsPaneIcons(
    left: Boolean,
    workspace: Workspace
) {

    val theme = workspace.theme
    val top = workspace.topPanes(left)
    val middle = workspace.middlePanes(left)
    val bottom = workspace.bottomPanes(left)

    column {
        if (left) theme.leftIconColumn else theme.rightIconColumn

        column {
            for (pane in top) {
                wsPaneIcon(pane, workspace)
            }
            if (top.isNotEmpty() && middle.isNotEmpty()) {
                box { theme.divider }
            }
            for (pane in middle) {
                wsPaneIcon(pane, workspace)
            }
        }

        column {
            for (pane in bottom) {
                wsPaneIcon(pane, workspace)
            }
        }
    }

}

@Adaptive
private fun wsPaneIcon(
    pane: WorkspacePane,
    workspace: Workspace
) {

    val theme = workspace.theme

    box {
        theme.paneIconContainer
        onClick { workspace.onIconClick(pane) }

        icon(pane.icon)

        hoverPopup {
            popupAlign.afterCenter

            row {
                theme.tooltipContainer

                text(pane.name) .. theme.tooltipName

                if (pane.shortcut != null) {
                    text(pane.shortcut) .. theme.tooltipShortcut
                }
            }
        }
    }
}