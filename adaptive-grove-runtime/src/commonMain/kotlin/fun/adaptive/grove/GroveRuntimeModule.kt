package `fun`.adaptive.grove

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.grove.api.GroveRuntimeFragmentFactory
import `fun`.adaptive.grove.hydration.lfm.*
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.wireformat.WireFormatRegistry

class GroveRuntimeModule<WT,AT:Any> : AppModule<WT, AT>() {

    override fun WireFormatRegistry.init() {
        this += LfmConst
        this += LfmDescendant
        this += LfmExternalStateVariable
        this += LfmFragment
        this += LfmInternalStateVariable
        this += LfmMapping
    }

    override fun AdaptiveAdapter.init() {
        fragmentFactory += GroveRuntimeFragmentFactory
    }

}