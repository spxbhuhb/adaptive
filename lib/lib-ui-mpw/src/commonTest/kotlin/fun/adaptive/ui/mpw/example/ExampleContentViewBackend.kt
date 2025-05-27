package `fun`.adaptive.ui.mpw.example

import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.backends.PaneViewBackend
import `fun`.adaptive.ui.mpw.model.PaneDef

class ExampleContentViewBackend(
    override val workspace: MultiPaneWorkspace,
    override val paneDef: PaneDef,
    val content : String = "Example content"
) : PaneViewBackend<ExampleContentViewBackend>()