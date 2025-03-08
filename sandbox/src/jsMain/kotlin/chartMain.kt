
import `fun`.adaptive.chart.ws.WsChartContext
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.builtin.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.workspace.*
import `fun`.adaptive.ui.workspace.model.WorkspaceTheme.Companion.workspaceTheme
import `fun`.adaptive.ui.workspace.model.Workspace
import `fun`.adaptive.ui.workspace.model.WorkspacePane
import `fun`.adaptive.ui.workspace.model.WorkspacePanePosition
import `fun`.adaptive.utility.UUID

class Context {
    val someData = "Some data from the context"
}

@Adaptive
fun chartMain(): AdaptiveFragment {

    val workspace = Workspace()
    initPanes(workspace)

    box {
        maxSize .. backgrounds.friendlyOpaque .. padding { 16.dp }

        grid {
            maxSize .. colTemplate(workspaceTheme.width, 1.fr, workspaceTheme.width)
            borders.outline .. backgrounds.surface

            localContext(Context()) {

                wsMain(workspace)

                wsPaneIcons(left = false, workspace)
            }
        }
    }

    return fragment()
}

fun initPanes(workspace: Workspace) {
    workspace.toolPanes.addAll(
        listOf(
            WorkspacePane(
                UUID(),
                "Chart",
                Graphics.menu,
                WorkspacePanePosition.RightTop,
                WsChartContext.CHART_TOOL_PANE_KEY
            ),
            WorkspacePane(
                UUID(),
                "Center",
                Graphics.menu,
                WorkspacePanePosition.Center,
                WsChartContext.CHART_CONTENT_PANE_KEY
            ),
        )
    )

    workspace.center.value = workspace.toolPanes.first { it.position == WorkspacePanePosition.Center }.uuid
}