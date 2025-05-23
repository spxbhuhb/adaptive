package `fun`.adaptive.sandbox.recipe.ui.tree

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.tree.tree
import `fun`.adaptive.ui.value.AvUiTreeViewBackend
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.model.AvTreeSetup
import `fun`.adaptive.value.store.AvComputeContext
import `fun`.adaptive.value.embedded.EmbeddedValueServer.Companion.embeddedValueServer

private val treeSetup = AvTreeSetup("node", "childList", "parentRef", "childListRef", "rootList")

@Adaptive
fun treeValueExample(): AdaptiveFragment {

    val values = embeddedValueServer { buildExampleTree() }

    val viewBackend = AvUiTreeViewBackend(
        values.clientBackend,
        String::class,
        treeSetup
    ) {
        _, item, _ -> println("selected: $item")
    }

    column {
        text("AvUiTree - read from value store")
        column {
            borders.outline .. width { 200.dp } .. height { 200.dp } .. verticalScroll .. horizontalScroll
            tree(viewBackend.treeBackend) .. sizeStrategy.container .. backgrounds.friendlyOpaque
        }
    }

    return fragment()
}

private fun AvComputeContext.buildExampleTree() {
    for (rootIndex in 1 .. 3) {
        val rootNode = AvValue(name = "root $rootIndex", markersOrNull = setOf("node"), spec = "root $rootIndex")
        addValue(rootNode)

        for (childIndex in 1 .. 3) {
            val childNode = AvValue(name = "child $rootIndex.$childIndex", markersOrNull = setOf("node"), spec = "child $rootIndex.$childIndex")
            addValue(childNode)
            linkTreeNode(rootNode.uuid, childNode.uuid, treeSetup)
        }
    }
}