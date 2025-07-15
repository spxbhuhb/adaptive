package `fun`.adaptive.sandbox.app

import `fun`.adaptive.cookbook.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.sandbox.CookbookFragmentFactory
import `fun`.adaptive.sandbox.recipe.ui.mpw.WorkspaceRecipePaneFragmentFactory
import `fun`.adaptive.sandbox.support.ExampleEnum
import `fun`.adaptive.wireformat.WireFormatRegistry

class CookbookModuleClient<FW : AbstractWorkspace, BW : AbstractWorkspace> : CookbookModule<FW,BW>() {

    override fun resourceInit() {
        application.stringStores += commonMainStringsStringStore0
    }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) {
        adapter.fragmentFactory += CookbookFragmentFactory
        adapter.fragmentFactory += WorkspaceRecipePaneFragmentFactory
    }
}