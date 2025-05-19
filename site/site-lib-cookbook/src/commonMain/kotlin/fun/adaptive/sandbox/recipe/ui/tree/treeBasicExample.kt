package `fun`.adaptive.sandbox.recipe.ui.tree

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.generated.resources.folder
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.tree.TreeViewBackend
import `fun`.adaptive.ui.tree.tree

@Adaptive
fun treeBasicExample(): AdaptiveFragment {

    column {
        text("static - double click to expand")
        column {
            borders.outline
            tree(TreeViewBackend(staticTree(Graphics.folder), context = Unit))
        }
    }

    return fragment()
}