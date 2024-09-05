package `fun`.adaptive.auto.api

import `fun`.adaptive.auto.backend.AutoService
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.AutoPropertyValue
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.operation.AutoAdd
import `fun`.adaptive.auto.model.operation.AutoEmpty
import `fun`.adaptive.auto.model.operation.AutoModify
import `fun`.adaptive.auto.model.operation.AutoMove
import `fun`.adaptive.auto.model.operation.AutoRemove
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.wireformat.WireFormatRegistry

@Adaptive
fun auto() {
    wireFormats()
    service { AutoService() }
    worker { AutoWorker() }
}

private fun wireFormats() {
    WireFormatRegistry += AutoHandle
    WireFormatRegistry += AutoConnectInfo
    WireFormatRegistry += AutoAdd
    WireFormatRegistry += AutoEmpty
    WireFormatRegistry += AutoModify
    WireFormatRegistry += AutoMove
    WireFormatRegistry += AutoRemove
    WireFormatRegistry += AutoPropertyValue
    WireFormatRegistry += LamportTimestamp
}