package `fun`.adaptive.grove.apm

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.grove.apm.model.ApmProjectItem
import `fun`.adaptive.grove.apm.model.ApmProjectItemSet
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.tree
import `fun`.adaptive.ui.workspace.Workspace.Companion.wsContext
import `fun`.adaptive.ui.workspace.wsToolPane

@Adaptive
fun apmProject(): AdaptiveFragment {
    val context = fragment().wsContext<ApmContext>()
    val items = root.toTreeItem { showItem(context, it) }.children

    wsToolPane(context.pane(apmProjectPaneKey)) {
        tree(items)
    }

    return fragment()
}

private fun showItem(context: ApmContext, item: TreeItem) {
    val centerKey = (item.data as String)
    context.activeRecipeKey.value = centerKey

    val ws = context.workspace
    ws.center.value = ws.panes.first { it.key == centerKey }.uuid
}

val root = ApmProjectItemSet(
    "Root",
    emptyList(),
    listOf(
        ApmProjectItem("Test", "./test", "grove:ufd:center")
    )
)
