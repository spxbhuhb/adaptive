package `fun`.adaptive.doc.example.value

import `fun`.adaptive.doc.support.ExampleValueSpec
import `fun`.adaptive.doc.support.exampleDomain
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.LifecycleBound
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.runtime.FrontendWorkspace
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.scroll
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.snackbar.infoNotification
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.tree
import `fun`.adaptive.ui.value.AvUiTreeSupport
import `fun`.adaptive.value.AvValue

/**
 * # Value tree in the view backend
 *
 * Use [AvUiTreeSupport](class://) to subscribe for [value trees](guide://) and transform them into
 * a tree of [TreeItem](class://) instances.
 *
 * The whole process is automatic, you can simply use the [tree](fragment://) fragment to display
 * the tree.
 *
 * See [Value trees](guide://) for more information.
 *
 * > [!IMPORTANT]
 * > [AvUiTreeSupport](class://) must be disposed when it is no longer needed as the subscription
 * > has to be removed from the server side. This example implements [LifecycleBound](interface://)
 * > and disposes the tree when the fragment is disposed.
 *
 * **NOTE** The tree definition is in [ExampleDomain](class://), the values are added by [SiteWorker](class://).
 */
@Adaptive
fun valueViewBackendTreeExample() : AdaptiveFragment {

    val viewBackend = ExampleViewBackend(fragment().firstContext())

    column {
        width { 400.dp } .. height { 200.dp } .. scroll .. borders.outline
        tree(viewBackend.exampleTree.treeBackend)
    }

    return fragment()
}

class ExampleViewBackend(
    workspace : FrontendWorkspace
) : LifecycleBound {

    val exampleTree = AvUiTreeSupport(
        workspace.backend,
        ExampleValueSpec::class,
        exampleDomain.treeDef,
        ::selectedFun
    )

    fun selectedFun(
        backend: AvUiTreeSupport<ExampleValueSpec>,
        item : TreeItem<AvValue<ExampleValueSpec>>,
        modifiers : Set<EventModifier>
    ) {
        infoNotification("Selected: ${item.data.name}")
    }

    override fun dispose(fragment: AdaptiveFragment, index: Int) {
        exampleTree.dispose()
    }

}


