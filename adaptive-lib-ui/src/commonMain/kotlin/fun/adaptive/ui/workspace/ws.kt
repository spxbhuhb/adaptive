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
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.workspace.WorkspaceTheme.Companion.workspaceTheme
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition

@Adaptive
fun wsFull(workspace: Workspace) {
    grid {
        maxSize .. colTemplate(workspaceTheme.width, 1.fr, workspaceTheme.width)

        wsPaneIcons(left = true, workspace)

        wsMain(workspace)

        wsPaneIcons(left = false, workspace)
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
fun wsPaneIcons(
    left: Boolean,
    workspace: Workspace
) {

    val theme = workspace.theme

    val top = if (left) {
        workspace.directPanes() + workspace.topPanes(left)
    } else {
        workspace.topPanes(left)
    }

    val middle = workspace.middlePanes(left)
    val bottom = workspace.bottomPanes(left)

    column {
        if (left) theme.leftIconColumn else theme.rightIconColumn

        column {
            for (pane in top) {
                wsPaneIcon(pane, workspace)
            }
            if (top.isNotEmpty() && middle.isNotEmpty()) {
                box { theme.paneIconSeparator }
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
    pane: WsPane<*, *>,
    workspace: Workspace
) {

    val hover = hover()
    val theme = workspace.theme
    val activePane = valueFrom { workspace.paneStore(pane) }
    val focusedPane = valueFrom { workspace.focusedPane }

    val containerStyle = theme.paneIconContainer(pane, activePane, focusedPane, hover)

    box {
        containerStyle

        onClick { workspace.toggle(pane) }

        icon(pane.icon) .. theme.paneIcon

        hoverPopup {
            popupAlign.afterCenter

            row {
                theme.tooltipContainer

                text(pane.name) .. theme.tooltipName

                if (pane.tooltip != null) {
                    text(pane.tooltip) .. theme.tooltipShortcut
                }
            }
        }
    }
}