package `fun`.adaptive.iot.history.ui

import `fun`.adaptive.foundation.value.adaptiveStoreFor
import `fun`.adaptive.general.ObservableListener
import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.iot.device.DeviceMarkers
import `fun`.adaptive.iot.device.publish.AioDeviceTreePublishApi
import `fun`.adaptive.iot.device.ui.DeviceItems
import `fun`.adaptive.iot.generated.resources.database
import `fun`.adaptive.iot.generated.resources.historicalData
import `fun`.adaptive.iot.point.AioPointSpec
import `fun`.adaptive.iot.point.PointMarkers
import `fun`.adaptive.iot.space.AioSpaceSpec
import `fun`.adaptive.iot.space.SpaceMarkers
import `fun`.adaptive.iot.space.publish.AioSpaceTreePublishApi
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.value.AvNameCache
import `fun`.adaptive.ui.value.AvUiTree
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.item.AvItem

class HistoryToolController(
    override val workspace: Workspace
) : WsPaneController<Unit>(), ObservableListener<AvNameCache> {

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
        multiSelect = true,
        context = this
    )

    val spacesTop = adaptiveStoreFor(emptyList<TreeItem<AvValueId>>())

    val spaceTreeStore = AvUiTree(
        getService<AioSpaceTreePublishApi>(transport),
        backend,
        AioSpaceSpec::class,
        SpaceMarkers.TOP_SPACES,
        SpaceMarkers.SUB_SPACES
    )

    var devicesTop = adaptiveStoreFor(emptyList<TreeItem<AvValueId>>())

    val deviceTreeStore = AvUiTree(
        getService<AioDeviceTreePublishApi>(transport),
        backend,
        AioDeviceSpec::class,
        DeviceMarkers.TOP_DEVICES,
        DeviceMarkers.SUB_DEVICES
    )

    fun start() {
        hisItems.value.start()

        spaceTreeStore.addListener {
            spacesTop.value = it
            if (mode == Mode.SPACE) {
                treeViewModel.items = it
                patchTree()
            }
        }

        deviceTreeStore.addListener {
            devicesTop.value = it
            if (mode == Mode.DEVICE) {
                treeViewModel.items = it
                patchTree()
            }
        }
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

        TreeViewModel.defaultSelectedFun(viewModel, treeItem, modifiers)

        val items = viewModel.selection.map { selectedItem ->
            val attachment = selectedItem.attachment as? AvItem<*>
            attachment?.let { listOf(it) } ?: selectedItem.children.mapNotNull { it.attachment as? AvItem<*> }
        }.flatten()

        val points = items.filter { PointMarkers.HIS in it.markers }
        if (points.isEmpty()) return

        val first = points.firstOrNull()

        if (first == null) return

        val name = if (items.size == 1) first.name else Strings.historicalData

        workspace.addContent(
            HistoryBrowserWsItem(name, DeviceItems.WSIT_HISTORY, this, items),
            emptySet()
        )

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
                val specific = hisPoint.spec as AioPointSpec
                TreeItem<AvValueId>(
                    Graphics.database,
                    hisPoint.name + " " + specific.displayAddress,
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