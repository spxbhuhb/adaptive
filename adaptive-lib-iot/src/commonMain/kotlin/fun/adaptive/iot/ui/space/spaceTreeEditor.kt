package `fun`.adaptive.iot.ui.space

import `fun`.adaptive.adaptive_lib_iot.generated.resources.*
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.model.space.AioSpace
import `fun`.adaptive.iot.model.space.AioSpaceEditOperation
import `fun`.adaptive.iot.model.space.AioSpaceType
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.zIndex
import `fun`.adaptive.ui.builtin.*
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.menu.MenuItemBase
import `fun`.adaptive.ui.menu.MenuSeparator
import `fun`.adaptive.ui.menu.contextMenu
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.tree
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPaneAction
import `fun`.adaptive.ui.workspace.model.WsPaneMenuAction
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

fun wsSpaceTreeEditor() =
    WsPane(
        UUID(),
        Strings.areas,
        Graphics.apartment,
        WsPanePosition.RightTop,
        AioWsContext.WSPANE_SPACE_TOOL,
        actions = listOf(
            WsPaneAction(Graphics.expand_all, Strings.expandAll, Unit) { w, p, d -> spaceToolExpandAll(w, p) },
            WsPaneAction(Graphics.collapse_all, Strings.collapseAll, Unit) { w, p, d -> spaceToolCollapseAll(w, p)},
            WsPaneMenuAction(Graphics.add, Strings.addArea, addTopMenu, ::applyToolMenuAction)
        ),
        model = Unit
    )

private fun spaceToolExpandAll(workspace: Workspace, @Suppress("unused") pane: WsPane<*>) {
    workspace.firstContext<AioWsContext>()?.spaceTree?.items?.forEach { it.expandAll() }
}

private fun spaceToolCollapseAll(workspace: Workspace, @Suppress("unused") pane: WsPane<*>) {
    workspace.firstContext<AioWsContext>()?.spaceTree?.items?.forEach { it.collapseAll() }
}

@Adaptive
fun spaceTreeEditor(spaceTree: SpaceTreeModel) {

    val observed = valueFrom { spaceTree }

    tree(observed, ::contextMenuBuilder)

}

private fun applyToolMenuAction(
    workspace: Workspace,
    @Suppress("unused") pane: WsPane<*>,
    menuItem: MenuItem<AioSpaceEditOperation>,
    @Suppress("unused") modifiers: Set<EventModifier>
) {
    val tree = workspace.firstContext<AioWsContext>()?.spaceTree ?: return
    apply(tree, menuItem, null)
}

private fun apply(tree: SpaceTreeModel, menuItem: MenuItem<AioSpaceEditOperation>, treeItem: TreeItem<AioSpace>?) {
    val projectId = tree.context.projectId
    val item = treeItem?.data
    val itemId = item?.uuid
    val displayOrder = (treeItem?.children?.lastOrNull()?.data?.displayOrder ?: 0) + 1
    val context = tree.context

    context.io {
        val space: AioSpace?

        when (menuItem.data) {
            AioSpaceEditOperation.AddSite -> {
                space = context.spaceService.add(projectId, itemId, AioSpaceType.Site, displayOrder)
            }

            AioSpaceEditOperation.AddBuilding -> {
                space = context.spaceService.add(projectId, itemId, AioSpaceType.Building, displayOrder)
            }

            AioSpaceEditOperation.AddFloor -> {
                space = context.spaceService.add(projectId, itemId, AioSpaceType.Floor, displayOrder)
            }

            AioSpaceEditOperation.AddRoom -> {
                space = context.spaceService.add(projectId, itemId, AioSpaceType.Room, displayOrder)
            }

            AioSpaceEditOperation.AddArea -> {
                space = context.spaceService.add(projectId, itemId, AioSpaceType.Area, displayOrder)
            }

            AioSpaceEditOperation.MoveUp -> {
                check(treeItem != null)
                move(context, tree, treeItem, -1)
                space = null
            }

            AioSpaceEditOperation.MoveDown -> {
                check(treeItem != null)
                move(context, tree, treeItem, 1)
                space = null
            }

            AioSpaceEditOperation.Inactivate -> {
                space = null
            }
        }

        if (space != null) {
            val newItem = space.toTreeItem(treeItem)

            if (treeItem != null) {
                treeItem.children += newItem
                if (! treeItem.open) treeItem.open = true
            } else {
                tree.items += newItem
            }
        }
    }
}

private fun move(
    context: AioWsContext,
    tree: SpaceTreeModel,
    treeItem: TreeItem<AioSpace>,
    offset : Int
) {
    val parent = treeItem.parent
    val children = parent?.children ?: tree.items

    val index = children.indexOfFirst { it.data.uuid == treeItem.data.uuid }
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

    val itemDisplayOrder = item.data.displayOrder
    val siblingDisplayOrder = sibling.data.displayOrder

    val newSiblingSpace = sibling.data.copy(displayOrder = itemDisplayOrder)
    val newItemSpace = item.data.copy(displayOrder = siblingDisplayOrder)

    sibling.data = newSiblingSpace
    item.data = newItemSpace

    context.io {
        context.spaceService.update(newItemSpace)
        context.spaceService.update(newSiblingSpace)
    }
}

private val addTopMenu = listOf(
    MenuItem<AioSpaceEditOperation>(Graphics.responsive_layout, Strings.addSite, AioSpaceEditOperation.AddSite),
    MenuItem<AioSpaceEditOperation>(Graphics.apartment, Strings.addBuilding, AioSpaceEditOperation.AddBuilding),
    MenuItem<AioSpaceEditOperation>(Graphics.crop_5_4, Strings.addArea, AioSpaceEditOperation.AddArea),
)

private val siteMenu = listOf(
    MenuItem<AioSpaceEditOperation>(Graphics.apartment, Strings.addBuilding, AioSpaceEditOperation.AddBuilding),
    MenuItem<AioSpaceEditOperation>(Graphics.crop_5_4, Strings.addArea, AioSpaceEditOperation.AddArea),
)

private val buildingMenu = listOf(
    MenuItem<AioSpaceEditOperation>(Graphics.stacks, Strings.addFloor, AioSpaceEditOperation.AddFloor),
    MenuItem<AioSpaceEditOperation>(Graphics.crop_5_4, Strings.addArea, AioSpaceEditOperation.AddArea),
)

private val floorMenu = listOf(
    MenuItem<AioSpaceEditOperation>(Graphics.meeting_room, Strings.addRoom, AioSpaceEditOperation.AddRoom),
    MenuItem<AioSpaceEditOperation>(Graphics.crop_5_4, Strings.addArea, AioSpaceEditOperation.AddArea),
)

private val roomMenu = listOf(
    MenuItem<AioSpaceEditOperation>(Graphics.crop_5_4, Strings.addArea, AioSpaceEditOperation.AddArea),
)

private fun menu(viewModel: SpaceTreeModel, treeItem: TreeItem<AioSpace>): List<MenuItemBase<AioSpaceEditOperation>> {

    val base =
        when (treeItem.data.spaceType) {
            AioSpaceType.Site -> siteMenu
            AioSpaceType.Building -> buildingMenu
            AioSpaceType.Floor -> floorMenu
            AioSpaceType.Room -> roomMenu
            AioSpaceType.Area -> roomMenu
        }

    val out = mutableListOf<MenuItemBase<AioSpaceEditOperation>>()
    out.addAll(base)

    out += MenuSeparator<AioSpaceEditOperation>()

    out += MenuItem<AioSpaceEditOperation>(
        Graphics.arrow_drop_up, Strings.moveUp, AioSpaceEditOperation.MoveUp,
        inactive = isFirst(viewModel, treeItem)
    )

    out += MenuItem<AioSpaceEditOperation>(
        Graphics.arrow_drop_down, Strings.moveDown, AioSpaceEditOperation.MoveDown,
        inactive = isLast(viewModel, treeItem)
    )

    out += MenuSeparator<AioSpaceEditOperation>()

    out += MenuItem<AioSpaceEditOperation>(null, Strings.inactivate, AioSpaceEditOperation.Inactivate)

    return out
}

private fun isFirst(viewModel: SpaceTreeModel, treeItem: TreeItem<AioSpace>): Boolean {
    val safeParent = treeItem.parent
    if (safeParent == null) {
        return viewModel.items.first() == treeItem
    } else {
        return safeParent.children.first() == treeItem
    }
}

private fun isLast(viewModel: SpaceTreeModel, treeItem: TreeItem<AioSpace>): Boolean {
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
    treeItem: TreeItem<AioSpace>
): AdaptiveFragment {
    column {
        zIndex { 200 }
        contextMenu(menu(viewModel, treeItem)) { menuItem, _ -> apply(viewModel, menuItem, treeItem); hide() }
    }
    return fragment()
}