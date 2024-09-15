package `fun`.adaptive.auto.internal.origin

import `fun`.adaptive.adat.AdatClass
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
import kotlin.time.Duration

class OriginBase<BE : BackendBase, FE : FrontendBase, CT>(
    worker: AutoWorker?,
    val handle: AutoHandle,
    serviceContext: ServiceContext?,
    metadata: AdatClassMetadata,
    wireFormat: AdatClassWireFormat<*>,
    trace: Boolean,
    builder: OriginBase<BE, FE, CT>.() -> Unit
) {

    val logger = getLogger("fun.adaptive.auto.${handle.globalId.toShort()}.${handle.peerId}").also {
        if (trace) it.enableFine()
    }

    val context = BackendContext(
        handle,
        worker?.scope,
        logger,
        Proto,
        metadata,
        wireFormat,
        LamportTimestamp(handle.peerId, 1),
    )

    lateinit var backend: BE

    lateinit var frontend: FE

    init {
        builder()
        backend.frontEnd = frontend

        if (worker != null) {

            worker.register(backend)

            if (serviceContext != null) {
                if (serviceContext.sessionOrNull != null) {
                    serviceContext.addSessionCleanup(CleanupHandler { worker.deregister(backend) })
                } else {
                    serviceContext.addContextCleanup(CleanupHandler { worker.deregister(backend) })
                }
            }
        }
    }

    fun connectInfo(): AutoConnectInfo<CT> {
        @Suppress("UNCHECKED_CAST")
        return backend.connectInfo() as AutoConnectInfo<CT>
    }

    suspend fun connect(
        transport: ServiceCallTransport? = defaultServiceCallTransport,
        waitForSync: Duration? = null, connectInfoFun: suspend () -> AutoConnectInfo<CT>
    ): OriginBase<BE, FE, CT> {
        val scope = backend.context.scope
        check(scope != null) { "connecting is not possible when there is no worker passed during creation" }

        val autoService = getService<AutoApi>(transport)
        val connectInfo = connectInfoFun()

        backend.addPeer(
            ServiceConnector(
                connectInfo.connectingHandle,
                connectInfo.originHandle,
                autoService,
                logger,
                scope,
                1000
            ),
            connectInfo.originTime
        )

        autoService.addPeer(
            connectInfo.originHandle,
            connectInfo.connectingHandle,
            backend.context.time
        )

        if (waitForSync != null) waitForSync(connectInfo, waitForSync)

        return this
    }

    suspend fun waitForSync(connectInfo: AutoConnectInfo<CT>, timeout: Duration) {
        backend.waitForSync(connectInfo, timeout)
    }
}