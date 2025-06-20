package `fun`.adaptive.ui.mpw.fragments

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.splitpane.horizontalSplitDivider
import `fun`.adaptive.ui.splitpane.verticalSplitDivider
import `fun`.adaptive.ui.mpw.AbstractSideBarAction
import `fun`.adaptive.ui.mpw.MultiPaneTheme.Companion.DEFAULT
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.backends.PaneViewBackend
import `fun`.adaptive.ui.mpw.model.PanePosition
import `fun`.adaptive.utility.UUID

@Adaptive
fun multiPaneWorkspace(workspace: MultiPaneWorkspace) {
    val isFullScreen = observe { workspace.isFullScreen }

    grid {
        maxSize .. if (isFullScreen) colTemplate(0.dp, 1.fr, 0.dp) else  colTemplate(DEFAULT.width, 1.fr, DEFAULT.width)

        box {
            if (! isFullScreen) {
                multiPaneWorkspaceSideBarIcons(left = true, workspace)
            }
        }

        multiPaneWorkspaceMain(workspace)

        box {
            if (! isFullScreen) {
                multiPaneWorkspaceSideBarIcons(left = false, workspace)
            }
        }
    }
}

@Adaptive
fun multiPaneWorkspaceMain(workspace: MultiPaneWorkspace) {
    val config = observe { workspace.mainSplit }

    splitPane(
        config,
        { mpTop(workspace) },
        { horizontalSplitDivider(workspace.theme.splitPaneTheme) },
        { mpBottom(workspace) }
    ) .. maxSize
}

@Adaptive
private fun mpTop(workspace: MultiPaneWorkspace) {
    val config = observe { workspace.topSplit }

    splitPane(
        config,
        { mpLeft(workspace) },
        { verticalSplitDivider(workspace.theme.splitPaneTheme) },
        { mpCenterRight(workspace) }
    ) .. maxSize
}

@Adaptive
private fun mpLeft(workspace: MultiPaneWorkspace) {
    val config = observe { workspace.leftSplit }

    splitPane(
        config,
        { mpPane(workspace, PanePosition.LeftTop) },
        { horizontalSplitDivider(workspace.theme.splitPaneTheme) },
        { mpPane(workspace, PanePosition.LeftMiddle) }
    ) .. maxSize
}

@Adaptive
private fun mpCenterRight(workspace: MultiPaneWorkspace) {
    val config = observe { workspace.centerRightSplit }

    splitPane(
        config,
        { centerPane(workspace) },
        { verticalSplitDivider(workspace.theme.splitPaneTheme) },
        { mpRight(workspace) }
    ) .. maxSize
}

@Adaptive
private fun mpRight(workspace: MultiPaneWorkspace) {
    val config = observe { workspace.rightSplit }

    splitPane(
        config,
        { mpPane(workspace, PanePosition.RightTop) },
        { horizontalSplitDivider(workspace.theme.splitPaneTheme) },
        { mpPane(workspace, PanePosition.RightMiddle) }
    ) .. maxSize
}

@Adaptive
private fun mpBottom(workspace: MultiPaneWorkspace) {
    val config = observe { workspace.bottomSplit }

    splitPane(
        config,
        { mpPane(workspace, PanePosition.LeftBottom) },
        { verticalSplitDivider(workspace.theme.splitPaneTheme) },
        { mpPane(workspace, PanePosition.RightBottom) }
    ) .. maxSize
}

@Adaptive
private fun mpPane(workspace: MultiPaneWorkspace, position: PanePosition): AdaptiveFragment {

    val paneUuid = observe {
        when (position) {
            PanePosition.RightTop -> {
                workspace.rightTop
            }

            PanePosition.RightMiddle -> {
                workspace.rightMiddle
            }

            PanePosition.RightBottom -> {
                workspace.rightBottom
            }

            PanePosition.LeftTop -> {
                workspace.leftTop
            }

            PanePosition.LeftMiddle -> {
                workspace.leftMiddle
            }

            PanePosition.LeftBottom -> {
                workspace.leftBottom
            }

            PanePosition.Center -> {
                error("center panes should not be loaded into a tool slot")
            }
        }
    }

    val pane = workspace.toolPanes.firstOrNull { it.uuid == paneUuid }

    box(instructions()) {
        maxSize
        if (pane != null) {
            mpPaneContent(pane)
        } else {
            text("no content")
        }
    }

    return fragment()
}

@Adaptive
private fun mpPaneContent(pane: PaneViewBackend<*>) {
    box {
        maxSize
        localContext(pane) {
            actualize(pane.paneDef.fragmentKey, null, emptyInstructions, pane)
        }
    }
}

@Adaptive
fun multiPaneWorkspaceSideBarIcons(
    left: Boolean,
    workspace: MultiPaneWorkspace
) {

    val theme = workspace.theme

    val top = workspace.topActions(left)
    val middle = workspace.middlePanes(left)
    val bottom = workspace.bottomPanes(left)

    column {
        if (left) theme.leftIconColumn else theme.rightIconColumn

        column {
            for (pane in top) {
                mpSideBarIcon(pane, workspace)
            }
            if (top.isNotEmpty() && middle.isNotEmpty()) {
                box { theme.paneIconSeparator }
            }
            for (pane in middle) {
                mpSideBarIcon(pane, workspace)
            }
        }

        column {
            for (pane in bottom) {
                mpSideBarIcon(pane, workspace)
            }
        }
    }

}

@Adaptive
private fun mpSideBarIcon(
    action: AbstractSideBarAction,
    workspace: MultiPaneWorkspace
) {

    val hover = hover()
    val theme = workspace.theme
//  FIXME  val activePane = valueFrom { workspace.paneStore(action.pane) }
    val focusedPane = observe { workspace.focusedPane }

    val containerStyle = theme.paneIconContainer(UUID.nil(), UUID.nil(), focusedPane, hover)

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