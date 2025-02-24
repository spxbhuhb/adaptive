package `fun`.adaptive.ui.workspace

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.hoverPopup
import `fun`.adaptive.ui.api.maxHeight
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.popupAlign
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.splitPane
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.width
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
private fun wsPane(workspace: Workspace, position: WorkspacePanePosition, vararg instructions: AdaptiveInstruction): AdaptiveFragment {

    val panelUuid = valueFrom {
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

    val panel = workspace.panes.firstOrNull { it.uuid == panelUuid }

    box(instructions()) {
        maxSize .. backgroundColor { colors.onSurfaceFriendly.opaque(0.2f) }
        if (panel != null) {
            wsPaneContent(panel._fixme_adaptive_content)
        } else {
            text("no content")
        }
    }

    return fragment()
}

@Adaptive
private fun wsPaneContent(@Adaptive content: () -> AdaptiveFragment) {
    box {
        maxSize
        content()
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
            for (panel in top) {
                wsPanelIcon(panel, workspace)
            }
            if (top.isNotEmpty() && middle.isNotEmpty()) {
                box { theme.divider }
            }
            for (panel in middle) {
                wsPanelIcon(panel, workspace)
            }
        }

        column {
            for (panel in bottom) {
                wsPanelIcon(panel, workspace)
            }
        }
    }

}

@Adaptive
private fun wsPanelIcon(
    panel: WorkspacePane,
    workspace: Workspace
) {

    val theme = workspace.theme

    box {
        theme.panelIconContainer
        onClick { workspace.onIconClick(panel) }

        icon(panel.icon)

        hoverPopup {
            popupAlign.afterCenter

            row {
                theme.tooltipContainer

                text(panel.name) .. theme.tooltipName

                if (panel.shortcut != null) {
                    text(panel.shortcut) .. theme.tooltipShortcut
                }
            }
        }
    }
}