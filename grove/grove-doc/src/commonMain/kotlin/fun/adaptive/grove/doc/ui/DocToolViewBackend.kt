package `fun`.adaptive.grove.doc.ui

import `fun`.adaptive.grove.doc.model.GroveDocValue
import `fun`.adaptive.grove.doc.model.groveDocDomain
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.generated.resources.collapseAll
import `fun`.adaptive.ui.generated.resources.expandAll
import `fun`.adaptive.ui.generated.resources.unfold_less
import `fun`.adaptive.ui.generated.resources.unfold_more
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.mpw.MultiPaneUrlResolver
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.backends.PaneViewBackend
import `fun`.adaptive.ui.mpw.model.AbstractPaneAction
import `fun`.adaptive.ui.mpw.model.PaneAction
import `fun`.adaptive.ui.mpw.model.PaneContentItem
import `fun`.adaptive.ui.mpw.model.PaneContentType
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.ui.navigation.NavState
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.value.AvUiTreeViewBackend
import `fun`.adaptive.utility.encodeToUrl
import `fun`.adaptive.value.AvValue

class DocToolViewBackend(
    override val workspace: MultiPaneWorkspace,
    override val paneDef: PaneDef
) : PaneViewBackend<DocToolViewBackend>(), MultiPaneUrlResolver {

    val tree = AvUiTreeViewBackend(workspace.backend, String::class, groveDocDomain.treeDef, ::selectedFun, ::sortChildrenFun)

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
        workspace.addContent(groveDocDomain.node, GroveDocContentItem(docPathNames(item.data)), modifiers)
    }

    fun sortChildrenFun(
        children: List<TreeItem<AvValue<String>>>
    ): List<TreeItem<AvValue<String>>> {
        return children.sortedBy { it.data.name?.lowercase() }
    }

    fun docPathNames(item: GroveDocValue): List<String> {
        return tree.treeSubscriber.pathNames(item)
    }

    override fun resolve(navState: NavState): Pair<PaneContentType, PaneContentItem>? {
        if (navState.segments[0] != "documentation") return null // FIXME hard coded URL segment
        return groveDocDomain.node to GroveDocContentItem(navState.segments.drop(1))
    }

    override fun toNavState(type : PaneContentType, item: PaneContentItem): NavState? {
        if (type != groveDocDomain.node || item !is GroveDocContentItem) return null
        return NavState.parse("/documentation/${item.path.joinToString("/") { it.encodeToUrl() }}") // FIXME hard coded URL segment
    }

}