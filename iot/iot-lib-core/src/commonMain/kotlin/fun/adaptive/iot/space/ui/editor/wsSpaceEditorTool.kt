package `fun`.adaptive.iot.space.ui.editor

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.app.IotWsModule
import `fun`.adaptive.iot.generated.resources.*
import `fun`.adaptive.iot.space.SpaceMarkers
import `fun`.adaptive.iot.space.ui.SpaceTreeModel
import `fun`.adaptive.iot.device.ui.DeviceItems
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.zIndex
import `fun`.adaptive.ui.generated.resources.*
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.menu.MenuItemBase
import `fun`.adaptive.ui.menu.MenuSeparator
import `fun`.adaptive.ui.menu.contextMenu
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.tree
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPaneAction
import `fun`.adaptive.ui.workspace.model.WsPaneMenuAction
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.ui.workspace.wsToolPane
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.AvValueId

@Adaptive
fun wsSpaceEditorTool(pane: WsPane<Unit, SpaceEditorToolController>): AdaptiveFragment {

    val observed = valueFrom { pane.controller.treeViewModel }

    wsToolPane(pane) {
        tree(observed, ::contextMenuBuilder)
    }

    return fragment()
}

internal fun apply(controller: SpaceEditorToolController, menuItem: MenuItem<AioSpaceEditOperation>, treeItem: TreeItem<AvValueId>?) {

    val (name, marker) = when (menuItem.data) {
        AioSpaceEditOperation.AddSite -> Strings.site to SpaceMarkers.SITE
        AioSpaceEditOperation.AddBuilding -> Strings.building to SpaceMarkers.BUILDING
        AioSpaceEditOperation.AddFloor -> Strings.floor to SpaceMarkers.FLOOR
        AioSpaceEditOperation.AddRoom -> Strings.room to SpaceMarkers.ROOM
        AioSpaceEditOperation.AddArea -> Strings.area to SpaceMarkers.AREA
        else -> null to null
    }

    if (name != null && marker != null) {
        controller.addSpace(name, marker, treeItem?.data)
        return
    }

    check(treeItem != null)

    when (menuItem.data) {
        AioSpaceEditOperation.MoveUp -> controller.moveUp(treeItem.data)
        AioSpaceEditOperation.MoveDown -> controller.moveDown(treeItem.data)
        AioSpaceEditOperation.Inactivate -> Unit
        else -> Unit
    }

}

private val addSite = MenuItem<AioSpaceEditOperation>(Graphics.responsive_layout, Strings.addSite, AioSpaceEditOperation.AddSite)
private val addBuilding = MenuItem<AioSpaceEditOperation>(Graphics.apartment, Strings.addBuilding, AioSpaceEditOperation.AddBuilding)
private val addFloor = MenuItem<AioSpaceEditOperation>(Graphics.stacks, Strings.addFloor, AioSpaceEditOperation.AddFloor)
private val addRoom = MenuItem<AioSpaceEditOperation>(Graphics.meeting_room, Strings.addRoom, AioSpaceEditOperation.AddRoom)
private val addArea = MenuItem<AioSpaceEditOperation>(Graphics.crop_5_4, Strings.addArea, AioSpaceEditOperation.AddArea)

internal val addTopMenu = listOf(addSite, addBuilding, addArea)
private val siteMenu = listOf(addBuilding, addArea)
private val buildingMenu = listOf(addFloor, addArea)
private val floorMenu = listOf(addRoom, addArea)
private val areaMenu = listOf(addArea)
private val roomMenu = listOf(addArea)

private fun menu(viewModel: SpaceTreeModel, treeItem: TreeItem<AvValueId>): List<MenuItemBase<AioSpaceEditOperation>> {

    val controller = viewModel.context
    val itemId = treeItem.data

    val markers = controller.valueTreeStore[itemId]?.markers?.keys ?: emptySet<SpaceMarkers>()

    val base =
        when {
            SpaceMarkers.SITE in markers -> siteMenu
            SpaceMarkers.BUILDING in markers -> buildingMenu
            SpaceMarkers.FLOOR in markers -> floorMenu
            SpaceMarkers.ROOM in markers -> roomMenu
            SpaceMarkers.AREA in markers -> areaMenu
            else -> addTopMenu
        }

    val out = mutableListOf<MenuItemBase<AioSpaceEditOperation>>()
    out.addAll(base)

    out += MenuSeparator<AioSpaceEditOperation>()

    val subSpaces = controller.valueTreeStore.getParentSubItems(itemId)

    out += MenuItem<AioSpaceEditOperation>(
        Graphics.arrow_drop_up, Strings.moveUp, AioSpaceEditOperation.MoveUp,
        inactive = (subSpaces.isEmpty() || subSpaces.first() == itemId)
    )

    out += MenuItem<AioSpaceEditOperation>(
        Graphics.arrow_drop_down, Strings.moveDown, AioSpaceEditOperation.MoveDown,
        inactive = (subSpaces.isEmpty() || subSpaces.last() == itemId)
    )

    out += MenuSeparator<AioSpaceEditOperation>()

    out += MenuItem<AioSpaceEditOperation>(null, Strings.inactivate, AioSpaceEditOperation.Inactivate)

    return out
}

@Adaptive
private fun contextMenuBuilder(
    hide: () -> Unit,
    viewModel: SpaceTreeModel,
    treeItem: TreeItem<AvValueId>
): AdaptiveFragment {
    column {
        zIndex { 200 }
        contextMenu(menu(viewModel, treeItem)) { menuItem, _ ->
            apply(viewModel.context as SpaceEditorToolController, menuItem, treeItem); hide()
        }
    }
    return fragment()
}