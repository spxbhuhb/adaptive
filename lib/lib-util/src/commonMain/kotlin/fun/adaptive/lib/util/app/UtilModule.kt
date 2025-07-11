package `fun`.adaptive.lib.util.app

import `fun`.adaptive.lib.util.datetime.TimeRange
import `fun`.adaptive.lib.util.temporal.model.TemporalIndexEntry
import `fun`.adaptive.lib.util.temporal.model.TemporalIndexHeader
import `fun`.adaptive.lib.util.url.Url
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.runtime.BackendWorkspace
import `fun`.adaptive.wireformat.WireFormatRegistry

open class UtilModule<FW : AbstractWorkspace, BW : AbstractWorkspace> : AppModule<FW, BW>() {

    override fun wireFormatInit(registry: WireFormatRegistry) = with(registry) {
        + TemporalIndexHeader
        + TemporalIndexEntry
        + Url
        + TimeRange
    }

}