package `fun`.adaptive.doc.app

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.runtime.AbstractWorkspace

class DocExampleModuleClient<FW : AbstractWorkspace, BW : AbstractWorkspace> : DocExampleModule<FW,BW>() {

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) {
        adapter.fragmentFactory += ExampleFragmentFactory
    }

}