package `fun`.adaptive.ui.mpw.example.tree

import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.generated.resources.collapseAll
import `fun`.adaptive.ui.generated.resources.expandAll
import `fun`.adaptive.ui.generated.resources.unfold_less
import `fun`.adaptive.ui.generated.resources.unfold_more
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.backends.PaneViewBackend
import `fun`.adaptive.ui.mpw.model.AbstractPaneAction
import `fun`.adaptive.ui.mpw.model.PaneAction
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.value.AvUiTreeViewBackend
import `fun`.adaptive.value.AvValue

class ExampleTreeToolViewBackend(
    override val workspace: MultiPaneWorkspace,
    override val paneDef: PaneDef
) : PaneViewBackend<ExampleTreeToolViewBackend>() {

    val tree = AvUiTreeViewBackend(workspace.backend, ExampleTreeValueSpec::class, avDomain.treeDef, ::selectedFun)

    override fun getPaneActions(): List<AbstractPaneAction> {
        return listOf(
            PaneAction(Graphics.unfold_more, Strings.expandAll) { tree.treeBackend.expandAll() },
            PaneAction(Graphics.unfold_less, Strings.collapseAll) { tree.treeBackend.collapseAll() }
        )
    }

    fun selectedFun(
        @Suppress("unused")
        backend: AvUiTreeViewBackend<ExampleTreeValueSpec>,
        item: TreeItem<AvValue<ExampleTreeValueSpec>>,
        modifiers: Set<EventModifier>
    ) {
        workspace.addContent(avDomain.node, item.data, modifiers)
    }

    fun docPathNames(item : ExampleValue): List<String> {
        return tree.treeSubscriber.pathNames(item)
    }

}