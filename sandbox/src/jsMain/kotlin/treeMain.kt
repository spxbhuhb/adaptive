import `fun`.adaptive.cookbook.add
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.sandbox.apartment
import `fun`.adaptive.sandbox.crop_5_4
import `fun`.adaptive.sandbox.meeting_room
import `fun`.adaptive.sandbox.stacks
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.builtin.collapse_all
import `fun`.adaptive.ui.builtin.expand_all
import `fun`.adaptive.ui.builtin.folder
import `fun`.adaptive.ui.builtin.remove
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.denseIconTheme
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.menu.contextMenu
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.tree.TreeViewModel.Companion.defaultSelectedFun
import `fun`.adaptive.ui.tree.tree
import kotlin.random.Random

@Adaptive
fun treeMain(): AdaptiveFragment {

    val treeViewModel = TreeViewModel(
        staticTree,
        selectedFun = ::defaultSelectedFun,
        multiSelect = true
    )

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
                borders.outline .. padding { 16.dp } .. backgrounds.surfaceVariant
                tree(treeViewModel, ::contextMenuBuilder)
            }
        }

//        grid {
//            rowTemplate(24.dp, 24.dp, 1.fr)
//            text("random $generatedTreeSize")
//            row {
//                gap { 16.dp }
//                actionIcon(Graphics.expand_all, theme = denseIconTheme) .. onClick { generatedTree.forEach { it.expandAll() } }
//                actionIcon(Graphics.collapse_all, theme = denseIconTheme) .. onClick { generatedTree.forEach { it.collapseAll() } }
//            }
//            column {
//                borders.outline .. maxHeight .. verticalScroll
//                tree(generatedTree, selectedFun = { item, _ -> item.selected = true })
//            }
//        }
    }

    return fragment()
}

fun apply(tree: List<TreeItem<Unit>>, treeItem: TreeItem<Unit>, menuItem: MenuItem<String>) {
    if (menuItem.data == "add") {
        treeItem.children = treeItem.children + TreeItem(
            icon = Graphics.folder,
            title = "Item ${treeItem.children.size + 1}",
            data = Unit,
            parent = null
        )
        if (! treeItem.open) treeItem.open = true
    } else {
        println("remove")
    }
}

fun treeMenu() = listOf(
    MenuItem<String>(Graphics.add, "Add item", "add"),
    MenuItem<String>(Graphics.remove, "Remove item", "remove")
)

val menu = treeMenu()

@Adaptive
fun contextMenuBuilder(
    hide: () -> Unit,
    treeItem: TreeItem<Unit>
) {
    column {
        zIndex { 200 }
        contextMenu(menu) { menuItem, _ -> apply(staticTree, treeItem, menuItem); hide() }
    }
}

val staticTree = listOf(
    TreeItem<Unit>(
        icon = Graphics.apartment,
        title = "1. épület",
        data = Unit,
        parent = null
    ).also { item ->
        item.children = listOf(
            TreeItem<Unit>(
                icon = Graphics.stacks,
                title = "1. emelet",
                data = Unit,
                parent = null
            ).also { item ->
                item.children = listOf(
                    TreeItem(
                        icon = Graphics.meeting_room,
                        title = "2. szoba",
                        data = Unit,
                        parent = item
                    )
                )
            }
        )
    },
    TreeItem<Unit>(
        icon = Graphics.apartment,
        title = "2. épület",
        data = Unit,
        parent = null
    ).also { item ->
        item.children = listOf(
            TreeItem(
                icon = Graphics.stacks,
                title = "1. emelet",
                data = Unit,
                parent = null
            ).also { item ->
                item.children = listOf(
                    TreeItem(
                        icon = Graphics.meeting_room,
                        title = "2. szoba",
                        data = Unit,
                        parent = item
                    )
                )
            }
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

var generatedTreeSize = 0

val generatedTree = generate()
