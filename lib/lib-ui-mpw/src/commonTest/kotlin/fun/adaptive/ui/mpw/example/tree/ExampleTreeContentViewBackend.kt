package `fun`.adaptive.ui.mpw.example.tree

import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.backends.PaneViewBackend
import `fun`.adaptive.ui.mpw.model.PaneDef

class ExampleTreeContentViewBackend(
    override val workspace: MultiPaneWorkspace,
    override val paneDef: PaneDef,
    val content : ExampleValue?
) : PaneViewBackend<ExampleTreeContentViewBackend>()