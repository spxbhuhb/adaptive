package `fun`.adaptive.auto.internal.origin

import `fun`.adaptive.adat.metadata.AdatClassMetadata
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.backend.BackendBase
import `fun`.adaptive.auto.internal.backend.BackendContext
import `fun`.adaptive.auto.internal.frontend.FrontendBase
import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.wireformat.protobuf.ProtoWireFormatProvider

class OriginBase<BE : BackendBase, FE : FrontendBase>(
    worker: AutoWorker,
    metadata: AdatClassMetadata,
    wireFormat: AdatClassWireFormat<*>,
    trace: Boolean,
    builder: OriginBase<BE, FE>.() -> Unit
) {

    val handle = AutoHandle(UUID(), 1)

    val logger = getLogger("fun.adaptive.auto.${handle.globalId.toShort()}.${handle.clientId}").also {
        if (trace) it.enableFine()
    }

    val context = BackendContext(
        handle,
        worker.scope,
        logger,
        ProtoWireFormatProvider(),
        metadata,
        wireFormat,
        LamportTimestamp(1, 1)
    )

    lateinit var backend: BE

    lateinit var frontend: FE

    init {
        builder()
        backend.frontEnd = frontend
        worker.register(backend)
    }

    fun connectInfo(): AutoConnectInfo {
        return backend.connectInfo()
    }
}