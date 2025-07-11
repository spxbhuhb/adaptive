package `fun`.adaptive.grove.doc.ui

import `fun`.adaptive.grove.doc.model.GroveDocSpec
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
import `fun`.adaptive.ui.snackbar.warningNotification
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.value.AvUiTreeViewBackend
import `fun`.adaptive.utility.Url
import `fun`.adaptive.utility.decodeFromUrl
import `fun`.adaptive.utility.encodeToUrl
import `fun`.adaptive.value.AvValue

class ReferenceToolViewBackend(
    override val workspace: MultiPaneWorkspace,
    override val paneDef: PaneDef
) : PaneViewBackend<ReferenceToolViewBackend>(), MultiPaneUrlResolver {

    val tree = AvUiTreeViewBackend(workspace.backend, GroveDocSpec::class, groveDocDomain.treeDef, ::selectedFun, ::sortChildrenFun)

    override fun getPaneActions(): List<AbstractPaneAction> {
        return listOf(
            PaneAction(Graphics.unfold_more, Strings.expandAll) { tree.treeBackend.expandAll() },
            PaneAction(Graphics.unfold_less, Strings.collapseAll) { tree.treeBackend.collapseAll() }
        )
    }

    fun selectedFun(
        @Suppress("unused")
        backend: AvUiTreeViewBackend<GroveDocSpec>,
        item: TreeItem<GroveDocValue>,
        modifiers: Set<EventModifier>
    ) {
        workspace.addContent(groveDocDomain.node, GroveDocContentItem(docPathNames(item.data)), modifiers)
    }

    fun sortChildrenFun(
        children: List<TreeItem<GroveDocValue>>
    ): List<TreeItem<GroveDocValue>> {
        return children.sortedBy { it.data.name?.lowercase() }
    }

    fun docPathNames(item: GroveDocValue): List<String> {
        return tree.treeSubscriber.pathNames(item)
    }

    fun findByUrl(url: String): AvValue<GroveDocSpec>? {
        val name = url.decodeFromUrl()

        val value = when {
            name.startsWith(groveDocDomain.guide) -> findGuideByName(name.removePrefix(groveDocDomain.guide + "://"))
            name.startsWith(groveDocDomain.definition) -> findDefinitionByName(name.removePrefix(groveDocDomain.definition + "://"))
            else -> findGuideByName(name) ?: findDefinitionByName(name)
        }

        return value
    }

    fun findGuideByName(name: String): AvValue<GroveDocSpec>? =
        tree.treeSubscriber.find { it.name == name && groveDocDomain.guide in it.markers }

    fun findDefinitionByName(name: String): AvValue<GroveDocSpec>? =
        tree.treeSubscriber.find { it.name == name && groveDocDomain.definition in it.markers }

    fun filterByNamePart(name: String) =
        tree.treeSubscriber.filter { name in it.nameLike.lowercase() && groveDocDomain.guide in it.markers }


    override fun resolve(url: Url): Pair<PaneContentType, PaneContentItem>? {
        if (! url.segmentsStartsWith("/documentation")) return null // FIXME hard coded URL segment
        val path = url.segments.drop(2) // drop the empty segment and documentation
        // path should be at least <subproject>/<type>/<name>
        if (path.size < 3) return null
        return groveDocDomain.node to GroveDocContentItem(path)
    }

    override fun toNavState(type: PaneContentType, item: PaneContentItem): NavState? {
        if (type != groveDocDomain.node || item !is GroveDocContentItem) return null
        return NavState.parse("/documentation/${item.path.joinToString("/") { it.encodeToUrl() }}") // FIXME hard coded URL segment
    }

}