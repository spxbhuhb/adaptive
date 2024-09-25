package `fun`.adaptive.auto.internal.origin

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.api.AutoApi
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.backend.BackendBase
import `fun`.adaptive.auto.internal.backend.BackendContext
import `fun`.adaptive.auto.internal.connector.DirectConnector
import `fun`.adaptive.auto.internal.connector.ServiceConnector
import `fun`.adaptive.auto.internal.frontend.AdatClassListFrontend
import `fun`.adaptive.auto.internal.frontend.FrontendBase
import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.service.getService
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.utility.CleanupHandler
import `fun`.adaptive.wireformat.api.Proto
import kotlin.time.Duration

class OriginBase<BE : BackendBase, FE : FrontendBase, VT, IT : AdatClass>(
    val worker: AutoWorker?,
    val handle: AutoHandle,
    serviceContext: ServiceContext?,
    defaultWireFormat: AdatClassWireFormat<*>?,
    trace: Boolean,
    register: Boolean = true,
    builder: OriginBase<BE, FE, VT, IT>.() -> Unit
) {

    val logger = getLogger("fun.adaptive.auto.${handle.globalId.toShort()}.${handle.peerId}").also {
        if (trace) it.enableFine()
    }

    val context = BackendContext<IT>(
        handle,
        worker?.scope,
        logger,
        Proto,
        defaultWireFormat,
        LamportTimestamp(handle.peerId, 1),
    )

    lateinit var backend: BE

    lateinit var frontend: FE

    init {
        builder()
        backend.frontend = frontend

        if (register) {
            requireNotNull(worker) { "cannot register without a worker" }

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

    fun connectInfo(): AutoConnectInfo<VT> {
        @Suppress("UNCHECKED_CAST")
        return backend.connectInfo() as AutoConnectInfo<VT>
    }

    fun <IT> connectInfo(itemId: ItemId): AutoConnectInfo<IT> {
        @Suppress("UNCHECKED_CAST")
        return backend.connectInfo(itemId) as AutoConnectInfo<IT>
    }

    @Suppress("UNCHECKED_CAST")
    fun <IT> connectInfo(filterFun: (IT) -> Boolean): AutoConnectInfo<IT>? {
        val safeFrontend = frontend
        check(safeFrontend is AdatClassListFrontend<*>) { "connecting with filter function is supported only by AdatClassListFrontend" }
        val itemId = safeFrontend.values.firstOrNull { filterFun(it as IT) }?.let { safeFrontend.itemId(it) } ?: return null
        return backend.connectInfo(itemId) as AutoConnectInfo<IT>
    }

    suspend fun connect(
        transport: ServiceCallTransport = requireNotNull(worker?.adapter?.transport),
        waitForSync: Duration? = null,
        connectInfoFun: suspend () -> AutoConnectInfo<VT>
    ): OriginBase<BE, FE, VT, IT> {
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

    /**
     * Connect to the origin backend with [DirectConnector]. This works only if the origin is
     * in the same VM as the connecting peer.
     */
    suspend fun connectDirect(
        waitForSync: Duration? = null,
        connectInfoFun: suspend () -> AutoConnectInfo<VT>
    ): OriginBase<BE, FE, VT, IT> {

        checkNotNull(worker) { "cannot connect directly without a worker" }

        val connectInfo = connectInfoFun()

        val peer = worker.backends[connectInfo.originHandle.globalId]
        checkNotNull(peer) { "direct backend for ${connectInfo.originHandle.globalId} is missing" }

        backend.addPeer(DirectConnector(peer), connectInfo.originTime)
        peer.addPeer(DirectConnector(backend), backend.context.time)

        if (waitForSync != null) waitForSync(connectInfo, waitForSync)

        return this
    }

    suspend fun waitForSync(connectInfo: AutoConnectInfo<VT>, timeout: Duration) {
        backend.waitForSync(connectInfo, timeout)
    }

    fun update(original: IT, new: IT) {
        frontend.also {
            if (it is AdatClassListFrontend<*>) {
                @Suppress("UNCHECKED_CAST")
                it as AdatClassListFrontend<IT>
                it.update(original, new)
            }
        }
    }
}