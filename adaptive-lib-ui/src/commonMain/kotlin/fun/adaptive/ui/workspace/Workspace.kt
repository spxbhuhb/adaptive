package `fun`.adaptive.ui.workspace

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.general.Observable
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.builtin.menu
import `fun`.adaptive.ui.fragment.layout.SplitPaneConfiguration
import `fun`.adaptive.ui.instruction.layout.Orientation
import `fun`.adaptive.ui.instruction.layout.SplitMethod
import `fun`.adaptive.ui.instruction.layout.SplitVisibility
import `fun`.adaptive.ui.workspace.model.WsContentPaneBuilder
import `fun`.adaptive.ui.workspace.model.WsContentPaneGroup
import `fun`.adaptive.ui.workspace.model.WsItem
import `fun`.adaptive.ui.workspace.model.WsItemType
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPaneId
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.ui.workspace.model.WsPaneSingularity
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.firstInstance
import kotlin.collections.filter

class Workspace {

    companion object {
        inline fun <reified T> AdaptiveFragment.wsContext() =
            firstContext<Workspace>().contexts.firstInstance<T>()

        const val WS_CENTER_PANE = "lib:ws:center"
        const val WS_NO_CONTENT = "lib:ws:nocontent"

        val noContentPane = WsPane(
            UUID(),
            "No content",
            Graphics.menu,
            WsPanePosition.Center,
            WS_NO_CONTENT,
            model = Unit
        )
    }

    val logger = getLogger("workspace")

    val contexts = mutableListOf<Any>()

    val contentPaneBuilders = mutableMapOf<WsItemType,MutableList<WsContentPaneBuilder>>()

    val theme
        get() = WorkspaceTheme.Companion.workspaceTheme

    val toolPanes = mutableListOf<WsPane<*>>()

    val contentPaneGroups = storeFor {
        listOf<WsContentPaneGroup>(
            WsContentPaneGroup(UUID(), this, noContentPane)
        )
    }

    var lastActiveContentPaneGroup: WsContentPaneGroup? = null

    var lastActiveContentPane: WsPane<*>? = null

    val focusedPane = storeFor<WsPaneId?> { null }

    val rightTop = storeFor<WsPaneId?> { null }
    val rightMiddle = storeFor<WsPaneId?> { null }
    val rightBottom = storeFor<WsPaneId?> { null }
    val center = storeFor<WsPaneId?> { null }
    val leftTop = storeFor<WsPaneId?> { null }
    val leftMiddle = storeFor<WsPaneId?> { null }
    val leftBottom = storeFor<WsPaneId?> { null }

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
        toolPanes.filter { it.singularity == WsPaneSingularity.SINGULAR_OVERALL }

    fun topPanes(left: Boolean) =
        toolPanes.filter(if (left) WsPanePosition.LeftTop else WsPanePosition.RightTop)

    fun middlePanes(left: Boolean) =
        toolPanes.filter(if (left) WsPanePosition.LeftMiddle else WsPanePosition.RightMiddle)

    fun bottomPanes(left: Boolean) =
        toolPanes.filter(if (left) WsPanePosition.LeftBottom else WsPanePosition.RightBottom)

    fun List<WsPane<*>>.filter(position: WsPanePosition) =
        filter { it.position == position }

    // ---------------------------------------------------------------------------------------------
    // Pane switching
    // ---------------------------------------------------------------------------------------------

    /**
     * Toggle the given pane (typically when the user clicks on the pane icon).
     */
    fun toggle(pane: WsPane<*>) {
        when (pane.position) {
            WsPanePosition.LeftTop -> toggleStore(leftTop, pane)
            WsPanePosition.LeftMiddle -> toggleStore(leftMiddle, pane)
            WsPanePosition.LeftBottom -> toggleStore(leftBottom, pane)
            WsPanePosition.RightTop -> toggleStore(rightTop, pane)
            WsPanePosition.RightMiddle -> toggleStore(rightMiddle, pane)
            WsPanePosition.RightBottom -> toggleStore(rightBottom, pane)
            WsPanePosition.Center -> {
                center.value = pane.uuid
                return // no split update is needed as center is always shown
            }
        }
        updateSplits()
    }

    private fun toggleStore(store: Observable<WsPaneId?>, pane: WsPane<*>) {
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

    fun update(split: Observable<SplitPaneConfiguration>, first: UUID<*>?, second: UUID<*>?) {
        val new = visibility(first, second)
        val current = split.value.visibility
        if (current == new) return
        split.value = split.value.copy(visibility = new)
    }

    fun update(split: Observable<SplitPaneConfiguration>, new: SplitVisibility) {
        val current = split.value.visibility
        if (current == new) return
        split.value = split.value.copy(visibility = new)
    }

    fun paneStore(pane: WsPane<*>): Observable<WsPaneId?> =
        when (pane.position) {
            WsPanePosition.LeftTop -> leftTop
            WsPanePosition.LeftMiddle -> leftMiddle
            WsPanePosition.LeftBottom -> leftBottom
            WsPanePosition.RightTop -> rightTop
            WsPanePosition.RightMiddle -> rightMiddle
            WsPanePosition.RightBottom -> rightBottom
            WsPanePosition.Center -> center
        }

    // --------------------------------------------------------------------------------
    // Content management
    // --------------------------------------------------------------------------------

    fun addContentPaneBuilder(itemType: WsItemType, builder: WsContentPaneBuilder) {
        contentPaneBuilders.getOrPut(itemType) { mutableListOf() } += builder
    }

    fun addContent(item: WsItem) {

        accept(item)?.let {
            it.load(item)
            return
        }

        val builder = contentPaneBuilders[item.type]?.firstOrNull()

        if (builder == null) {
            logger.warning("no pane builder for type ${item.type}")
            return
        }

        val pane = builder.invoke(item)

        when (pane.singularity) {
            WsPaneSingularity.SINGULAR_OVERALL -> {
                TODO()
            }
            WsPaneSingularity.SINGULAR_GROUP -> {
                TODO()
            }
            WsPaneSingularity.GROUP -> {
                loadGroupContentPane(pane, item)
            }
        }
    }

    fun accept(item: WsItem): WsPane<*>? {
        lastActiveContentPane?.let {
            if (it.accepts(item)) return it
        }

        lastActiveContentPaneGroup?.let {
            accept(item, it)?.also { return it }
        }

        contentPaneGroups.value.forEach {
            accept(item, it)?.also { return it }
        }

        return null
    }

    fun accept(item: WsItem, group: WsContentPaneGroup): WsPane<*>? {

        for (pane in group.panes.value) {
            if (pane.accepts(item)) return pane
        }

        return null
    }

    fun loadGroupContentPane(pane : WsPane<*>, item: WsItem) {

        val safeGroup = lastActiveContentPaneGroup

        if (safeGroup == null) {

            WsContentPaneGroup(UUID(), this, pane).also {
                lastActiveContentPaneGroup = it
                contentPaneGroups.value = listOf(it)
                pane.load(item)
            }

            return
        }

        return
    }

}