package `fun`.adaptive.sandbox.recipe.ui.tree

import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.tree.TreeItem

fun staticTree(icon: GraphicsResourceSet?) = listOf(
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