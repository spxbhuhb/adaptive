package `fun`.adaptive.app.ws.admin

import `fun`.adaptive.app.ws.wsAddContent
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.builtin.empty
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeTheme
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.tree.tree
import `fun`.adaptive.ui.workspace.Workspace.Companion.wsContext
import `fun`.adaptive.ui.workspace.model.SingularWsItem
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.wsToolPane

@Adaptive
fun wsAppAdminTool(pane: WsPane<*, *>): AdaptiveFragment {

    val module = fragment().wsContext<AppAdminWsModule<*>>()

    val model = TreeViewModel(
        module.adminItems.toTreeItems(),
        context = Unit,
        theme = TreeTheme.list,
        selectedFun = { _, item, _ -> fragment().wsAddContent(item.data) }
    )

    wsToolPane(pane) {
        tree(model)
    }

    return fragment()
}

fun List<SingularWsItem>.toTreeItems() =
    map {
        TreeItem<SingularWsItem>(
            icon = it.icon ?: Graphics.empty,
            title = it.name,
            data = it,
            parent = null
        )
    }