import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.builtin.collapse_all
import `fun`.adaptive.ui.builtin.expand_all
import `fun`.adaptive.ui.builtin.folder
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.denseIconTheme
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.tree
import kotlin.random.Random

@Adaptive
fun treeMain(): AdaptiveFragment {
    grid {
        maxSize .. colTemplate(1.fr, 1.fr) .. gap { 16.dp }

        column {
            text("static")

            row {
                gap { 16.dp }
                actionIcon(Graphics.expand_all, theme = denseIconTheme) .. onClick { staticTree.forEach { it.expandAll() } }
                actionIcon(Graphics.collapse_all, theme = denseIconTheme) .. onClick { staticTree.forEach { it.collapseAll() } }
            }

            column {
                borders.outline
                tree(staticTree)
            }
        }

        grid {
            rowTemplate(24.dp, 24.dp, 1.fr)
            text("random $generatedTreeSize")
            row {
                gap { 16.dp }
                actionIcon(Graphics.expand_all, theme = denseIconTheme) .. onClick { generatedTree.forEach { it.expandAll() } }
                actionIcon(Graphics.collapse_all, theme = denseIconTheme) .. onClick { generatedTree.forEach { it.collapseAll() } }
            }
            column {
                borders.outline .. maxHeight .. verticalScroll
                tree(generatedTree)
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

var generatedTreeSize = 0

val generatedTree = generate()

private fun generate(): List<TreeItem<Unit>> {
    val numRoots = Random.nextInt(1, 4)
    return List(numRoots) { generateRandomTree(it + 1, 6) } // Adjust depth as needed
}

private fun generateRandomTree(index: Int, depth: Int): TreeItem<Unit>{
    val nodeTitle = "Item ${index.toString().toCharArray().joinToString(".")}"
    val numChildren = Random.nextInt(1, 4)

    val children = when (depth) {
        0 -> emptyList()
        else -> List(numChildren) { generateRandomTree(index * 10 + it + 1, depth - 1) }
    }

    generatedTreeSize++

    return TreeItem(
        icon = Graphics.folder,
        title = nodeTitle,
        children = children,
        data = Unit
    )
}
