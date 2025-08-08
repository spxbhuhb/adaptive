package `fun`.adaptive.doc.example.mpw

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.grove.generated.resources.redo
import `fun`.adaptive.grove.generated.resources.undo
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.generated.resources.*
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.backends.PaneViewBackend
import `fun`.adaptive.ui.mpw.fragments.toolPane
import `fun`.adaptive.ui.mpw.model.AbstractPaneAction
import `fun`.adaptive.ui.mpw.model.PaneAction
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.ui.mpw.model.PaneMenuAction
import `fun`.adaptive.ui.snackbar.infoNotification
import `fun`.adaptive.ui.viewbackend.viewBackend

/**
 * # Tool pane
 *
 * Use [toolPane](fragment://) to add header and actions to a tool pane.
 *
 * You can create tool panes without this, you are free to add whatever content you want.
 *
 * You can get the backend of the plane with [viewBackend](function://). The backend is added
 * as a [local context](def://) by the workspace.
 */
@Adaptive
fun mpwToolPaneExample(): AdaptiveFragment {

    val viewBackend = viewBackend(ExampleToolViewBackend::class)

    toolPane(viewBackend) {
        text("Example tool content")
    }

    return fragment()
}

class ExampleToolViewBackend(
    override val workspace: MultiPaneWorkspace,
    override val paneDef: PaneDef
) : PaneViewBackend<ExampleToolViewBackend>() {

    val toolMenu = listOf(
        MenuItem(Graphics.add, "data-1", "Example 1"),
        MenuItem(Graphics.undo, "data-2", "Example 2"),
        MenuItem(Graphics.redo, "data-3", "Example 3"),
    )

    override fun getPaneActions(): List<AbstractPaneAction> {
        return listOf(
            PaneAction(Graphics.unfold_more, Strings.expandAll) { infoNotification("expand all") },
            PaneAction(Graphics.unfold_less, Strings.collapseAll) { infoNotification("collapse all") },
            PaneMenuAction(Graphics.more_vert, Strings.actions, toolMenu, { infoNotification("menu: ${it.menuItem.data}") })
        )
    }
}

