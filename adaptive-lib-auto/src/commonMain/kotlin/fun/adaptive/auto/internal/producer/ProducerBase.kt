package `fun`.adaptive.auto.internal.producer

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.store.AdatStore
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.api.AutoApi
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.backend.BackendBase
import `fun`.adaptive.auto.internal.backend.BackendContext
import `fun`.adaptive.auto.internal.connector.DirectConnector
import `fun`.adaptive.auto.internal.connector.ServiceConnector
import `fun`.adaptive.auto.internal.frontend.FrontendBase
import `fun`.adaptive.auto.internal.origin.OriginBase
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.auto.model.AutoConnectionType
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.AdaptiveProducer
import `fun`.adaptive.log.AdaptiveLogger
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.service.getService
import `fun`.adaptive.utility.safeLaunch
import `fun`.adaptive.wireformat.api.Proto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class ProducerBase<BE : BackendBase, FE : FrontendBase, VT, IT : AdatClass>(
    override val binding: AdaptiveStateVariableBinding<VT>,
    val connect: suspend () -> AutoConnectionInfo<VT>,
    val peer: OriginBase<*, *, VT, IT>? = null,
    val trace: Boolean
) : AdatStore<IT>(), AdaptiveProducer<VT> {

    override var latestValue: VT? = null

    val scope: CoroutineScope = CoroutineScope(adapter.dispatcher)

    lateinit var logger: AdaptiveLogger
    lateinit var connectInfo: AutoConnectionInfo<VT>
    lateinit var context: BackendContext<IT>

    lateinit var backend: BE
    lateinit var frontend: FE

    val adapter
        get() = binding.targetFragment.adapter

    abstract val defaultWireFormat : AdatClassWireFormat<*>?

    override fun start() {
        scope.launch {
            connectInfo = connect()
            val originHandle = connectInfo.originHandle
            val connectingHandle = connectInfo.connectingHandle

            logger = getLogger("fun.adaptive.auto.internal.producer.${this::class.simpleName}.${connectingHandle.globalId.toShort()}.${connectingHandle.peerId}")
            if (trace) logger.enableFine()

            context = BackendContext(
                connectingHandle,
                scope,
                logger,
                Proto,
                defaultWireFormat,
                LamportTimestamp(connectingHandle.peerId, 0),
            )

            build()

            backend.frontend = frontend

            if (connectInfo.connectionType == AutoConnectionType.Service) {
                connectService(connectingHandle, originHandle)
            } else {
                connectDirect()
            }
        }
    }

    suspend fun connectService(connectingHandle: AutoHandle, originHandle: AutoHandle) {
        adapter.backend.firstImpl<AutoWorker>().register(backend)

        val autoService = getService<AutoApi>(adapter.transport)

        backend.addPeer(
            ServiceConnector(connectingHandle, originHandle, autoService, logger, scope, 1000),
            connectInfo.originTime
        )

        autoService.addPeer(originHandle, connectingHandle, backend.context.time)
    }

    fun connectDirect() {
        checkNotNull(peer)
        backend.addPeer(DirectConnector(backend, peer.backend), connectInfo.originTime)
        peer.backend.addPeer(DirectConnector(peer.backend, backend), backend.context.time)
    }

    abstract fun build()

    override fun stop() {
        if (connectInfo.connectionType == AutoConnectionType.Service) {
            adapter.backend.firstImpl<AutoWorker>().deregister(backend)
        }

        scope.safeLaunch(logger) {
            backend.disconnect()
        }
    }

    override fun toString(): String {
        return "AutoInstance($binding)"
    }

}