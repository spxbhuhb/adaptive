package `fun`.adaptive.ui.mpw.backends

import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.model.PaneDef

/**
 * Basic pane view backend with no functions and data.
 */
class UnitPaneViewBackend(
    override val workspace: MultiPaneWorkspace,
    override val paneDef: PaneDef
) : PaneViewBackend<UnitPaneViewBackend>()