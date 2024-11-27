package `fun`.adaptive.cookbook.ui.tree

import `fun`.adaptive.cookbook.Res
import `fun`.adaptive.cookbook.folder
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.tree
import kotlin.random.Random

@Adaptive
fun treeRecipe() {
    column {
        borders.outline
        tree(generate())
    }
}

private fun generateRandomTree(index: Int, depth: Int): TreeItem {
    val nodeTitle = "Item ${index.toString().toCharArray().joinToString(".")}"
    val numChildren = Random.nextInt(0, 4)

    val children = when (depth) {
        0 -> emptyList()
        else -> List(numChildren) { generateRandomTree(index * 10 + it, depth - 1) }
    }

    return TreeItem(
        icon = Res.drawable.folder,
        title = nodeTitle,
        children = children
    )
}

private fun generate(): List<TreeItem> {
    val numRoots = Random.nextInt(1, 4)
    return List(numRoots) { generateRandomTree(it, 3) } // Adjust depth as needed
}