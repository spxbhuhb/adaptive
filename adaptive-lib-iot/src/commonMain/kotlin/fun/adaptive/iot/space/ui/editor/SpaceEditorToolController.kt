package `fun`.adaptive.iot.space.ui.editor

import `fun`.adaptive.adaptive_lib_iot.generated.resources.humidity
import `fun`.adaptive.adaptive_lib_iot.generated.resources.temperature
import `fun`.adaptive.iot.haystack.PhScienceMarkers
import `fun`.adaptive.iot.point.AioPointApi
import `fun`.adaptive.iot.point.PointMarkers
import `fun`.adaptive.iot.point.computed.AioComputedPointSpec
import `fun`.adaptive.iot.space.ui.AbstractSpaceToolController
import `fun`.adaptive.iot.space.ui.SpaceTreeModel
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.value.AvValueId

class SpaceEditorToolController(
    workspace: Workspace
) : AbstractSpaceToolController(workspace) {

    val pointService = getService<AioPointApi>(transport)

    override fun selectedFun(viewModel: SpaceTreeModel, treeItem: TreeItem<AvValueId>, modifiers: Set<EventModifier>) {
        val item = valueTreeStore[treeItem.data] ?: return
        workspace.addContent(item, modifiers)
        TreeViewModel.defaultSelectedFun(viewModel, treeItem, modifiers)
    }

    fun addSpace(name: String, marker: String, parentId: AvValueId?) {
        io {
            val spaceId = spaceService.add(name, marker, parentId)

            pointService.add(
                Strings.temperature,
                PointMarkers.COMPUTED_POINT,
                spaceId,
                AioComputedPointSpec(dependencyMarker = PhScienceMarkers.TEMP),
                listOf(PhScienceMarkers.TEMP)
            )

            pointService.add(
                Strings.humidity,
                PointMarkers.COMPUTED_POINT,
                spaceId,
                AioComputedPointSpec(dependencyMarker = PhScienceMarkers.HUMIDITY),
                listOf(PhScienceMarkers.HUMIDITY)
            )
        }
    }

    fun moveUp(id: AvValueId) {
        io { spaceService.moveUp(id) }
    }

    fun moveDown(id: AvValueId) {
        io { spaceService.moveDown(id) }
    }

}