package `fun`.adaptive.grove.doc.ui

import `fun`.adaptive.foundation.value.observableOf
import `fun`.adaptive.grove.doc.model.GroveDocSpec
import `fun`.adaptive.grove.doc.model.GroveDocValue
import `fun`.adaptive.grove.doc.model.groveDocDomain
import `fun`.adaptive.markdown.transform.MarkdownToTreeVisitor
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
import `fun`.adaptive.ui.mpw.model.*
import `fun`.adaptive.ui.navigation.NavState
import `fun`.adaptive.ui.snackbar.warningNotification
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewBackend
import `fun`.adaptive.ui.value.AvUiTreeViewBackend
import `fun`.adaptive.utility.decodeFromUrl
import `fun`.adaptive.utility.encodeToUrl
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.remote.AvRemoteValueSubscriber

class DocToolViewBackend(
    override val workspace: MultiPaneWorkspace,
    override val paneDef: PaneDef
) : PaneViewBackend<DocToolViewBackend>(), MultiPaneUrlResolver {

    val doc = AvRemoteValueSubscriber(workspace.backend, GroveDocSpec::class, groveDocDomain.groveDocToc)
    var treeBackend = observableOf<TreeViewBackend<String, DocToolViewBackend>?> { null }

    init {
        doc.addListener { docValue ->
            if (docValue == null) return@addListener
            treeBackend.value = TreeViewBackend(
                MarkdownToTreeVisitor(docValue.spec.content).transform(),
                context = this,
                selectedFun = ::selectedFun,
                handleAtEnd = true
            )
        }
    }

    override fun getPaneActions(): List<AbstractPaneAction> {
        return listOf(
            PaneAction(Graphics.unfold_more, Strings.expandAll) { treeBackend.value?.expandAll() },
            PaneAction(Graphics.unfold_less, Strings.collapseAll) { treeBackend.value?.collapseAll() }
        )
    }

    fun selectedFun(
        @Suppress("unused")
        backend: TreeViewBackend<String, DocToolViewBackend>?,
        item: TreeItem<String>,
        modifiers: Set<EventModifier>
    ) {
        val referenceTool = workspace.toolBackend(ReferenceToolViewBackend::class) ?: return

        val name = item.data.removeSuffix(".md").decodeFromUrl()

        val value = when {
            name.startsWith(groveDocDomain.guide) -> referenceTool.findGuideByName(name.removePrefix(groveDocDomain.guide + "-"))
            name.startsWith(groveDocDomain.definition) -> referenceTool.findDefinitionByName(name.removePrefix(groveDocDomain.definition + "-"))
            else -> referenceTool.findGuideByName(name) ?: referenceTool.findDefinitionByName(name)
        }

        if (value == null) {
            warningNotification("Cannot find guide: ${item.data}")
            return
        }

        val path = referenceTool.docPathNames(value)

        workspace.addContent(groveDocDomain.node, GroveDocContentItem(path), modifiers)
        TreeViewBackend.defaultSelectedFun(backend !!, item, modifiers)
    }

    override fun resolve(navState: NavState): Pair<PaneContentType, PaneContentItem>? {
        if (navState.url.segmentsStartsWith("/documentation")) return null // FIXME hard coded URL segment
        return groveDocDomain.node to GroveDocContentItem(navState.url.segments.drop(2))
    }

    override fun toNavState(type: PaneContentType, item: PaneContentItem): NavState? {
        if (type != groveDocDomain.node || item !is GroveDocContentItem) return null
        return NavState.parse("/documentation/${item.path.joinToString("/") { it.encodeToUrl() }}") // FIXME hard coded URL segment
    }

}