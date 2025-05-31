package `fun`.adaptive.lib.util.app

import `fun`.adaptive.lib.util.temporal.model.TemporalIndexEntry
import `fun`.adaptive.lib.util.temporal.model.TemporalIndexHeader
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.runtime.BackendWorkspace
import `fun`.adaptive.wireformat.WireFormatRegistry

open class UtilServerModule<WT : BackendWorkspace> : AppModule<WT>() {

    override fun wireFormatInit(registry: WireFormatRegistry) = with(registry) {
        + TemporalIndexHeader
        + TemporalIndexEntry
    }

}