package `fun`.adaptive.iot.ui.space

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.model.space.AioSpace
import `fun`.adaptive.iot.model.space.SpaceBrowserConfig
import `fun`.adaptive.iot.model.space.SpaceBrowserState
import `fun`.adaptive.iot.model.space.SpaceBrowserWsItem
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.builtin.arrows_input
import `fun`.adaptive.ui.builtin.collapseAll
import `fun`.adaptive.ui.builtin.expandAll
import `fun`.adaptive.ui.builtin.zoom_out_map
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeItemSelectedFun
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.tree.tree
import `fun`.adaptive.ui.workspace.Workspace.Companion.wsContext
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPaneAction
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.ui.workspace.wsToolPane
import `fun`.adaptive.utility.UUID

@Adaptive
fun wsMeasurementToolPane(pane: WsPane<SpaceBrowserState>): AdaptiveFragment {

    val context = fragment().wsContext<AioWsContext>()

    val observed = valueFrom {
        // TODO refresh when the original tree changes
        pane.model.tree ?: transformTreeModel(context, pane, ::browserToolSelectedFun).also { pane.model.tree = it }
    }

    wsToolPane(pane) {
        tree(observed)
    }

    return fragment()
}

typealias SpaceBrowserModel = TreeViewModel<SpaceBrowserWsItem, AioWsContext>
private typealias SpaceBrowserTreeItem = TreeItem<SpaceBrowserWsItem>

fun wsSpaceBrowserTool(config: SpaceBrowserConfig) =
    WsPane(
        UUID(),
        config.name,
        config.icon,
        WsPanePosition.LeftTop,
        AioWsContext.WSPANE_MEASUREMENT_LOCATION_TOOL,
        model = SpaceBrowserState(config),
        actions = listOf(
            WsPaneAction(Graphics.zoom_out_map, Strings.expandAll, Unit) { w, p, d -> spaceBrowserExpand(p) },
            WsPaneAction(Graphics.arrows_input, Strings.collapseAll, Unit) { w, p, d -> spaceBrowserCollapse(p) },
        )
    )

private fun browserToolSelectedFun(viewModel: SpaceBrowserModel, item: SpaceBrowserTreeItem, modifiers: Set<EventModifier>) {
    val workspace = viewModel.context.workspace
    workspace.addContent(item.data, modifiers)
    TreeViewModel.defaultSelectedFun(viewModel, item, modifiers)
}

private fun spaceBrowserExpand(pane: WsPane<*>) {
    val state = pane.model as SpaceBrowserState
    state.tree?.items?.forEach { it.expandAll() }
}

private fun spaceBrowserCollapse(pane: WsPane<*>) {
    val state = pane.model as SpaceBrowserState
    state.tree?.items?.forEach { it.collapseAll() }
}

private fun transformTreeModel(
    context: AioWsContext,
    pane: WsPane<SpaceBrowserState>,
    selectedFun: TreeItemSelectedFun<SpaceBrowserWsItem, AioWsContext>
): SpaceBrowserModel {

    fun transformItem(original: TreeItem<AioSpace>, newParent: SpaceBrowserTreeItem?): SpaceBrowserTreeItem {
        TreeItem(
            original.icon,
            original.title,
            SpaceBrowserWsItem(original.data.name, config = pane.model.config, spaceId = original.data.uuid),
            parent = newParent
        ).also { new ->
            new.children = original.children.map { transformItem(it, new) }
            return new
        }
    }

    return context.spaceTree.transform(
        context,
        selectedFun = selectedFun,
        transform = ::transformItem
    )
}
