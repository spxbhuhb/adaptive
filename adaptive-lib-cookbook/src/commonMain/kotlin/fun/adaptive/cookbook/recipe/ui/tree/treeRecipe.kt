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
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.tree.TreeViewModel.Companion.defaultSelectedFun
import `fun`.adaptive.ui.tree.tree
import kotlin.random.Random

@Adaptive
fun treeRecipe(): AdaptiveFragment {

    grid {
        maxSize .. colTemplate(1.fr, 1.fr) .. gap { 16.dp }

        column {
            text("static - double click to expand")
            column {
                borders.outline
                tree(TreeViewModel(staticTree))
            }
        }

        grid {
            rowTemplate(24.dp, 1.fr)
            text("random - single click to expand")
            column {
                borders.outline .. maxHeight .. verticalScroll
                tree(TreeViewModel(generate(), selectedFun = ::defaultSelectedFun, openWithSingleClick = true))
            }
        }
    }

    return fragment()
}

val staticTree = listOf(
    TreeItem<Unit>(
        icon = Graphics.folder,
        title = "Item 1",
        data = Unit,
        parent = null
    ).also { item ->
        item.children = listOf(
            TreeItem(
                icon = Graphics.folder,
                title = "Item 1.1",
                data = Unit,
                parent = item
            )
        )
    },
    TreeItem(
        icon = Graphics.folder,
        title = "Item 2",
        data = Unit,
        parent = null
    ).also { item ->
        item.children = listOf(
            TreeItem(
                icon = Graphics.folder,
                title = "Item 2.1",
                data = Unit,
                parent = item
            )
        )
    }
)


private fun generate(): List<TreeItem<Unit>> {
    val numRoots = Random.nextInt(1, 4)
    return List(numRoots) { generateRandomTree(it + 1, 3, null) } // Adjust depth as needed
}

private fun generateRandomTree(index: Int, depth: Int, parent: TreeItem<Unit>?): TreeItem<Unit> {
    val nodeTitle = "Item ${index.toString().toCharArray().joinToString(".")}"
    val numChildren = Random.nextInt(1, 4)

    val item = TreeItem(
        icon = Graphics.folder,
        title = nodeTitle,
        data = Unit,
        parent = parent
    )

    item.children = when (depth) {
        0 -> emptyList()
        else -> List(numChildren) { generateRandomTree(index * 10 + it + 1, depth - 1, item) }
    }

    return item
}
