package `fun`.adaptive.ui.mpw.app

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.fragments.centerPane
import `fun`.adaptive.ui.mpw.fragments.emptyCenterPane

class MultiPaneClientModule<FW : AbstractWorkspace, BW : AbstractWorkspace> : AppModule<FW,BW>() {

    override fun frontendAdapterInit(adapter : AdaptiveAdapter)= with(adapter.fragmentFactory) {
        add(MultiPaneWorkspace.WS_CENTER_PANE, ::centerPane)
        add(MultiPaneWorkspace.WSPANE_EMPTY, ::emptyCenterPane)
    }

}