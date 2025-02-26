package `fun`.adaptive.ui.workspace

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.general.Observable
import `fun`.adaptive.ui.fragment.layout.SplitPaneConfiguration
import `fun`.adaptive.ui.instruction.layout.Orientation
import `fun`.adaptive.ui.instruction.layout.SplitMethod
import `fun`.adaptive.ui.instruction.layout.SplitVisibility
import `fun`.adaptive.ui.workspace.WorkspaceTheme.Companion.workspaceTheme
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.firstInstance

class Workspace {

    companion object {
        inline fun <reified T> AdaptiveFragment.wsContext() =
            firstContext<Workspace>().contexts.firstInstance<T>()
    }

    val contexts = mutableListOf<Any>()

    val theme
        get() = workspaceTheme

    val panes = mutableListOf<WorkspacePane>()

    val rightTop = storeFor<UUID<WorkspacePane>?> { null }
    val rightMiddle = storeFor<UUID<WorkspacePane>?> { null }
    val rightBottom = storeFor<UUID<WorkspacePane>?> { null }
    val center = storeFor<UUID<WorkspacePane>?> { null }
    val leftTop = storeFor<UUID<WorkspacePane>?> { null }
    val leftMiddle = storeFor<UUID<WorkspacePane>?> { null }
    val leftBottom = storeFor<UUID<WorkspacePane>?> { null }

    /**
     * Top contains: top split
     * Bottom contains: bottom split
     */
    val mainSplit = storeFor { SplitPaneConfiguration(SplitVisibility.First, SplitMethod.FixSecond, 300.0, Orientation.Vertical) }

    /**
     * Left contains: bottom left pane
     * Right contains: bottom right pane
     */
    val bottomSplit = storeFor { SplitPaneConfiguration(SplitVisibility.First, SplitMethod.Proportional, 0.5, Orientation.Horizontal) }

    /**
     * Left contains: left split
     * Right contains: center and right split
     */
    val topSplit = storeFor { SplitPaneConfiguration(SplitVisibility.Second, SplitMethod.FixFirst, 300.0, Orientation.Horizontal) }

    /**
     * Top contains: left top pane
     * Bottom contains: left middle pane
     */
    var leftSplit = storeFor { SplitPaneConfiguration(SplitVisibility.First, SplitMethod.Proportional, 0.5, Orientation.Vertical) }

    /**
     * Left contains: center pane
     * Right contains: right split
     */
    var centerRightSplit = storeFor { SplitPaneConfiguration(SplitVisibility.First, SplitMethod.FixSecond, 300.0, Orientation.Horizontal) }

    /**
     * Top contains: right top pane
     * Bottom contains: right middle pane
     */
    var rightSplit = storeFor { SplitPaneConfiguration(SplitVisibility.First, SplitMethod.Proportional, 0.5, Orientation.Vertical) }

    // ---------------------------------------------------------------------------------------------
    // Utility
    // ---------------------------------------------------------------------------------------------

    fun directPanes() =
        panes.filter { it.direct }

    fun topPanes(left: Boolean) =
        panes.filter(if (left) WorkspacePanePosition.LeftTop else WorkspacePanePosition.RightTop)

    fun middlePanes(left: Boolean) =
        panes.filter(if (left) WorkspacePanePosition.LeftMiddle else WorkspacePanePosition.RightMiddle)

    fun bottomPanes(left: Boolean) =
        panes.filter(if (left) WorkspacePanePosition.LeftBottom else WorkspacePanePosition.RightBottom)

    fun List<WorkspacePane>.filter(position: WorkspacePanePosition) =
        filter { it.position == position }


    // ---------------------------------------------------------------------------------------------
    // Pane switching
    // ---------------------------------------------------------------------------------------------

    /**
     * Toggle the given pane (typically when the user clicks on the pane icon).
     */
    fun toggle(pane: WorkspacePane) {
        when (pane.position) {
            WorkspacePanePosition.LeftTop -> toggleStore(leftTop, pane)
            WorkspacePanePosition.LeftMiddle -> toggleStore(leftMiddle , pane)
            WorkspacePanePosition.LeftBottom -> toggleStore(leftBottom , pane)
            WorkspacePanePosition.RightTop -> toggleStore(rightTop , pane)
            WorkspacePanePosition.RightMiddle -> toggleStore(rightMiddle , pane)
            WorkspacePanePosition.RightBottom -> toggleStore(rightBottom , pane)
            WorkspacePanePosition.Center -> {
                center.value = pane.uuid
                return // no split update is needed as center is always shown
            }
        }
        updateSplits()
    }

    private fun toggleStore(store : Observable<UUID<WorkspacePane>?>, pane: WorkspacePane) {
        if (store.value == pane.uuid) {
            store.value = null
        } else {
            store.value = pane.uuid
        }
    }

    fun updateSplits() {
        val leftTop = leftTop.value
        val leftMiddle = leftMiddle.value
        val leftBottom = leftBottom.value
        val rightBottom = rightBottom.value
        val rightTop = rightTop.value
        val rightMiddle = rightMiddle.value

        val hasLeft = (leftTop != null || leftMiddle != null)
        val hasBottom = (leftBottom != null || rightBottom != null)
        val hasRight = (rightTop != null || rightMiddle != null)

        update(rightSplit, rightTop, rightMiddle)
        update(bottomSplit, leftBottom, rightBottom)
        update(leftSplit, leftTop, leftMiddle)

        update(centerRightSplit, if (hasRight) SplitVisibility.Both else SplitVisibility.First)
        update(topSplit, if (hasLeft) SplitVisibility.Both else SplitVisibility.Second)
        update(mainSplit, if (hasBottom) SplitVisibility.Both else SplitVisibility.First)
    }

    fun visibility(first: UUID<*>?, second: UUID<*>?): SplitVisibility =
        if (first == null) {
            if (second != null) SplitVisibility.Second else SplitVisibility.None
        } else {
            if (second == null) SplitVisibility.First else SplitVisibility.Both
        }

    fun update(split : Observable<SplitPaneConfiguration>, first : UUID<*>?, second : UUID<*>?) {
        val new = visibility(first, second)
        val current = split.value.visibility
        if (current == new) return
        split.value = split.value.copy(visibility = new)
    }

    fun update(split : Observable<SplitPaneConfiguration>, new : SplitVisibility) {
        val current = split.value.visibility
        if (current == new) return
        split.value = split.value.copy(visibility = new)
    }

}