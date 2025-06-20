package `fun`.adaptive.grove.sheet

import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.model.WsContext

class SheetViewContext(
    override val workspace: MultiPaneWorkspace
) : WsContext {
    val focusedView = storeFor<SheetViewController?> { null }
}