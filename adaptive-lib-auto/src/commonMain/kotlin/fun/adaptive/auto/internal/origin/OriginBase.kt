package `fun`.adaptive.auto.internal.origin

import `fun`.adaptive.adat.metadata.AdatClassMetadata
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.api.AutoApi
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.backend.BackendBase
import `fun`.adaptive.auto.internal.backend.BackendContext
import `fun`.adaptive.auto.internal.connector.ServiceConnector
import `fun`.adaptive.auto.internal.frontend.FrontendBase
import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.service.defaultServiceCallTransport
import `fun`.adaptive.service.getService
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.utility.CleanupHandler
import `fun`.adaptive.wireformat.api.Proto

class OriginBase<BE : BackendBase, FE : FrontendBase>(
    worker: AutoWorker,
    val handle : AutoHandle,
    serviceContext: ServiceContext?,
    metadata: AdatClassMetadata,
    wireFormat: AdatClassWireFormat<*>,
    trace: Boolean,
    builder: OriginBase<BE, FE>.() -> Unit
) {

    val logger = getLogger("fun.adaptive.auto.${handle.globalId.toShort()}.${handle.clientId}").also {
        if (trace) it.enableFine()
    }

    val context = BackendContext(
        handle,
        worker.scope,
        logger,
        Proto,
        metadata,
        wireFormat,
        LamportTimestamp(handle.clientId, handle.clientId),
    )

    lateinit var backend: BE

    lateinit var frontend: FE

    init {
        builder()
        backend.frontEnd = frontend
        worker.register(backend)

        if (serviceContext != null) {
            if (serviceContext.sessionOrNull != null) {
                serviceContext.addSessionCleanup(CleanupHandler { worker.deregister(backend) })
            } else {
                serviceContext.addContextCleanup(CleanupHandler { worker.deregister(backend) })
            }
        }
    }

    fun connectInfo(): AutoConnectInfo {
        return backend.connectInfo()
    }

    suspend fun connect(transport: ServiceCallTransport? = defaultServiceCallTransport, waitForSync: Boolean = false, connectInfoFun: suspend () -> AutoConnectInfo): OriginBase<BE, FE> {

        val autoService = getService<AutoApi>(transport)
        val connectInfo = connectInfoFun()

        backend.addPeer(
            ServiceConnector(
                connectInfo.connectingHandle,
                connectInfo.originHandle,
                autoService,
                logger,
                backend.context.scope,
                1000
            ),
            connectInfo.originTime
        )

        autoService.addPeer(
            connectInfo.originHandle,
            connectInfo.connectingHandle,
            backend.context.time
        )

        if (waitForSync) waitForSync(connectInfo)

        return this
    }

    suspend fun waitForSync(connectInfo: AutoConnectInfo) {
        backend.waitForSync(connectInfo)
    }
}