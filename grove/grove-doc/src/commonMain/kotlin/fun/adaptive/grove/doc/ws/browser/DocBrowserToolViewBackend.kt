package `fun`.adaptive.grove.doc.ws.browser

import `fun`.adaptive.grove.doc.model.avDomain
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.value.AvUiTreeViewBackend
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace
import `fun`.adaptive.value.AvValue

class DocBrowserToolViewBackend(
    workspace: MultiPaneWorkspace,
    val config: DocBrowserConfig
) : AbstractDocToolViewBackend<DocBrowserToolViewBackend>(workspace) {

    override fun selectedFun(backend: AvUiTreeViewBackend<String>, item: TreeItem<AvValue<String>>, modifiers: Set<EventModifier>) {
        val value = item.data

        val browserItem = DocBrowserWsItem(
            value.nameLike,
            config.itemType,
            config,
            value
        )

        workspace.addContent(avDomain.node, browserItem, modifiers)
    }

}