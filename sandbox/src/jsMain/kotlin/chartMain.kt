
import `fun`.adaptive.chart.chartCommon
import `fun`.adaptive.chart.ws.model.WsChartContext
import `fun`.adaptive.chart.ws.model.WsItemChartSeries
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.WorkspaceTheme.Companion.workspaceTheme
import `fun`.adaptive.ui.workspace.wsMain
import `fun`.adaptive.ui.workspace.wsSideBarIcons
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.firstInstanceOrNull

class Context {
    val someData = "Some data from the context"
}

@Adaptive
fun chartMain(): AdaptiveFragment {

    val workspace = Workspace(adapter().backend)
    workspace.chartCommon()
    initCharts(workspace)

    box {
        maxSize .. backgrounds.friendlyOpaque .. padding { 16.dp }

        grid {
            maxSize .. colTemplate(1.fr, workspaceTheme.width)
            borders.outline .. backgrounds.surface

            localContext(workspace) {

                wsMain(workspace)

                wsSideBarIcons(left = false, workspace)
            }
        }
    }

    return fragment()
}

fun initCharts(workspace: Workspace) {

    val chartContext = workspace.contexts.firstInstanceOrNull<WsChartContext>() ?: return

    chartContext.items += WsItemChartSeries(
        "Series 1",
        WsChartContext.WSIT_CHART_SERIES,
        UUID()
    )

   //workspace.center.value = workspace.toolPanes.first { it.position == WorkspacePanePosition.Center }.uuid
}