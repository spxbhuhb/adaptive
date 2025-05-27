package `fun`.adaptive.ui.mpw.app

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.ui.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.fragments.centerPane
import `fun`.adaptive.ui.mpw.fragments.emptyCenterPane
import `fun`.adaptive.ui.snackbar.SnackType
import `fun`.adaptive.wireformat.WireFormatRegistry

class MultiPaneClientModule<WT : AbstractWorkspace> : AppModule<WT>() {

    override fun frontendAdapterInit(adapter : AdaptiveAdapter)= with(adapter.fragmentFactory) {
        add(MultiPaneWorkspace.WS_CENTER_PANE, ::centerPane)
        add(MultiPaneWorkspace.WSPANE_EMPTY, ::emptyCenterPane)
    }

}