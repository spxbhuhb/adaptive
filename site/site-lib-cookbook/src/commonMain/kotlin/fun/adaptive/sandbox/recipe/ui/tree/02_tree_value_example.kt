package `fun`.adaptive.sandbox.recipe.ui.tree

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.tree.tree
import `fun`.adaptive.ui.value.AvUiTreeViewBackend
import `fun`.adaptive.ui.value.iconCache
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.model.AvTreeDef
import `fun`.adaptive.value.store.AvComputeContext
import `fun`.adaptive.value.embedded.EmbeddedValueServer.Companion.embeddedValueServer

/**
 * # Value tree
 *
 * [AvUiTreeViewBackend](class://) can build the tree items for [TreeViewBackend](class://) automatically.
 *
 * For this to work, you have to use the [lib-value](def://) module of [Adaptive](def://),
 * and have a [value tree](def://) in a [value store](def://).
 *
 * Building a [value tree](def://) is actually quite easy, see [building value trees](guide://)
 * for more information.
 *
 * This tree is built from values, using the tree functionality of [value store](def://).
 *
 * - The tree definition defines the markers and reference labels the tree uses.
 * - use [AvUiTreeViewBackend](class://) to initialize the backend of the tree
 */
@Adaptive
fun treeValueExample(): AdaptiveFragment {

    val viewBackend = AvUiTreeViewBackend(
        values.clientBackend,
        String::class,
        treeDef,
        { _, item, _ -> println("selected: $item") }
    )

    tree(viewBackend.treeBackend)

    return fragment()
}

private val treeDef = AvTreeDef("node", "childList", "parentRef", "childListRef", "rootList")

val values = embeddedValueServer { buildExampleTree() }

private fun AvComputeContext.buildExampleTree() {
    for (rootIndex in 1 .. 3) {
        val rootNode = AvValue(name = "root $rootIndex", markersOrNull = setOf("node"), spec = "root $rootIndex")
        addValue(rootNode)

        for (childIndex in 1 .. 3) {
            val childNode = AvValue(name = "child $rootIndex.$childIndex", markersOrNull = setOf("node"), spec = "child $rootIndex.$childIndex")
            addValue(childNode)
            linkTreeNode(treeDef, rootNode.uuid, childNode.uuid)
        }
    }
}