package `fun`.adaptive.doc.example.tree

import `fun`.adaptive.doc.support.ExampleValueSpec
import `fun`.adaptive.doc.support.exampleDomain
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.runtime.FrontendWorkspace
import `fun`.adaptive.ui.snackbar.infoNotification
import `fun`.adaptive.ui.tree.tree
import `fun`.adaptive.ui.value.AvUiTreeSupport

/**
 * # Value tree
 *
 * [AvUiTreeSupport](class://) can build the tree items for [tree](fragment://) automatically.
 *
 * For this to work, you have to use the [lib-value](def://) module of [Adaptive](def://),
 * and have a [value tree](def://) in a [value store](def://).
 *
 * Building a [value tree](def://) is actually quite easy, see [value trees](guide://)
 * for more information.
 *
 * This tree is built from values, using the tree functionality of [value store](def://).
 *
 * - The tree definition defines the markers and reference labels the tree uses.
 * - use [AvUiTreeViewBackend](class://) to initialize the backend of the tree
 *
 * > [!NOTE]
 * > [AvUiTreeSupport](class://) subscribes to value changes, and thus it has to be disposed of.
 * > When used in a fragment like in this example, Adaptive automatically disposes of it. However,
 * > if you use it other ways, you might dispose of it manually.
 *
 * **NOTE** The tree definition is in [ExampleDomain](class://), the values are added by [SiteWorker](class://).
 */
@Adaptive
fun treeValueExample(): AdaptiveFragment {

    val viewBackend = AvUiTreeSupport(
        fragment().firstContext<FrontendWorkspace>().backend,
        ExampleValueSpec::class,
        exampleDomain.treeDef,
        { _, item, _ -> infoNotification("Selected: ${item.data.name}") }
    )

    tree(viewBackend.treeBackend)

    return fragment()
}