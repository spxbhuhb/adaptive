package `fun`.adaptive.ui.mpw

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.value.observableOf
import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.general.Observable
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.runtime.BackendWorkspace
import `fun`.adaptive.runtime.FrontendWorkspace
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.ui.fragment.layout.SplitPaneViewBackend
import `fun`.adaptive.ui.generated.resources.menu
import `fun`.adaptive.ui.generated.resources.saveFail
import `fun`.adaptive.ui.generated.resources.saveSuccess
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.instruction.layout.Orientation
import `fun`.adaptive.ui.instruction.layout.SplitMethod
import `fun`.adaptive.ui.instruction.layout.SplitVisibility
import `fun`.adaptive.ui.mpw.backends.ContentPaneGroupViewBackend
import `fun`.adaptive.ui.mpw.backends.PaneViewBackend
import `fun`.adaptive.ui.mpw.backends.UnitPaneViewBackend
import `fun`.adaptive.ui.mpw.model.*
import `fun`.adaptive.ui.navigation.NavState
import `fun`.adaptive.ui.snackbar.failNotification
import `fun`.adaptive.ui.snackbar.successNotification
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.firstInstance
import kotlinx.coroutines.CoroutineScope
import kotlin.reflect.KClass

open class MultiPaneWorkspace(
    backend: BackendAdapter,
    backendWorkspace: BackendWorkspace,
    scope: CoroutineScope = backend.scope,
    transport: ServiceCallTransport = backend.transport
) : FrontendWorkspace(backend, backendWorkspace, scope, transport) {

    companion object {
        inline fun <reified T> AdaptiveFragment.wsContext() =
            firstContext<MultiPaneWorkspace>().contexts.firstInstance<T>()

        inline fun <reified T : PaneViewBackend<T>> AdaptiveFragment.wsToolOrNull() =
            firstContext<MultiPaneWorkspace>().toolBackend(T::class)

        const val WS_CENTER_PANE = "lib:ws:center"
        const val WSPANE_EMPTY = "lib:ws:nocontent"
    }

    val contentPaneBuilders = mutableMapOf<PaneContentType, MutableList<ContentPaneBuilder<*>>>()

    val theme
        get() = MultiPaneTheme.DEFAULT

    val toolPanes = mutableListOf<PaneViewBackend<*>>()

    val sideBarActions = mutableListOf<AbstractSideBarAction>()

    val urlResolvers = mutableListOf<MultiPaneUrlResolver>()

    val noContentPane = PaneDef(
        UUID(),
        name = "No content",
        icon = Graphics.menu,
        position = PanePosition.Center,
        fragmentKey = WSPANE_EMPTY,
        singularity = PaneSingularity.SINGULAR
    )

    val contentPaneGroups = storeFor {
        listOf(
            ContentPaneGroupViewBackend(
                UUID(),
                this,
                UnitPaneViewBackend(this, noContentPane)
            )
        )
    }

    var lastActiveContentPaneGroup: ContentPaneGroupViewBackend? = null

    var lastActiveContentPane: PaneViewBackend<*>? = null

    val focusedPane = observableOf<PaneId?> { null }

    val isFullScreen = observableOf { false }

    val rightTop = observableOf<PaneId?> { null }
    val rightMiddle = observableOf<PaneId?> { null }
    val rightBottom = observableOf<PaneId?> { null }
    val center = observableOf<PaneId?> { null }
    val leftTop = observableOf<PaneId?> { null }
    val leftMiddle = observableOf<PaneId?> { null }
    val leftBottom = observableOf<PaneId?> { null }
    val hidden = observableOf<PaneId?> { null }

    /**
     * Top contains: top split
     * Bottom contains: bottom split
     */
    val mainSplit = storeFor { SplitPaneViewBackend(SplitVisibility.First, SplitMethod.FixSecond, 300.0, Orientation.Vertical) }

    /**
     * Left contains: bottom left pane
     * Right contains: bottom right pane
     */
    val bottomSplit = storeFor { SplitPaneViewBackend(SplitVisibility.First, SplitMethod.Proportional, 0.5, Orientation.Horizontal) }

    /**
     * Left contains: left split
     * Right contains: center and right split
     */
    val topSplit = storeFor { SplitPaneViewBackend(SplitVisibility.Second, SplitMethod.FixFirst, 300.0, Orientation.Horizontal) }

    /**
     * Top contains: left top pane
     * Bottom contains: left middle pane
     */
    var leftSplit = storeFor { SplitPaneViewBackend(SplitVisibility.First, SplitMethod.Proportional, 0.5, Orientation.Vertical) }

    /**
     * Left contains: center pane
     * Right contains: right split
     */
    var centerRightSplit = storeFor { SplitPaneViewBackend(SplitVisibility.First, SplitMethod.FixSecond, 300.0, Orientation.Horizontal) }

    /**
     * Top contains: right top pane
     * Bottom contains: right middle pane
     */
    var rightSplit = storeFor { SplitPaneViewBackend(SplitVisibility.First, SplitMethod.Proportional, 0.5, Orientation.Vertical) }

    // ---------------------------------------------------------------------------------------------
    // Utility
    // ---------------------------------------------------------------------------------------------

    fun topActions(left: Boolean) =
        if (left) {
            sideBarActions {
                (it.singularity == PaneSingularity.SINGULAR && it.position == PanePosition.Center)
                    || it.position == PanePosition.LeftTop
            }
        } else {
            sideBarActions { it.position == PanePosition.RightTop }
        }

    fun middlePanes(left: Boolean) =
        if (left) {
            sideBarActions { it.position == PanePosition.LeftMiddle }
        } else {
            sideBarActions { it.position == PanePosition.RightMiddle }
        }

    fun bottomPanes(left: Boolean) =
        if (left) {
            sideBarActions { it.position == PanePosition.LeftBottom }
        } else {
            sideBarActions { it.position == PanePosition.RightBottom }
        }

    fun sideBarActions(filterFun: (action: AbstractSideBarAction) -> Boolean) =
        (toolPanes.map { it.paneDef }.filter(filterFun) + sideBarActions.filter(filterFun)).sortedBy { it.displayOrder }

    // ---------------------------------------------------------------------------------------------
    // Pane switching
    // ---------------------------------------------------------------------------------------------

    fun toggle(paneDef: PaneDef) {
        toolPanes.firstOrNull { it.paneDef.uuid == paneDef.uuid }?.let { toggle(it) }
    }

    /**
     * Toggle the given pane (typically when the user clicks on the pane icon).
     */
    fun toggle(pane: PaneViewBackend<*>) {
        when (pane.paneDef.position) {
            PanePosition.LeftTop -> toggleStore(leftTop, pane)
            PanePosition.LeftMiddle -> toggleStore(leftMiddle, pane)
            PanePosition.LeftBottom -> toggleStore(leftBottom, pane)
            PanePosition.RightTop -> toggleStore(rightTop, pane)
            PanePosition.RightMiddle -> toggleStore(rightMiddle, pane)
            PanePosition.RightBottom -> toggleStore(rightBottom, pane)
            PanePosition.Center -> {
                // FIXME ?? addContent(pane)
                return // no split update is needed as center is always shown
            }
        }

        updateSplits()
    }

    fun closeAllTools() {
        leftTop.value = null
        leftMiddle.value = null
        leftBottom.value = null
        rightTop.value = null
        rightMiddle.value = null
        rightBottom.value = null

        updateSplits()
    }

    fun toFullScreen() {
        closeAllTools()
        isFullScreen.value = true
    }

    fun fromFullScreen() {
        isFullScreen.value = false
    }

    private fun toggleStore(store: Observable<PaneId?>, pane: PaneViewBackend<*>) {
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

    fun update(split: Observable<SplitPaneViewBackend>, first: UUID<*>?, second: UUID<*>?) {
        val new = visibility(first, second)
        val current = split.value.visibility
        if (current == new) return
        split.value = split.value.copy(visibility = new)
    }

    fun update(split: Observable<SplitPaneViewBackend>, new: SplitVisibility) {
        val current = split.value.visibility
        if (current == new) return
        split.value = split.value.copy(visibility = new)
    }

    fun paneStore(paneDef: PaneDef?): Observable<PaneId?> =
        when (paneDef?.position) {
            PanePosition.LeftTop -> leftTop
            PanePosition.LeftMiddle -> leftMiddle
            PanePosition.LeftBottom -> leftBottom
            PanePosition.RightTop -> rightTop
            PanePosition.RightMiddle -> rightMiddle
            PanePosition.RightBottom -> rightBottom
            PanePosition.Center -> center
            else -> hidden
        }

    // --------------------------------------------------------------------------------
    // Tool management
    // --------------------------------------------------------------------------------

    inline fun addToolPane(paneBackend: () -> PaneViewBackend<*>) {
        toolPanes += paneBackend()
    }

    operator fun SideBarAction.unaryPlus() {
        sideBarActions += this
    }

    fun <T : PaneViewBackend<T>> toolBackend(kClass: KClass<T>): T? {
        for (pane in toolPanes) {
            @Suppress("UNCHECKED_CAST")
            if (kClass.isInstance(pane)) return pane as T
        }
        return null
    }

    // --------------------------------------------------------------------------------
    // Content management
    // --------------------------------------------------------------------------------

    class ContentPaneBuilder<T>(
        val condition: (type : PaneContentType, item : PaneContentItem) -> T?,
        val builder: (item: T) -> PaneViewBackend<*>?
    )

    fun <T> addContentPaneBuilder(
        contentType: PaneContentType,
        condition: (type : PaneContentType, item : PaneContentItem) -> T?,
        builder: (item: T) -> PaneViewBackend<*>?
    ) {
        contentPaneBuilders.getOrPut(contentType) { mutableListOf() } +=
            ContentPaneBuilder(
                condition,
                builder
            )
    }

    fun addSingularContentPane(
        singularItem: SingularPaneItem,
        builder: (item: SingularPaneItem) -> PaneViewBackend<*>?
    ) {
        contentPaneBuilders.getOrPut(singularItem.type) { mutableListOf() } +=
            ContentPaneBuilder(
                { type, item -> if (item === singularItem) item else null },
                builder
            )
    }

    fun addContent(item: SingularPaneItem, modifiers: Set<EventModifier> = emptySet()) {
        addContent(item.type, item, modifiers)
    }

    fun addContent(type: PaneContentType, item: PaneContentItem, modifiers: Set<EventModifier> = emptySet()) {

        val accepted = accept(type, item, modifiers)
        if (accepted) {
            return
        }

        @Suppress("UNCHECKED_CAST") // erase the type so we can call builder freely
        val builder = findBuilder(type) as? ContentPaneBuilder<Any>

        if (builder == null) {
            logger.warning("no pane builder for type $type")
            return
        }

        val pane = builder.builder(item)
        checkNotNull(pane) { "builder for type $type returned null pane" }

        when (pane.paneDef.singularity) {
            PaneSingularity.FULLSCREEN -> {
                lastActiveContentPaneGroup = null
                toFullScreen()
                addGroupContentPane(type, item, modifiers, pane)
            }

            PaneSingularity.SINGULAR -> {
                lastActiveContentPaneGroup = null
                fromFullScreen()
                addGroupContentPane(type, item, modifiers, pane)
            }

            PaneSingularity.GROUP -> {
                fromFullScreen()
                addGroupContentPane(type, item, modifiers, pane)
            }
        }
    }

    fun findBuilder(type: PaneContentType): ContentPaneBuilder<*>? {
        var builder = contentPaneBuilders[type]?.firstOrNull()
        if (builder != null) return builder

        var generalType = type.substringBeforeLast(':')

        while (generalType.isNotEmpty()) {

            builder = contentPaneBuilders[generalType]?.firstOrNull()
            if (builder != null) return builder

            val lastColon = generalType.lastIndexOf(':')
            if (lastColon == - 1) break

            generalType = generalType.substring(0, lastColon)
        }

        return null
    }

    fun accept(
        type: PaneContentType,
        item: PaneContentItem,
        modifiers: Set<EventModifier>
    ): Boolean {
        lastActiveContentPane?.let {
            if (it.accepts(item, modifiers)) {
                loadContentPane(type, item, modifiers, it, lastActiveContentPaneGroup !!)
                return true
            }
        }

        lastActiveContentPaneGroup?.let {
            if (accept(type, item, modifiers, it)) return true
        }

        contentPaneGroups.value.forEach {
            if (accept(type, item, modifiers, it)) return true
        }

        return false
    }

    fun accept(
        type: PaneContentType,
        item: PaneContentItem,
        modifiers: Set<EventModifier>,
        group: ContentPaneGroupViewBackend
    ): Boolean {

        for (pane in group.panes) {
            if (pane.accepts(item, modifiers)) {
                loadContentPane(type, item, modifiers, pane, lastActiveContentPaneGroup !!)
                return true
            }
        }

        return false
    }

    fun loadContentPane(
        type : PaneContentType,
        item: PaneContentItem,
        modifiers: Set<EventModifier>,
        pane: PaneViewBackend<*>,
        group: ContentPaneGroupViewBackend
    ) {
        pane.load(item, modifiers)
        group.load(pane)
        updateUrl(type, item)
    }

    fun addGroupContentPane(
        type: PaneContentType,
        item: PaneContentItem,
        modifiers: Set<EventModifier>,
        pane: PaneViewBackend<*>
    ) {

        val safeGroup = lastActiveContentPaneGroup

        if (safeGroup == null || safeGroup.isSingular) {

            ContentPaneGroupViewBackend(UUID(), this, pane).also {
                lastActiveContentPaneGroup = it
                loadContentPane(type, item, modifiers, pane, it)
                contentPaneGroups.value = listOf(it)
            }

        } else {

            safeGroup.panes += pane
            loadContentPane(type, item, modifiers, pane, safeGroup)

        }

        return
    }

    fun removePaneGroup(group: ContentPaneGroupViewBackend) {
        ContentPaneGroupViewBackend(UUID(), this, UnitPaneViewBackend(this, noContentPane)).also {
            lastActiveContentPaneGroup = it
            contentPaneGroups.value = listOf(it)
        }
    }

    // --------------------------------------------------------------------------------
    // Item type management
    // --------------------------------------------------------------------------------

    private val itemTypes = mutableMapOf<PaneContentType, WsItemConfig>()

    /**
     * Add a URL resolver which is able to resolve the given URL into an item
     * to be loaded into a content pane.
     */
    fun addUrlResolver(resolver: MultiPaneUrlResolver) {
        urlResolvers += resolver
    }

    /**
     * Resolve a URL to a workspace item to load into a content pane.
     */
    fun resolveUrl(url: String): Pair<PaneContentType,Any>? {
        val navState = NavState.parse(url)
        if (navState.segments.isEmpty()) return null

        for (resolver in urlResolvers) {
            val typeAndItem = resolver.resolve(navState)
            if (typeAndItem != null) return typeAndItem
        }

        return null
    }

    fun loadUrl(url: String) {
        resolveUrl(url)?.let {
            addContent(it.first, it.second, emptySet())
        }
    }

    /**
     * Update the application URL if there is any resolver that can turn the item
     * into one.
     */
    fun updateUrl(
        type: PaneContentType,
        item : PaneContentItem
    ) {
        for (resolver in urlResolvers) {
            val navState = resolver.toNavState(type, item) ?: continue
            application.setUrl(navState.toUrl())
            break
        }
    }

    fun addItemConfig(type: PaneContentType, icon: GraphicsResourceSet, tooltip: String? = null) {
        itemTypes[type] = WsItemConfig(type, icon, tooltip)
    }

    fun getItemConfig(type: PaneContentType) = itemTypes[type] ?: WsItemConfig.DEFAULT

    // --------------------------------------------------------------------------------
    // User feedback
    // --------------------------------------------------------------------------------

    class FeedbackStrings(
        val success: String,
        val fail: String
    )

    open fun execute(
        success: String,
        fail: String,
        action: suspend () -> Unit
    ) {
        execute(context = FeedbackStrings(success, fail), block = action)
    }

    override fun onSuccess(context: Any?) {
        successNotification(
            if (context is FeedbackStrings) context.success else Strings.saveSuccess
        )
    }

    override fun onFail(context: Any?, ex: Exception) {
        failNotification(
            if (context is FeedbackStrings) context.fail else Strings.saveFail
        )
    }

}