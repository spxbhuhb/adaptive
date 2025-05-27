package `fun`.adaptive.ui.mpw.example

import `fun`.adaptive.log.exampleInfo
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.generated.resources.collapseAll
import `fun`.adaptive.ui.generated.resources.expandAll
import `fun`.adaptive.ui.generated.resources.unfold_less
import `fun`.adaptive.ui.generated.resources.unfold_more
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.backends.PaneViewBackend
import `fun`.adaptive.ui.mpw.model.AbstractPaneAction
import `fun`.adaptive.ui.mpw.model.PaneAction
import `fun`.adaptive.ui.mpw.model.PaneDef

class ExampleToolViewBackend(
    override val workspace: MultiPaneWorkspace,
    override val paneDef: PaneDef
) : PaneViewBackend<ExampleToolViewBackend>() {

    override fun paneActions(): List<AbstractPaneAction> {
        return listOf(
            PaneAction(Graphics.unfold_more, Strings.expandAll) { exampleInfo { "expand all click" } },
            PaneAction(Graphics.unfold_less, Strings.collapseAll) { exampleInfo { "collapse all click" } }
        )
    }
}