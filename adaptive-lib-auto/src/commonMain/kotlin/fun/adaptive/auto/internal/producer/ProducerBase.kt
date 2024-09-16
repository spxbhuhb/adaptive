package `fun`.adaptive.auto.internal.producer

import `fun`.adaptive.adat.store.AdatStore
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.api.AutoApi
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.backend.BackendBase
import `fun`.adaptive.auto.internal.backend.BackendContext
import `fun`.adaptive.auto.internal.connector.ServiceConnector
import `fun`.adaptive.auto.internal.frontend.FrontendBase
import `fun`.adaptive.auto.model.AutoConnectInfo
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

abstract class ProducerBase<BE : BackendBase, FE : FrontendBase, T>(
    override val binding: AdaptiveStateVariableBinding<T>,
    val connect: suspend () -> AutoConnectInfo<T>,
    val trace: Boolean
) : AdatStore(), AdaptiveProducer<T> {

    override var latestValue: T? = null

    val scope: CoroutineScope = CoroutineScope(adapter.dispatcher)

    lateinit var logger: AdaptiveLogger
    lateinit var context: BackendContext

    lateinit var backend: BE
    lateinit var frontend: FE

    val adapter
        get() = binding.targetFragment.adapter

    abstract val defaultWireFormat : AdatClassWireFormat<*>?

    override fun start() {
        scope.launch {
            val connectInfo = connect()
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

            backend.frontEnd = frontend

            adapter.backend.firstImpl<AutoWorker>().register(backend)

            val autoService = getService<AutoApi>()

            backend.addPeer(
                ServiceConnector(connectingHandle, originHandle, autoService, logger, scope, 1000),
                connectInfo.originTime
            )

            autoService.addPeer(originHandle, connectingHandle, backend.context.time)
        }
    }

    abstract fun build()

    override fun stop() {
        adapter.backend.firstImpl<AutoWorker>().deregister(backend)

        scope.safeLaunch(logger) {
            backend.disconnect()
        }
    }

    override fun toString(): String {
        return "AutoInstance($binding)"
    }

}