package `fun`.adaptive.ui.mpw.backends

import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.ui.mpw.model.SingularPaneItem

class UnitSingularContentViewBackend(
    workspace: MultiPaneWorkspace,
    paneDef: PaneDef,
    item: SingularPaneItem
) : SingularContentViewBackend<UnitSingularContentViewBackend>(workspace, paneDef, item)