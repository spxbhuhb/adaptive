package `fun`.adaptive.auto.api

import `fun`.adaptive.auto.backend.AutoService
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.auto.model.AutoConnectionType
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.AutoPropertyValue
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.operation.AutoAdd
import `fun`.adaptive.auto.model.operation.AutoEmpty
import `fun`.adaptive.auto.model.operation.AutoModify
import `fun`.adaptive.auto.model.operation.AutoMove
import `fun`.adaptive.auto.model.operation.AutoRemove
import `fun`.adaptive.auto.model.operation.AutoSyncBatch
import `fun`.adaptive.auto.model.operation.AutoSyncEnd
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.wireformat.WireFormatRegistry
import `fun`.adaptive.wireformat.builtin.EnumWireFormat
import kotlin.enums.EnumEntries

@Adaptive
fun auto() {
    wireFormats()
    service { AutoService() }
    worker { AutoWorker() }
}

private fun wireFormats() {
    WireFormatRegistry.plusAssign(AutoHandle)
    WireFormatRegistry += AutoConnectionInfo
    WireFormatRegistry += AutoAdd
    WireFormatRegistry += AutoEmpty
    WireFormatRegistry += AutoModify
    WireFormatRegistry += AutoMove
    WireFormatRegistry += AutoRemove
    WireFormatRegistry += AutoSyncBatch
    WireFormatRegistry += AutoSyncEnd
    WireFormatRegistry += AutoPropertyValue
    WireFormatRegistry += LamportTimestamp
    enum(AutoConnectionType.entries)
}

private inline fun <reified E : Enum<E>> enum(entries: EnumEntries<E>) {
    WireFormatRegistry["fun.adaptive.auto.model.${E::class.simpleName}"] = EnumWireFormat<E>(entries)
}