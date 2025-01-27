package `fun`.adaptive.cookbook.ui.tree

import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.cookbook.folder
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.tree
import kotlin.random.Random

@Adaptive
fun treeRecipe() {
    row {
        gap { 16.dp }
        column {
            text("static")
            column {
                borders.outline
                tree(staticTree)
            }
        }
        column {
            text("random")
            column {
                borders.outline
                tree(generate())
            }
        }
    }
}

val staticTree = listOf(
    TreeItem(
        icon = Graphics.folder,
        title = "Item 1",
        children = listOf(
            TreeItem(
                icon = Graphics.folder,
                title = "Item 1.1",
                children = listOf()
            )
        )
    ),
    TreeItem(
        icon = Graphics.folder,
        title = "Item 2",
        children = listOf(
            TreeItem(
                icon = Graphics.folder,
                title = "Item 2.1",
                children = listOf()
            )
        )
    )
)


private fun generate(): List<TreeItem> {
    val numRoots = Random.nextInt(1, 4)
    return List(numRoots) { generateRandomTree(it + 1, 3) } // Adjust depth as needed
}

private fun generateRandomTree(index: Int, depth: Int): TreeItem {
    val nodeTitle = "Item ${index.toString().toCharArray().joinToString(".")}"
    val numChildren = Random.nextInt(1, 4)

    val children = when (depth) {
        0 -> emptyList()
        else -> List(numChildren) { generateRandomTree(index * 10 + it + 1, depth - 1) }
    }

    return TreeItem(
        icon = Graphics.folder,
        title = nodeTitle,
        children = children
    )
}
