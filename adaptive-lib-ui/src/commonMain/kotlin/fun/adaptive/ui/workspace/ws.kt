package `fun`.adaptive.ui.workspace

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.workspace.WorkspaceTheme.Companion.DEFAULT
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition

@Adaptive
fun wsFull(workspace: Workspace) {
    val isFullScreen = valueFrom { workspace.isFullScreen }

    grid {
        maxSize .. if (isFullScreen) colTemplate(0.dp, 1.fr, 0.dp) else  colTemplate(DEFAULT.width, 1.fr, DEFAULT.width)

        box {
            if (! isFullScreen) {
                wsSideBarIcons(left = true, workspace)
            }
        }

        wsMain(workspace)

        box {
            if (! isFullScreen) {
                wsSideBarIcons(left = false, workspace)
            }
        }
    }
}

@Adaptive
fun wsMain(workspace: Workspace) {
    val config = valueFrom { workspace.mainSplit }

    splitPane(
        config,
        { wsTop(workspace) },
        { wsHorizontalDivider(workspace.theme) },
        { wsBottom(workspace) }
    ) .. maxSize
}

@Adaptive
private fun wsTop(workspace: Workspace) {
    val config = valueFrom { workspace.topSplit }

    splitPane(
        config,
        { wsLeft(workspace) },
        { wsVerticalDivider(workspace.theme) },
        { wsCenterRight(workspace) }
    ) .. maxSize
}

@Adaptive
private fun wsLeft(workspace: Workspace) {
    val config = valueFrom { workspace.leftSplit }

    splitPane(
        config,
        { wsPane(workspace, WsPanePosition.LeftTop) },
        { wsHorizontalDivider(workspace.theme) },
        { wsPane(workspace, WsPanePosition.LeftMiddle) }
    ) .. maxSize
}

@Adaptive
private fun wsCenterRight(workspace: Workspace) {
    val config = valueFrom { workspace.centerRightSplit }

    splitPane(
        config,
        { wsCenterPane(workspace) },
        { wsVerticalDivider(workspace.theme) },
        { wsRight(workspace) }
    ) .. maxSize
}

@Adaptive
private fun wsRight(workspace: Workspace) {
    val config = valueFrom { workspace.rightSplit }

    splitPane(
        config,
        { wsPane(workspace, WsPanePosition.RightTop) },
        { wsHorizontalDivider(workspace.theme) },
        { wsPane(workspace, WsPanePosition.RightMiddle) }
    ) .. maxSize
}

@Adaptive
private fun wsBottom(workspace: Workspace) {
    val config = valueFrom { workspace.bottomSplit }

    splitPane(
        config,
        { wsPane(workspace, WsPanePosition.LeftBottom) },
        { wsVerticalDivider(workspace.theme) },
        { wsPane(workspace, WsPanePosition.RightBottom) }
    ) .. maxSize
}

@Adaptive
private fun wsHorizontalDivider(theme: WorkspaceTheme) {
    box {
        theme.splitDividerHorizontalOverlay
        box {
            theme.splitDividerHorizontalVisible
        }
    }
}

@Adaptive
private fun wsVerticalDivider(theme: WorkspaceTheme) {
    box {
        theme.splitDividerVerticalOverlay
        box {
            theme.splitDividerVerticalVisible
        }
    }
}

@Adaptive
private fun wsPane(workspace: Workspace, position: WsPanePosition): AdaptiveFragment {

    val paneUuid = valueFrom {
        when (position) {
            WsPanePosition.RightTop -> {
                workspace.rightTop
            }

            WsPanePosition.RightMiddle -> {
                workspace.rightMiddle
            }

            WsPanePosition.RightBottom -> {
                workspace.rightBottom
            }

            WsPanePosition.LeftTop -> {
                workspace.leftTop
            }

            WsPanePosition.LeftMiddle -> {
                workspace.leftMiddle
            }

            WsPanePosition.LeftBottom -> {
                workspace.leftBottom
            }

            WsPanePosition.Center -> {
                error("center panes should not be loaded into a tool slot")
            }
        }
    }

    val pane = workspace.toolPanes.firstOrNull { it.uuid == paneUuid }

    box(instructions()) {
        maxSize
        if (pane != null) {
            wsPaneContent(pane)
        } else {
            text("no content")
        }
    }

    return fragment()
}

@Adaptive
private fun wsPaneContent(pane: WsPane<*, *>) {
    box {
        maxSize
        actualize(pane.key, emptyInstructions, pane)
    }
}

@Adaptive
fun wsSideBarIcons(
    left: Boolean,
    workspace: Workspace
) {

    val theme = workspace.theme

    val top = workspace.topActions(left)
    val middle = workspace.middlePanes(left)
    val bottom = workspace.bottomPanes(left)

    column {
        if (left) theme.leftIconColumn else theme.rightIconColumn

        column {
            for (pane in top) {
                wsSideBarIcon(pane, workspace)
            }
            if (top.isNotEmpty() && middle.isNotEmpty()) {
                box { theme.paneIconSeparator }
            }
            for (pane in middle) {
                wsSideBarIcon(pane, workspace)
            }
        }

        column {
            for (pane in bottom) {
                wsSideBarIcon(pane, workspace)
            }
        }
    }

}

@Adaptive
private fun wsSideBarIcon(
    action: AbstractSideBarAction,
    workspace: Workspace
) {

    val hover = hover()
    val theme = workspace.theme
    val activePane = valueFrom { workspace.paneStore(action.pane) }
    val focusedPane = valueFrom { workspace.focusedPane }

    val containerStyle = theme.paneIconContainer(action.pane, activePane, focusedPane, hover)

    box {
        containerStyle

        onClick { action.actionFun(workspace) }

        icon(action.icon) .. theme.paneIcon

        hoverPopup {
            popupAlign.afterCenter

            row {
                theme.tooltipContainer

                text(action.name) .. theme.tooltipName

                if (action.tooltip != null) {
                    text(action.tooltip) .. theme.tooltipShortcut
                }
            }
        }
    }
}