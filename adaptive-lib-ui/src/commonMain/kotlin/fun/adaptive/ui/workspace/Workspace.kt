package `fun`.adaptive.ui.workspace

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.general.Observable
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.model.NamedItemType
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.ui.builtin.menu
import `fun`.adaptive.ui.fragment.layout.SplitPaneConfiguration
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.instruction.layout.Orientation
import `fun`.adaptive.ui.instruction.layout.SplitMethod
import `fun`.adaptive.ui.instruction.layout.SplitVisibility
import `fun`.adaptive.ui.workspace.logic.WsUnitPaneController
import `fun`.adaptive.ui.workspace.model.*
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.firstInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class Workspace(
    val backend: BackendAdapter,
    val scope: CoroutineScope = backend.scope,
    val transport: ServiceCallTransport = backend.transport
) {

    companion object {
        inline fun <reified T> AdaptiveFragment.wsContext() =
            firstContext<Workspace>().contexts.firstInstance<T>()

        const val WS_CENTER_PANE = "lib:ws:center"
        const val WSPANE_EMPTY = "lib:ws:nocontent"

        val noContentPane = WsPane(
            UUID(),
            "No content",
            Graphics.menu,
            WsPanePosition.Center,
            WSPANE_EMPTY,
            data = Unit,
            controller = WsUnitPaneController(),
            singularity = WsPaneSingularity.SINGULAR
        )
    }

    val logger = getLogger("workspace")

    val contexts = mutableListOf<Any>()

    val contentPaneBuilders = mutableMapOf<NamedItemType, MutableList<WsContentPaneBuilder>>()

    val theme
        get() = WorkspaceTheme.DEFAULT

    val toolPanes = mutableListOf<WsPane<*, *>>()

    val sideBarActions = mutableListOf<AbstractSideBarAction>()

    val contentPaneGroups = storeFor {
        listOf<WsContentPaneGroup>(
            WsContentPaneGroup(UUID(), this, noContentPane)
        )
    }

    var lastActiveContentPaneGroup: WsContentPaneGroup? = null

    var lastActiveContentPane: WsPane<*, *>? = null

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

    fun topActions(left: Boolean) =
        if (left) {
            sideBarActions {
                (it.singularity == WsPaneSingularity.SINGULAR && it.position == WsPanePosition.Center)
                    || it.position == WsPanePosition.LeftTop
            }
        } else {
            sideBarActions { it.position == WsPanePosition.RightTop }
        }

    fun middlePanes(left: Boolean) =
        if (left) {
            sideBarActions { it.position == WsPanePosition.LeftMiddle }
        } else {
            sideBarActions { it.position == WsPanePosition.RightMiddle }
        }

    fun bottomPanes(left: Boolean) =
        if (left) {
            sideBarActions { it.position == WsPanePosition.LeftBottom }
        } else {
            sideBarActions { it.position == WsPanePosition.RightBottom }
        }

    fun sideBarActions(filterFun: (action: AbstractSideBarAction) -> Boolean) =
        (toolPanes.filter(filterFun) + sideBarActions.filter(filterFun)).sortedBy { it.displayOrder }

    fun io(block: suspend () -> Unit) {
        scope.launch { block() } // FIXME this should run in the backend scope
    }

    fun ui(block: suspend () -> Unit) {
        scope.launch { block() } // FIXME this should run in the frontend scope
    }

    // ---------------------------------------------------------------------------------------------
    // Pane switching
    // ---------------------------------------------------------------------------------------------

    /**
     * Toggle the given pane (typically when the user clicks on the pane icon).
     */
    fun toggle(pane: WsPane<*, *>) {
        when (pane.position) {
            WsPanePosition.LeftTop -> toggleStore(leftTop, pane)
            WsPanePosition.LeftMiddle -> toggleStore(leftMiddle, pane)
            WsPanePosition.LeftBottom -> toggleStore(leftBottom, pane)
            WsPanePosition.RightTop -> toggleStore(rightTop, pane)
            WsPanePosition.RightMiddle -> toggleStore(rightMiddle, pane)
            WsPanePosition.RightBottom -> toggleStore(rightBottom, pane)
            WsPanePosition.Center -> {
                if (pane.data is NamedItem) {
                    addContent(pane.data)
                }
                return // no split update is needed as center is always shown
            }
        }

        updateSplits()
    }

    private fun toggleStore(store: Observable<WsPaneId?>, pane: WsPane<*, *>) {
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

    fun paneStore(pane: WsPane<*, *>): Observable<WsPaneId?> =
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
    // Tool management
    // --------------------------------------------------------------------------------

    operator fun WsPane<*,*>.unaryPlus() {
        toolPanes += this
    }

    operator fun WsSideBarAction.unaryPlus() {
        sideBarActions += this
    }

    // --------------------------------------------------------------------------------
    // Content management
    // --------------------------------------------------------------------------------

    fun addContentPaneBuilder(vararg itemTypes: NamedItemType, builder: WsContentPaneBuilder) {
        for (itemType in itemTypes) {
            contentPaneBuilders.getOrPut(itemType) { mutableListOf() } += builder
        }
    }

    fun addContent(item: NamedItem, modifiers: Set<EventModifier> = emptySet()) {

        val accepted = accept(item, modifiers)
        if (accepted) {
            return
        }

        val builder = findBuilder(item.type)

        if (builder == null) {
            logger.warning("no pane builder for type ${item.type}")
            return
        }

        val pane = builder.invoke(item)

        when (pane.singularity) {
            WsPaneSingularity.SINGULAR -> {
                lastActiveContentPaneGroup = null
                addGroupContentPane(item, modifiers, pane)
            }

            WsPaneSingularity.GROUP -> {
                addGroupContentPane(item, modifiers, pane)
            }
        }
    }

    fun findBuilder(type: NamedItemType): WsContentPaneBuilder? {
        var builder = contentPaneBuilders[type]?.firstOrNull()
        if (builder != null) return builder

        var generalType = type.substringBeforeLast(':')

        while (generalType.isNotEmpty()) {

            var builder = contentPaneBuilders[generalType]?.firstOrNull()
            if (builder != null) return builder

            val lastColon = generalType.lastIndexOf(':')
            if (lastColon == - 1) break

            generalType = generalType.substring(0, lastColon)
        }

        return null
    }

    fun accept(item: NamedItem, modifiers: Set<EventModifier>): Boolean {
        lastActiveContentPane?.let {
            if (it.accepts(item, modifiers)) {
                loadContentPane(item, modifiers, it, lastActiveContentPaneGroup !!)
                return true
            }
        }

        lastActiveContentPaneGroup?.let {
            if (accept(item, modifiers, it)) return true
        }

        contentPaneGroups.value.forEach {
            if (accept(item, modifiers, it)) return true
        }

        return false
    }

    fun accept(item: NamedItem, modifiers: Set<EventModifier>, group: WsContentPaneGroup): Boolean {

        for (pane in group.panes) {
            if (pane.accepts(item, modifiers)) {
                loadContentPane(item, modifiers, pane, lastActiveContentPaneGroup !!)
                return true
            }
        }

        return false
    }

    fun loadContentPane(item: NamedItem, modifiers: Set<EventModifier>, pane: WsPane<*, *>, group: WsContentPaneGroup) {
        // pane.load may return with a different pane, most notably the name and tooltip of the pane may change
        group.load(pane.load(item, modifiers))
    }

    fun addGroupContentPane(item: NamedItem, modifiers: Set<EventModifier>, pane: WsPane<*, *>) {

        val safeGroup = lastActiveContentPaneGroup

        if (safeGroup == null || safeGroup.isSingular) {

            WsContentPaneGroup(UUID(), this, pane).also {
                lastActiveContentPaneGroup = it
                contentPaneGroups.value = listOf(it)
                loadContentPane(item, modifiers, pane, it)
            }

        } else {

            safeGroup.panes += pane
            loadContentPane(item, modifiers, pane, safeGroup)

        }

        return
    }

    // --------------------------------------------------------------------------------
    // Item type management
    // --------------------------------------------------------------------------------

    private val itemTypes = mutableMapOf<NamedItemType, WsItemConfig>()

    fun addItemConfig(type: NamedItemType, icon: GraphicsResourceSet, tooltip: String? = null) {
        itemTypes[type] = WsItemConfig(type, icon, tooltip)
    }

    fun getItemConfig(type: NamedItemType) = itemTypes[type] ?: WsItemConfig.DEFAULT

}