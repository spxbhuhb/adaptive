package `fun`.adaptive.grove

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.grove.api.GroveRuntimeFragmentFactory
import `fun`.adaptive.grove.hydration.lfm.*
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.wireformat.WireFormatRegistry

class GroveRuntimeModule<FW : AbstractWorkspace, BW : AbstractWorkspace> : AppModule<FW, BW>() {

    override fun wireFormatInit(registry: WireFormatRegistry) = with(registry) {
        + LfmConst
        + LfmDescendant
        + LfmExternalStateVariable
        + LfmFragment
        + LfmInternalStateVariable
        + LfmMapping
    }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter) {
        + GroveRuntimeFragmentFactory
    }

}