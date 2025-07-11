package `fun`.adaptive.grove.doc.ui

import `fun`.adaptive.foundation.value.observableOf
import `fun`.adaptive.grove.doc.model.GroveDocSpec
import `fun`.adaptive.grove.doc.model.groveDocDomain
import `fun`.adaptive.markdown.transform.MarkdownToTreeVisitor
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
import `fun`.adaptive.ui.snackbar.warningNotification
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewBackend
import `fun`.adaptive.value.remote.AvRemoteValueSubscriber

class DocToolViewBackend(
    override val workspace: MultiPaneWorkspace,
    override val paneDef: PaneDef
) : PaneViewBackend<DocToolViewBackend>() {

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
        openDocument(item.data, modifiers)
        TreeViewBackend.defaultSelectedFun(backend !!, item, modifiers)
    }

    fun openDocument(
        data: String,
        modifiers: Set<EventModifier>
    ) {
        val referenceTool = workspace.toolBackend(ReferenceToolViewBackend::class) ?: return

        val value = referenceTool.findByUrl(data)

        if (value == null) {
            warningNotification("Cannot find documentation: $data")
            return
        }

        val path = referenceTool.docPathNames(value)

        workspace.addContent(groveDocDomain.node, GroveDocContentItem(path), modifiers)
    }

}