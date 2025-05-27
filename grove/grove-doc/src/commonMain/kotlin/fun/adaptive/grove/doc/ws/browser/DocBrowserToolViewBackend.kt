package `fun`.adaptive.grove.doc.ws.browser

import `fun`.adaptive.grove.doc.model.avDomain
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.value.AvUiTreeViewBackend
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.value.AvValue

class DocBrowserToolViewBackend(
    workspace: MultiPaneWorkspace
) : AbstractDocToolViewBackend<DocBrowserToolViewBackend>(workspace) {

    override fun selectedFun(backend: AvUiTreeViewBackend<String>, item: TreeItem<AvValue<String>>, modifiers: Set<EventModifier>) {
        workspace.addContent(avDomain.node, item.data, modifiers)
    }

}