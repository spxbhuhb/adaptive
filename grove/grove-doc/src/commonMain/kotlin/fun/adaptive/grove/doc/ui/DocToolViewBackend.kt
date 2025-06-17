package `fun`.adaptive.grove.doc.ui

import `fun`.adaptive.grove.doc.model.GroveDocValue
import `fun`.adaptive.grove.doc.model.avDomain
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.generated.resources.collapseAll
import `fun`.adaptive.ui.generated.resources.expandAll
import `fun`.adaptive.ui.generated.resources.unfold_less
import `fun`.adaptive.ui.generated.resources.unfold_more
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.value.AvUiTreeViewBackend
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.backends.PaneViewBackend
import `fun`.adaptive.ui.mpw.model.AbstractPaneAction
import `fun`.adaptive.ui.mpw.model.PaneAction
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.value.AvValue

class DocToolViewBackend(
    override val workspace: MultiPaneWorkspace,
    override val paneDef: PaneDef
) : PaneViewBackend<DocToolViewBackend>() {

    val tree = AvUiTreeViewBackend(workspace.backend, String::class, avDomain.treeDef, ::selectedFun, ::sortChildrenFun)

    override fun getPaneActions(): List<AbstractPaneAction> {
        return listOf(
            PaneAction(Graphics.unfold_more, Strings.expandAll) { tree.treeBackend.expandAll() },
            PaneAction(Graphics.unfold_less, Strings.collapseAll) { tree.treeBackend.collapseAll() }
        )
    }

    fun selectedFun(
        @Suppress("unused")
        backend: AvUiTreeViewBackend<String>,
        item: TreeItem<AvValue<String>>,
        modifiers: Set<EventModifier>
    ) {
        workspace.addContent(avDomain.node, item.data, modifiers)
    }

    fun sortChildrenFun(
        children: List<TreeItem<AvValue<String>>>
    ): List<TreeItem<AvValue<String>>> {
        return children.sortedBy { it.data.name?.lowercase() }
    }

    fun docPathNames(item : GroveDocValue): List<String> {
        return tree.treeSubscriber.pathNames(item)
    }
}