package `fun`.adaptive.iot.history.ui

import `fun`.adaptive.adaptive_lib_iot.generated.resources.database
import `fun`.adaptive.foundation.value.adaptiveStoreFor
import `fun`.adaptive.general.ObservableListener
import `fun`.adaptive.iot.device.DeviceMarkers
import `fun`.adaptive.iot.point.PointMarkers
import `fun`.adaptive.iot.space.SpaceMarkers
import `fun`.adaptive.iot.ws.AioWsContext.Companion.WSIT_HISTORY
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.workspace.WithWorkspace
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.ui.AvNameCache
import `fun`.adaptive.value.ui.AvUiTree
import `fun`.adaptive.value.ui.iconFor

class HistoryToolController(
    override val workspace: Workspace
) : WsPaneController<Unit>(), WithWorkspace, ObservableListener<AvNameCache> {

    enum class Mode {
        SPACE, DEVICE
    }

    var mode = Mode.SPACE

    val hisItems = adaptiveStoreFor(
        AvNameCache(
            workspace.backend,
            workspace.transport,
            workspace.scope,
            PointMarkers.HIS
        )
    ).also { it.addListener(this) }

    val treeViewModel = TreeViewModel<AvValueId, HistoryToolController>(
        emptyList(),
        selectedFun = ::selectedFun,
        multiSelect = false,
        context = this
    )

    val spacesTop = adaptiveStoreFor(emptyList<TreeItem<AvValueId>>())

    val spaceTreeStore = AvUiTree(
        backend,
        transport,
        scope,
        SpaceMarkers.SPACE,
        SpaceMarkers.SUB_SPACES,
        SpaceMarkers.TOP_SPACES
    ) {
        spacesTop.value = it
        if (mode == Mode.SPACE) {
            treeViewModel.items = it
            patchTree()
        }
    }

    var devicesTop = adaptiveStoreFor(emptyList<TreeItem<AvValueId>>())

    val deviceTreeStore = AvUiTree(
        backend,
        transport,
        scope,
        DeviceMarkers.DEVICE,
        DeviceMarkers.SUB_DEVICES,
        DeviceMarkers.TOP_DEVICES
    ) {
        devicesTop.value = it
        if (mode == Mode.DEVICE) {
            treeViewModel.items = it
            patchTree()
        }
    }

    fun start() {
        hisItems.value.start()
        spaceTreeStore.start()
        deviceTreeStore.start()
    }

    fun expandAll() {
        treeViewModel.items.forEach { it.expandAll() }
    }

    fun collapseAll() {
        treeViewModel.items.forEach { it.collapseAll() }
    }

    fun showSpaces() {
        mode = Mode.SPACE
        treeViewModel.items = spacesTop.value
        patchTree()
    }

    fun showDevices() {
        mode = Mode.DEVICE
        treeViewModel.items = devicesTop.value
        patchTree()
    }

    fun selectedFun(viewModel: HistoryTreeModel, treeItem: TreeItem<AvValueId>, modifiers: Set<EventModifier>) {
        val item = (treeItem.attachment as? AvItem<*>) ?: return

        if (PointMarkers.HIS in item.markers) {
            workspace.addContent(
                HistoryBrowserWsItem(item.name, WSIT_HISTORY, item),
                emptySet()
            )
        }

        TreeViewModel.defaultSelectedFun(viewModel, treeItem, modifiers)
    }

    override fun onChange(value: AvNameCache) {
        patchTree()
    }

    fun patchTree() {
        val tops = if (mode == Mode.SPACE) spacesTop.value else devicesTop.value

        val hisParentMap = mutableMapOf<AvValueId, MutableList<AvItem<*>>>()

        for (hisItem in hisItems.value.value) {
            val parentId = hisItem.item.parentId ?: continue
            val parentMap = hisParentMap.getOrPut(parentId) { mutableListOf() }
            parentMap += hisItem.item
        }

        val newTops = tops.map { top -> patch(top, hisParentMap) }

        if (mode == Mode.SPACE) {
            spacesTop.value = newTops
        } else {
            devicesTop.value = newTops
        }
    }

    fun patch(treeItem: TreeItem<AvValueId>, hisParentMap: MutableMap<AvValueId, MutableList<AvItem<*>>>): TreeItem<AvValueId> {
        val hisPoints = hisParentMap[treeItem.data]

        // mapNotNull filters out the points which we will add in the next step

        val treeChildren = treeItem.children.mapNotNull {
            if (it.attachment == null) {
                patch(it, hisParentMap)
            } else {
                null
            }
        }

        if (hisPoints == null) {
            return treeItem
        }

        val hisTreeItems = hisPoints
            .sortedBy { it.name }
            .map { hisPoint ->
                TreeItem<AvValueId>(
                    Graphics.database,
                    hisPoint.name,
                    hisPoint.uuid,
                    parent = treeItem
                ).also {
                    it.attachment = hisPoint
                }
            }

        treeItem.children = hisTreeItems + treeChildren

        return treeItem
    }

}