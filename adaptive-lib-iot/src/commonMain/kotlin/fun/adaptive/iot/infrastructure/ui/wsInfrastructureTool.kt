package `fun`.adaptive.iot.infrastructure.ui

import `fun`.adaptive.adaptive_lib_iot.generated.resources.*
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.infrastructure.AioInfrastructureItem
import `fun`.adaptive.iot.infrastructure.AioInfrastructureItemType
import `fun`.adaptive.iot.infrastructure.InfrastructureTreeModel
import `fun`.adaptive.iot.infrastructure.ui.model.AioInfrastructureEditOperation
import `fun`.adaptive.iot.space.AioSpace
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
import `fun`.adaptive.ui.workspace.Workspace.Companion.wsContext
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPaneAction
import `fun`.adaptive.ui.workspace.model.WsPaneMenuAction
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.ui.workspace.wsToolPane
import `fun`.adaptive.utility.UUID

@Adaptive
fun wsInfrastructureEditorPane(pane: WsPane<Unit>): AdaptiveFragment {

    val context = fragment().wsContext<AioWsContext>()
    val observed = valueFrom { context.infrastructureTree }

    wsToolPane(pane) {
        tree(observed, ::contextMenuBuilder)
    }

    return fragment()
}

fun wsInfrastructureEditorDef() =
    WsPane(
        UUID(),
        Strings.infrastructure,
        Graphics.account_tree,
        WsPanePosition.RightTop,
        AioWsContext.WSPANE_INFRASTRUCTURE_TOOL,
        actions = listOf(
            WsPaneAction(Graphics.zoom_out_map, Strings.expandAll, Unit) { w, p, d -> infrastructureToolExpandAll(w, p) },
            WsPaneAction(Graphics.arrows_input, Strings.collapseAll, Unit) { w, p, d -> infrastructureToolCollapseAll(w, p) },
            WsPaneMenuAction(Graphics.add, Strings.add, addTopMenu, ::applyToolMenuAction)
        ),
        model = Unit
    )

private fun infrastructureToolExpandAll(workspace: Workspace, @Suppress("unused") pane: WsPane<*>) {
    workspace.firstContext<AioWsContext>()?.infrastructureTree?.items?.forEach { it.expandAll() }
}

private fun infrastructureToolCollapseAll(workspace: Workspace, @Suppress("unused") pane: WsPane<*>) {
    workspace.firstContext<AioWsContext>()?.infrastructureTree?.items?.forEach { it.collapseAll() }
}

private fun applyToolMenuAction(
    workspace: Workspace,
    @Suppress("unused") pane: WsPane<*>,
    menuItem: MenuItem<AioInfrastructureEditOperation>,
    @Suppress("unused") modifiers: Set<EventModifier>
) {
    val tree = workspace.firstContext<AioWsContext>()?.infrastructureTree ?: return
    apply(tree, menuItem, null)
}

private fun apply(tree: InfrastructureTreeModel, menuItem: MenuItem<AioInfrastructureEditOperation>, treeItem: TreeItem<AioInfrastructureItem>?) {
    val item = treeItem?.data
    val itemId = item?.uuid
    val context = tree.context

//    val addType = when (menuItem.data) {
//        AioInfrastructureEditOperation.AddHost -> Aio
//        AioInfrastructureEditOperation.AddBuilding -> AioSpaceType.Building
//        AioInfrastructureEditOperation.AddFloor -> AioSpaceType.Floor
//        AioInfrastructureEditOperation.AddRoom -> AioSpaceType.Room
//        AioInfrastructureEditOperation.AddArea -> AioSpaceType.Area
//        else -> null
//    }
//
//    if (addType != null) {
//        context.addSpace(treeItem, itemId, addType, displayOrder)
//        return
//    }

}

private val addHost = MenuItem<AioInfrastructureEditOperation>(Graphics.host, Strings.addHost, AioInfrastructureEditOperation.AddHost)
private val addNetwork = MenuItem<AioInfrastructureEditOperation>(Graphics.account_tree, Strings.addNetwork, AioInfrastructureEditOperation.AddNetwork)
private val addDevice = MenuItem<AioInfrastructureEditOperation>(Graphics.memory, Strings.addDevice, AioInfrastructureEditOperation.AddDevice)


private val addTopMenu = listOf(addHost, addNetwork)
private val hostMenu = listOf(addNetwork, addDevice)
private val networkMenu = listOf(addDevice)
private val emptyMenu = listOf<MenuItem<AioInfrastructureEditOperation>>()

private fun menu(viewModel: InfrastructureTreeModel, treeItem: TreeItem<AioInfrastructureItem>): List<MenuItemBase<AioInfrastructureEditOperation>> {

    val base =
        when (treeItem.data.itemType) {
            AioInfrastructureItemType.Host -> hostMenu
            AioInfrastructureItemType.Network -> networkMenu
            AioInfrastructureItemType.Device -> emptyMenu
            AioInfrastructureItemType.Point -> emptyMenu
        }

    val out = mutableListOf<MenuItemBase<AioInfrastructureEditOperation>>()
    out.addAll(base)

    out += MenuSeparator<AioInfrastructureEditOperation>()

    out += MenuItem<AioInfrastructureEditOperation>(null, Strings.enable, AioInfrastructureEditOperation.Enable)
    out += MenuItem<AioInfrastructureEditOperation>(null, Strings.disable, AioInfrastructureEditOperation.Disable)

    return out
}

private fun isFirst(viewModel: InfrastructureTreeModel, treeItem: TreeItem<AioSpace>): Boolean {
    val safeParent = treeItem.parent
    if (safeParent == null) {
        return viewModel.items.first() == treeItem
    } else {
        return safeParent.children.first() == treeItem
    }
}

private fun isLast(viewModel: InfrastructureTreeModel, treeItem: TreeItem<AioSpace>): Boolean {
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
    viewModel: InfrastructureTreeModel,
    treeItem: TreeItem<AioInfrastructureItem>
): AdaptiveFragment {
    column {
        zIndex { 200 }
        contextMenu(menu(viewModel, treeItem)) { menuItem, _ -> apply(viewModel, menuItem, treeItem); hide() }
    }
    return fragment()
}