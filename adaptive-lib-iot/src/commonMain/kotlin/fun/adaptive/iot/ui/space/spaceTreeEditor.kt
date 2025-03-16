package `fun`.adaptive.iot.ui.space

import `fun`.adaptive.adaptive_lib_iot.generated.resources.*
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.model.project.AioProject
import `fun`.adaptive.iot.model.space.AioSpace
import `fun`.adaptive.iot.model.space.AioSpaceEditOperation
import `fun`.adaptive.iot.model.space.AioSpaceType
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.builtin.*
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.denseIconTheme
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.menu.MenuItemBase
import `fun`.adaptive.ui.menu.MenuSeparator
import `fun`.adaptive.ui.menu.contextMenu
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewModel
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

private fun spaceToolExpandAll(workspace: Workspace, pane: WsPane<*>) {
    workspace.firstContext<AioWsContext>()?.spaceTree?.items?.forEach { it.expandAll() }
}

private fun spaceToolCollapseAll(workspace: Workspace, pane: WsPane<*>) {
    workspace.firstContext<AioWsContext>()?.spaceTree?.items?.forEach { it.collapseAll() }
}

@Adaptive
fun spaceTreeEditor(spaceTree: TreeViewModel<AioSpace, AioProject>) {

    val observed = valueFrom { spaceTree }

    row {
        maxWidth .. spaceBetween .. alignItems.center

        text(Strings.areas)

        row {
            actionIcon(Graphics.expand_all, theme = denseIconTheme) .. onClick { observed.items.forEach { it.expandAll() } }
            actionIcon(Graphics.collapse_all, theme = denseIconTheme) .. onClick { observed.items.forEach { it.collapseAll() } }
            box {
                actionIcon(Graphics.add, theme = denseIconTheme)
                primaryPopup { hide ->
                    contextMenu(addTopMenu) { menuItem, _ -> ; hide() }
                }
            }
        }

    }

    tree(observed, ::contextMenuBuilder)

}

private fun applyToolMenuAction(
    workspace: Workspace,
    pane: WsPane<*>,
    menuItem: MenuItem<AioSpaceEditOperation>,
    modifiers: Set<EventModifier>
) {
    val tree = workspace.firstContext<AioWsContext>()?.spaceTree ?: return
    apply(tree, menuItem, null)
}

private fun apply(tree: TreeViewModel<AioSpace, AioProject>, menuItem: MenuItem<AioSpaceEditOperation>, treeItem: TreeItem<AioSpace>?) {
    val projectId = tree.context.uuid
    val space: AioSpace?

    when (menuItem.data) {
        AioSpaceEditOperation.AddSite -> {
            space = AioSpace(
                uuid = UUID(),
                projectId = projectId,
                friendlyId = "",
                spaceType = AioSpaceType.Site,
                name = "${Strings.site} ${tree.items.size + 1}"
            )
        }

        AioSpaceEditOperation.AddBuilding -> {
            space = AioSpace(
                uuid = UUID(),
                projectId = projectId,
                friendlyId = "",
                spaceType = AioSpaceType.Building,
                name = "${Strings.building} ${tree.items.size + 1}"
            )
        }

        AioSpaceEditOperation.AddFloor -> {
            space = AioSpace(
                uuid = UUID(),
                projectId = projectId,
                friendlyId = "",
                spaceType = AioSpaceType.Floor,
                name = "${Strings.floor} ${tree.items.size + 1}"
            )
        }

        AioSpaceEditOperation.AddRoom -> {
            space = AioSpace(
                uuid = UUID(),
                projectId = projectId,
                friendlyId = "",
                spaceType = AioSpaceType.Room,
                name = "${Strings.room} ${tree.items.size + 1}"
            )
        }

        AioSpaceEditOperation.AddArea -> {
            space = AioSpace(
                uuid = UUID(),
                projectId = projectId,
                friendlyId = "",
                spaceType = AioSpaceType.Area,
                name = "${Strings.area} ${tree.items.size + 1}"
            )
        }

        AioSpaceEditOperation.MoveUp -> {
            space = null
        }

        AioSpaceEditOperation.MoveDown -> {
            space = null
        }

        AioSpaceEditOperation.Inactivate -> TODO()
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

private fun menu(viewModel: TreeViewModel<AioSpace, AioProject>, treeItem: TreeItem<AioSpace>): List<MenuItemBase<AioSpaceEditOperation>> {

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

private fun isFirst(viewModel: TreeViewModel<AioSpace, AioProject>, treeItem: TreeItem<AioSpace>): Boolean {
    val safeParent = treeItem.parent
    if (safeParent == null) {
        return viewModel.items.first() == treeItem
    } else {
        return safeParent.children.first() == treeItem
    }
}

private fun isLast(viewModel: TreeViewModel<AioSpace, AioProject>, treeItem: TreeItem<AioSpace>): Boolean {
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
    viewModel: TreeViewModel<AioSpace, AioProject>,
    treeItem: TreeItem<AioSpace>
): AdaptiveFragment {
    column {
        zIndex { 200 }
        contextMenu(menu(viewModel, treeItem)) { menuItem, _ -> apply(viewModel, menuItem, treeItem); hide() }
    }
    return fragment()
}