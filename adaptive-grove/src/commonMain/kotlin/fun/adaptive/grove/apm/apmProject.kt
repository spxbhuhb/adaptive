package `fun`.adaptive.grove.apm

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.grove.apm.model.ApmWsItem
import `fun`.adaptive.grove.resources.cards
import `fun`.adaptive.grove.ufd.UfdWsContext
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.tree.tree
import `fun`.adaptive.ui.workspace.Workspace.Companion.wsContext
import `fun`.adaptive.ui.workspace.wsToolPane

@Adaptive
fun apmProject(): AdaptiveFragment {
    val context = fragment().wsContext<ApmWsContext>()

    val treeViewModel = TreeViewModel(
        root.map { it.toTreeItem() },
        selectedFun = { viewModel, item, modifiers -> showItem(context, item, modifiers) },
        context = Unit
    )

    wsToolPane(context.pane(ApmWsContext.APM_PROJECT_TOOL_KEY)) {
        tree(treeViewModel)
    }

    return fragment()
}

private fun showItem(context: ApmWsContext, item: TreeItem<ApmWsItem>, modifiers: Set<EventModifier>) {
    context.workspace.addContent(item.data, modifiers)
}

val root =
    listOf(
        ApmWsItem(
            "Test",
            Graphics.cards,
            UfdWsContext.FRAGMENT_ITEM_TYPE,
            tooltip = "./test",
            path = "./test"
        )
    )
