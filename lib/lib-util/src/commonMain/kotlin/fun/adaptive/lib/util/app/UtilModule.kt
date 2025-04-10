package `fun`.adaptive.lib.util.app

import `fun`.adaptive.lib.util.temporal.model.TemporalIndexEntry
import `fun`.adaptive.lib.util.temporal.model.TemporalIndexHeader
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.wireformat.WireFormatRegistry

open class UtilModule<WT : AbstractWorkspace> : AppModule<WT>() {

    override fun wireFormatInit(registry: WireFormatRegistry) = with(registry) {
        + TemporalIndexHeader
        + TemporalIndexEntry
    }

}