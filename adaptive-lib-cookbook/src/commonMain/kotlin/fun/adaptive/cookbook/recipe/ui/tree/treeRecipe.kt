package `fun`.adaptive.cookbook.recipe.ui.tree

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.builtin.folder
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.tree
import kotlin.random.Random

@Adaptive
fun treeRecipe(): AdaptiveFragment {
    grid {
        maxSize .. colTemplate(1.fr, 1.fr) .. gap { 16.dp }

        column {
            text("static")
            column {
                borders.outline
                tree(staticTree)
            }
        }

        grid {
            rowTemplate(24.dp, 1.fr)
            text("random")
            column {
                borders.outline .. maxHeight .. verticalScroll
                tree(generate())
            }
        }
    }

    return fragment()
}

val staticTree = listOf(
    TreeItem<Unit>(
        icon = Graphics.folder,
        title = "Item 1",
        children = listOf(
            TreeItem(
                icon = Graphics.folder,
                title = "Item 1.1",
                children = listOf(),
                data = Unit
            )
        ),
        data = Unit
    ),
    TreeItem(
        icon = Graphics.folder,
        title = "Item 2",
        children = listOf(
            TreeItem(
                icon = Graphics.folder,
                title = "Item 2.1",
                children = listOf(),
                data = Unit
            )
        ),
        data = Unit
    )
)


private fun generate(): List<TreeItem<Unit>> {
    val numRoots = Random.nextInt(1, 4)
    return List(numRoots) { generateRandomTree(it + 1, 3) } // Adjust depth as needed
}

private fun generateRandomTree(index: Int, depth: Int): TreeItem<Unit>{
    val nodeTitle = "Item ${index.toString().toCharArray().joinToString(".")}"
    val numChildren = Random.nextInt(1, 4)

    val children = when (depth) {
        0 -> emptyList()
        else -> List(numChildren) { generateRandomTree(index * 10 + it + 1, depth - 1) }
    }

    return TreeItem(
        icon = Graphics.folder,
        title = nodeTitle,
        children = children,
        data = Unit
    )
}
