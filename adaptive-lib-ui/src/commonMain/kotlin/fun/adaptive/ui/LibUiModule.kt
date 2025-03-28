package `fun`.adaptive.ui

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.ui.builtin.commonMainStringsStringStore0
import `fun`.adaptive.ui.snackbar.SnackType
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.wireformat.WireFormatRegistry

class LibUiModule : AppModule<Workspace>() {

    override fun WireFormatRegistry.init() {
        this += SnackType
    }

    override suspend fun loadResources() {
        commonMainStringsStringStore0.load()
    }

    override fun AdaptiveAdapter.init() {
        fragmentFactory += LibFragmentFactory
    }

}