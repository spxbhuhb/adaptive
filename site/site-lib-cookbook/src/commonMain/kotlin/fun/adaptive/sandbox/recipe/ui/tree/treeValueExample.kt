package `fun`.adaptive.sandbox.recipe.ui.tree

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.LifecycleBound
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.tree.TreeViewBackend
import `fun`.adaptive.ui.tree.tree
import `fun`.adaptive.ui.value.AvUiTree
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.model.AvTreeSetup
import `fun`.adaptive.value.store.AvComputeContext
import `fun`.adaptive.value.testing.EmbeddedValueServer
import `fun`.adaptive.value.testing.EmbeddedValueServer.Companion.embeddedValueServer

private val treeSetup = AvTreeSetup("node", "childList", "parentRef", "childListRef", "rootList")

@Adaptive
fun treeValueExample(): AdaptiveFragment {

    val values = embeddedValueServer { buildExampleTree() }
    val viewBackend = TreeValueExampleViewBackend(values)

    column {
        text("AvUiTree - read from value store")
        column {
            borders.outline .. width { 200.dp } .. height { 200.dp} .. verticalScroll .. horizontalScroll
            tree(viewBackend.treeBackend)
        }
    }

    return fragment()
}

class TreeValueExampleViewBackend(
    values : EmbeddedValueServer
) : LifecycleBound {

    val avTree = AvUiTree(values.clientBackend, String::class, treeSetup)

    val treeBackend = TreeViewBackend<AvValue<String>, Unit>(emptyList(), context = Unit)

    init {
        avTree.addListener { treeBackend.items = it }
    }

    override fun dispose(fragment: AdaptiveFragment, index : Int) {
        avTree.stop()
    }

}

private fun AvComputeContext.buildExampleTree() {
    for (rootIndex in 1..3) {
        val rootNode = AvValue(name = "root $rootIndex", markersOrNull = setOf("node"), spec = "root $rootIndex")
        addValue(rootNode)

        for (childIndex in 1..3) {
            val childNode = AvValue(name = "child $rootIndex.$childIndex", markersOrNull = setOf("node"), spec = "child $rootIndex.$childIndex")
            addValue(childNode)
            addTreeNode(rootNode.uuid, childNode.uuid, treeSetup)
        }
    }
}