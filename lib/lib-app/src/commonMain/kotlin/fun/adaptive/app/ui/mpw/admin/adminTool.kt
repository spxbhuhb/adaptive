package `fun`.adaptive.app.ui.mpw.admin

import `fun`.adaptive.app.ui.mpw.addContent
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.generated.resources.empty
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace.Companion.wsContext
import `fun`.adaptive.ui.mpw.backends.UnitPaneViewBackend
import `fun`.adaptive.ui.mpw.fragments.toolPane
import `fun`.adaptive.ui.mpw.model.SingularPaneItem
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeTheme
import `fun`.adaptive.ui.tree.TreeViewBackend
import `fun`.adaptive.ui.tree.tree
import `fun`.adaptive.ui.viewbackend.viewBackend

@Adaptive
fun adminTool(): AdaptiveFragment {

    val viewBackend = viewBackend(UnitPaneViewBackend::class)
    val module = fragment().wsContext<AppAdminModuleMpw<*,*>>()

    val model = TreeViewBackend(
        module.plugins.toTreeItems(),
        context = Unit,
        theme = TreeTheme.list,
        selectedFun = { _, item, _ -> fragment().addContent(item.data) }
    )

    toolPane(viewBackend) {
        tree(model)
    }

    return fragment()
}

private fun List<SingularPaneItem>.toTreeItems() =
    map {
        TreeItem(
            icon = it.icon ?: Graphics.empty,
            title = it.name,
            data = it,
            parent = null
        )
    }