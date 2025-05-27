package `fun`.adaptive.grove.apm

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.grove.apm.model.ApmWsItem
import `fun`.adaptive.grove.ufd.UfdWsContext
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewBackend
import `fun`.adaptive.ui.tree.tree
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace.Companion.wsContext
import `fun`.adaptive.ui.mpw.model.Pane
import `fun`.adaptive.ui.mpw.fragments.toolPane

@Adaptive
fun apmProject(pane: Pane<*>): AdaptiveFragment {
    val context = fragment().wsContext<ApmWsContext>()

    val treeViewBackend = TreeViewBackend(
        root.map { it.toTreeItem(context) },
        selectedFun = { viewModel, item, modifiers -> showItem(context, item, modifiers) },
        context = Unit
    )

    toolPane(context.pane(ApmWsContext.APM_PROJECT_TOOL_KEY)) {
        tree(treeViewBackend)
    }

    return fragment()
}

private fun showItem(context: ApmWsContext, item: TreeItem<ApmWsItem>, modifiers: Set<EventModifier>) {
    //context.workspace.addContent(item.data, modifiers)
}

val root =
    listOf(
        ApmWsItem(
            "Test",
            UfdWsContext.WSIT_UFD_FRAGMENT,
            path = "./test"
        )
    )
