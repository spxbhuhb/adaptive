package `fun`.adaptive.document.ws.browser

import `fun`.adaptive.document.ws.DocTreeModel
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewBackend
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace
import `fun`.adaptive.value.AvValueId

class DocBrowserToolController(
    workspace: MultiPaneWorkspace,
    val config: DocBrowserConfig
) : AbstractDocToolController(workspace) {

    override fun selectedFun(viewModel: DocTreeModel, treeItem: TreeItem<AvValueId>, modifiers: Set<EventModifier>) {
        val aioItem = valueTreeStore[treeItem.data] ?: return

        val browserItem = DocBrowserWsItem(
            aioItem.name ?: "",
            config.itemType,
            config,
            aioItem
        )

        workspace.addContent(browserItem, modifiers)

        TreeViewBackend.Companion.defaultSelectedFun(viewModel, treeItem, modifiers)
    }

}