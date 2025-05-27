package `fun`.adaptive.app.ws.admin

import `fun`.adaptive.app.ws.wsAddContent
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.generated.resources.empty
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeTheme
import `fun`.adaptive.ui.tree.TreeViewBackend
import `fun`.adaptive.ui.tree.tree
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace.Companion.wsContext
import `fun`.adaptive.ui.mpw.model.SingularPaneItem
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.ui.mpw.fragments.toolPane

@Adaptive
fun wsAppAdminTool(pane: PaneDef<*>): AdaptiveFragment {

    val module = fragment().wsContext<AppAdminWsModule<*>>()

    val model = TreeViewBackend(
        module.adminItems.toTreeItems(),
        context = Unit,
        theme = TreeTheme.list,
        selectedFun = { _, item, _ -> fragment().wsAddContent(item.data) }
    )

    toolPane(pane) {
        tree(model)
    }

    return fragment()
}

fun List<SingularPaneItem>.toTreeItems() =
    map {
        TreeItem<SingularPaneItem>(
            icon = it.icon ?: Graphics.empty,
            title = it.name,
            data = it,
            parent = null
        )
    }