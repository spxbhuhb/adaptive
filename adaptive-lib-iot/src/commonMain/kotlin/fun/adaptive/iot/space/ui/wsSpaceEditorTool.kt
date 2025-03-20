package `fun`.adaptive.iot.space.ui

import `fun`.adaptive.adaptive_lib_iot.generated.resources.*
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.item.AioItem
import `fun`.adaptive.iot.space.AioSpaceType
import `fun`.adaptive.iot.space.ui.model.AioSpaceEditOperation
import `fun`.adaptive.iot.space.ui.model.SpaceToolConfig
import `fun`.adaptive.iot.space.ui.model.SpaceToolState
import `fun`.adaptive.iot.value.AioValueId
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.zIndex
import `fun`.adaptive.ui.builtin.*
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.menu.MenuItemBase
import `fun`.adaptive.ui.menu.contextMenu
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.tree
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPaneAction
import `fun`.adaptive.ui.workspace.model.WsPaneMenuAction
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.ui.workspace.wsToolPane
import `fun`.adaptive.utility.UUID

@Adaptive
fun wsSpaceEditorTool(pane: WsPane<SpaceToolState>): AdaptiveFragment {

    val observed = valueFrom { pane.model.treeViewModel }

    wsToolPane(pane) {
        tree(observed, ::contextMenuBuilder)
    }

    return fragment()
}

fun wsSpaceEditorToolDef(context: AioWsContext) : WsPane<SpaceToolState> {

    val state = SpaceToolState(
        context.workspace, SpaceToolConfig(Strings.areas, Graphics.apartment)
    )

    val pane = WsPane(
        UUID(),
        Strings.areas,
        Graphics.apartment,
        WsPanePosition.RightTop,
        AioWsContext.WSPANE_SPACE_TOOL,
        actions = listOf(
            WsPaneAction(Graphics.zoom_out_map, Strings.expandAll, Unit) { state.expandAll() },
            WsPaneAction(Graphics.arrows_input, Strings.collapseAll, Unit) { state.collapseAll() },
            WsPaneMenuAction(Graphics.add, Strings.add, addTopMenu, { apply(state, it.menuItem, null) })
        ),
        model = state
    )

    context.io {
        state.start()
    }

    return pane
}

private fun apply(state: SpaceToolState, menuItem: MenuItem<AioSpaceEditOperation>, treeItem: TreeItem<AioValueId>?) {

    val addType = when (menuItem.data) {
        AioSpaceEditOperation.AddSite -> AioSpaceType.Site
        AioSpaceEditOperation.AddBuilding -> AioSpaceType.Building
        AioSpaceEditOperation.AddFloor -> AioSpaceType.Floor
        AioSpaceEditOperation.AddRoom -> AioSpaceType.Room
        AioSpaceEditOperation.AddArea -> AioSpaceType.Area
        else -> null
    }

    if (addType != null) {
        // TODO context.addSpace(treeItem, itemId, addType, displayOrder)
        return
    }

    check(treeItem != null)

    state.workspace.io {
        when (menuItem.data) {
            AioSpaceEditOperation.MoveUp -> move(state, treeItem, - 1)
            AioSpaceEditOperation.MoveDown -> move(state, treeItem, 1)
            AioSpaceEditOperation.Inactivate -> Unit
            else -> Unit
        }
    }
}

private fun move(
    state: SpaceToolState,
    treeItem: TreeItem<AioValueId>,
    offset: Int
) {
    val tree = state.treeViewModel

    val parent = treeItem.parent
    val children = parent?.children ?: tree.items

    val index = children.indexOfFirst { it.data == treeItem.data }
    if (offset < 0 && index < 1) return
    if (offset > 0 && index == children.lastIndex) return

    val item = children[index]
    val sibling = children[index + offset]

    val newList = children.toMutableList()
    newList[index] = sibling
    newList[index + offset] = item

    if (parent != null) {
        parent.children = newList
    } else {
        tree.items = newList
    }

//    val itemDisplayOrder = item.data.displayOrder
//    val siblingDisplayOrder = sibling.data.displayOrder
//
//    val newSiblingSpace = sibling.data.copy(displayOrder = itemDisplayOrder)
//    val newItemSpace = item.data.copy(displayOrder = siblingDisplayOrder)
//
//    sibling.data = newSiblingSpace
//    item.data = newItemSpace

    state.workspace.io {
        // TODO context.spaceService.update(newItemSpace)
        // TODO context.spaceService.update(newSiblingSpace)
    }
}

private val addSite = MenuItem<AioSpaceEditOperation>(Graphics.responsive_layout, Strings.addSite, AioSpaceEditOperation.AddSite)
private val addBuilding = MenuItem<AioSpaceEditOperation>(Graphics.apartment, Strings.addBuilding, AioSpaceEditOperation.AddBuilding)
private val addFloor = MenuItem<AioSpaceEditOperation>(Graphics.stacks, Strings.addFloor, AioSpaceEditOperation.AddFloor)
private val addRoom = MenuItem<AioSpaceEditOperation>(Graphics.meeting_room, Strings.addRoom, AioSpaceEditOperation.AddRoom)
private val addArea = MenuItem<AioSpaceEditOperation>(Graphics.crop_5_4, Strings.addArea, AioSpaceEditOperation.AddArea)

private val addTopMenu = listOf(addSite, addBuilding, addArea)
private val siteMenu = listOf(addBuilding, addArea)
private val buildingMenu = listOf(addFloor, addArea)
private val floorMenu = listOf(addRoom, addArea)
private val areaMenu = listOf(addArea)
private val roomMenu = listOf(addArea)

private fun menu(viewModel: SpaceTreeModel, treeItem: TreeItem<AioValueId>): List<MenuItemBase<AioSpaceEditOperation>> {

    TODO()
//    val markers = treeItem.data.markersOrNull
//
//    val base =
//        when {
//            SpaceMarkers.SITE in markers -> siteMenu
//            SpaceMarkers.BUILDING in markers -> buildingMenu
//            SpaceMarkers.FLOOR in markers -> floorMenu
//            SpaceMarkers.ROOM in markers -> roomMenu
//            SpaceMarkers.AREA in markers -> areaMenu
//            else -> addTopMenu
//        }
//
//    val out = mutableListOf<MenuItemBase<AioSpaceEditOperation>>()
//    out.addAll(base)
//
//    out += MenuSeparator<AioSpaceEditOperation>()
//
//    out += MenuItem<AioSpaceEditOperation>(
//        Graphics.arrow_drop_up, Strings.moveUp, AioSpaceEditOperation.MoveUp,
//        inactive = isFirst(viewModel, treeItem)
//    )
//
//    out += MenuItem<AioSpaceEditOperation>(
//        Graphics.arrow_drop_down, Strings.moveDown, AioSpaceEditOperation.MoveDown,
//        inactive = isLast(viewModel, treeItem)
//    )
//
//    out += MenuSeparator<AioSpaceEditOperation>()
//
//    out += MenuItem<AioSpaceEditOperation>(null, Strings.inactivate, AioSpaceEditOperation.Inactivate)
//
//    return out
}

private fun isFirst(viewModel: SpaceTreeModel, treeItem: TreeItem<AioItem>): Boolean {
    val safeParent = treeItem.parent
    if (safeParent == null) {
        return viewModel.items.first() == treeItem
    } else {
        return safeParent.children.first() == treeItem
    }
}

private fun isLast(viewModel: SpaceTreeModel, treeItem: TreeItem<AioItem>): Boolean {
    val safeParent = treeItem.parent
    if (safeParent == null) {
        return viewModel.items.last() == treeItem
    } else {
        return safeParent.children.last() == treeItem
    }
}

@Adaptive
private fun contextMenuBuilder(
    hide: () -> Unit,
    viewModel: SpaceTreeModel,
    treeItem: TreeItem<AioValueId>
): AdaptiveFragment {
    column {
        zIndex { 200 }
        contextMenu(menu(viewModel, treeItem)) { menuItem, _ -> apply(viewModel.context, menuItem, treeItem); hide() }
    }
    return fragment()
}