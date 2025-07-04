package `fun`.adaptive.sandbox.recipe.ui.tree

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.generated.resources.folder
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewBackend
import `fun`.adaptive.ui.tree.tree

/**
 * # Basic tree
 *
 * - the [tree](fragment://) fragment displays a tree of [TreeItem](class://) instances
 * - width is maxWidth by default
 * - height is calculated from the tree itself, taking item state (opened/closed) into account
 */
@Adaptive
fun treeBasicExample(): AdaptiveFragment {

    val treeBackend = TreeViewBackend(
        exampleTree(Graphics.folder),
        singleClickOpen = true,
        context = Unit
    )

    tree(treeBackend)

    return fragment()
}

fun exampleTree(icon: GraphicsResourceSet?) = listOf(
    TreeItem(
        icon = icon,
        title = "Item 1",
        data = Unit,
        parent = null
    ).also { item ->
        item.children = listOf(
            TreeItem(
                icon = icon,
                title = "Item 1.1",
                data = Unit,
                parent = item
            )
        )
    },
    TreeItem(
        icon = icon,
        title = "Item 2",
        data = Unit,
        parent = null
    ).also { item ->
        item.children = listOf(
            TreeItem(
                icon = icon,
                title = "Item 2.1",
                data = Unit,
                parent = item
            )
        )
    }
)