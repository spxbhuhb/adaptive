package `fun`.adaptive.ui.workspace

import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.general.Observable
import `fun`.adaptive.ui.fragment.layout.SplitPaneConfiguration
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Orientation
import `fun`.adaptive.ui.instruction.layout.SplitMethod
import `fun`.adaptive.ui.instruction.layout.SplitVisibility
import `fun`.adaptive.ui.workspace.WorkspaceTheme.Companion.workspaceTheme
import `fun`.adaptive.utility.UUID

class Workspace {

    val theme
        get() = workspaceTheme

    val panes = mutableListOf<WorkspacePane>()

    val rightTop = storeFor<UUID<WorkspacePane>> { UUID.nil<WorkspacePane>() }
    val rightMiddle = storeFor<UUID<WorkspacePane>> { UUID.nil<WorkspacePane>() }
    val rightBottom = storeFor<UUID<WorkspacePane>> { UUID.nil<WorkspacePane>() }
    val center = storeFor<UUID<WorkspacePane>> { UUID.nil<WorkspacePane>() }
    val leftTop = storeFor<UUID<WorkspacePane>> { UUID.nil<WorkspacePane>() }
    val leftMiddle = storeFor<UUID<WorkspacePane>> { UUID.nil<WorkspacePane>() }
    val leftBottom = storeFor<UUID<WorkspacePane>> { UUID.nil<WorkspacePane>() }

    /**
     * Top contains: top split
     * Bottom contains: bottom split
     */
    val mainSplit = storeFor { SplitPaneConfiguration(SplitVisibility.First, SplitMethod.FixSecond, 300.0, Orientation.Vertical, 10.dp) }

    /**
     * Left contains: bottom left pane
     * Right contains: bottom right pane
     */
    val bottomSplit = storeFor { SplitPaneConfiguration(SplitVisibility.First, SplitMethod.Proportional, 0.5, Orientation.Horizontal, 10.dp) }

    /**
     * Left contains: left split
     * Right contains: center and right split
     */
    val topSplit = storeFor { SplitPaneConfiguration(SplitVisibility.Second, SplitMethod.FixFirst, 300.0, Orientation.Horizontal, 10.dp) }

    /**
     * Top contains: left top pane
     * Bottom contains: left middle pane
     */
    var leftSplit = storeFor { SplitPaneConfiguration(SplitVisibility.First, SplitMethod.Proportional, 0.5, Orientation.Vertical, 10.dp) }

    /**
     * Left contains: center pane
     * Right contains: right split
     */
    var centerRightSplit = storeFor { SplitPaneConfiguration(SplitVisibility.First, SplitMethod.FixSecond, 300.0, Orientation.Horizontal, 10.dp) }

    /**
     * Top contains: right top pane
     * Bottom contains: right middle pane
     */
    var rightSplit = storeFor { SplitPaneConfiguration(SplitVisibility.First, SplitMethod.Proportional, 0.5, Orientation.Vertical, 10.dp) }

    // ---------------------------------------------------------------------------------------------
    // Utility
    // ---------------------------------------------------------------------------------------------

    fun topPanes(left : Boolean) =
        panes.filter(if (left) WorkspacePanePosition.LeftTop else WorkspacePanePosition.RightTop)

    fun middlePanes(left : Boolean) =
        panes.filter(if (left) WorkspacePanePosition.LeftMiddle else WorkspacePanePosition.RightMiddle)

    fun bottomPanes(left : Boolean) =
        panes.filter(if (left) WorkspacePanePosition.LeftBottom else WorkspacePanePosition.RightBottom)

    fun List<WorkspacePane>.filter(position: WorkspacePanePosition) =
        filter { it.position == position }


    // ---------------------------------------------------------------------------------------------
    // Pane toggle
    // ---------------------------------------------------------------------------------------------

    /**
     * Toggle the given pane (typically when the user clicks on the pane icon).
     */
    fun onIconClick(pane: WorkspacePane) {
        when (pane.position) {
            WorkspacePanePosition.LeftTop -> {
                val leftShown = topSplit.isFirstVisible
                val leftTopShown = leftSplit.isFirstVisible
                val leftMiddleShown = leftSplit.isSecondVisible

                // should we show the left pane at all
                val showLeft = ! leftShown || (! leftTopShown || leftMiddleShown)
                if (! showLeft) {
                    topSplit.value = topSplit.value.copy(visibility = SplitVisibility.Second)
                    return
                }

                // we surely have to show the left pane
                if (leftShown) {
                    if (leftTopShown) {
                        leftSplit.value = leftSplit.value.copy(visibility = SplitVisibility.Second)
                    } else {
                        leftTop.value = pane.uuid
                        leftSplit.value = leftSplit.value.copy(visibility = SplitVisibility.Both)
                    }
                } else {
                    leftTop.value = pane.uuid
                    leftSplit.value = leftSplit.value.copy(visibility = SplitVisibility.First)
                    topSplit.value = topSplit.value.copy(visibility = SplitVisibility.Both)
                }
            }

            WorkspacePanePosition.LeftMiddle -> {
                val leftShown = topSplit.isFirstVisible
                val leftTopShown = leftSplit.isFirstVisible
                val leftMiddleShown = leftSplit.isSecondVisible

                // should we show the left pane at all
                val showLeft = ! leftShown || (leftTopShown || ! leftMiddleShown)
                if (! showLeft) {
                    topSplit.value = topSplit.value.copy(visibility = SplitVisibility.Second)
                    return
                }

                // we surely have to show the left pane
                if (leftShown) {
                    if (leftMiddleShown) {
                        leftSplit.value = leftSplit.value.copy(visibility = SplitVisibility.First)
                    } else {
                        leftMiddle.value = pane.uuid
                        leftSplit.value = leftSplit.value.copy(visibility = SplitVisibility.Both)
                    }
                } else {
                    leftMiddle.value = pane.uuid
                    leftSplit.value = leftSplit.value.copy(visibility = SplitVisibility.Second)
                    topSplit.value = topSplit.value.copy(visibility = SplitVisibility.Both)
                }
            }

            WorkspacePanePosition.LeftBottom -> leftBottom.value = pane.uuid
            WorkspacePanePosition.RightTop -> leftTop.value = pane.uuid
            WorkspacePanePosition.RightMiddle -> rightMiddle.value = pane.uuid
            WorkspacePanePosition.RightBottom -> rightBottom.value = pane.uuid
            WorkspacePanePosition.Center -> Unit
        }
    }

    val Observable<SplitPaneConfiguration>.isBothVisible
        get() = value.visibility == SplitVisibility.Both

    val Observable<SplitPaneConfiguration>.isFirstVisible
        get() = value.visibility.let { it == SplitVisibility.First || it == SplitVisibility.Both }

    val Observable<SplitPaneConfiguration>.isSecondVisible
        get() = value.visibility.let { it == SplitVisibility.Second || it == SplitVisibility.Both }

}