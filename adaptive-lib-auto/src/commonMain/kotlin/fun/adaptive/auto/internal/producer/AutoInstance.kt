package `fun`.adaptive.auto.internal.producer

import `fun`.adaptive.adat.AdatChange
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.AdatContext
import `fun`.adaptive.adat.applyContext
import `fun`.adaptive.adat.deepCopy
import `fun`.adaptive.adat.store.AdatStore
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.api.AutoApi
import `fun`.adaptive.auto.internal.backend.BackendContext
import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.internal.connector.ServiceConnector
import `fun`.adaptive.auto.internal.frontend.AdatClassFrontend
import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.auto.worker.AutoWorker
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.AdaptiveProducer
import `fun`.adaptive.foundation.producer.Producer
import `fun`.adaptive.service.getService
import `fun`.adaptive.wireformat.protobuf.ProtoWireFormatProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AutoInstance<A : AdatClass<A>>(
    override val binding: AdaptiveStateVariableBinding<A>,
    val connect: suspend () -> AutoConnectInfo,
    val onChange: ((newValue: A) -> Unit)?
) : AdatStore, AdaptiveProducer<A> {

    override var latestValue: A? = null

    lateinit var scope: CoroutineScope

    lateinit var backendContext: BackendContext
    lateinit var backend: PropertyBackend
    lateinit var frontend: AdatClassFrontend<A>

    @Suppress("UNCHECKED_CAST") // TODO should we create a binding for adat classes specifically?
    val companion = binding.adatCompanion !! as AdatCompanion<A>

    val adapter
        get() = binding.targetFragment.adapter

    val itemId
        get() = LamportTimestamp(0, 0) // does not matter for single instances

    fun replaceValue(newValue: A) {
        latestValue = makeCopy(newValue, null)
        binding.targetFragment.setDirty(binding.indexInTargetState, true)
    }

    fun setProperty(path: List<String>, value: Any?) {
        latestValue = latestValue?.let { makeCopy(it, AdatChange(path, value)) }
        onChange?.invoke(latestValue !!)
        binding.targetFragment.setDirty(binding.indexInTargetState, true)
    }

    fun makeCopy(value: A, change: AdatChange?) =
        value.deepCopy(change).also { it.applyContext(AdatContext(null, null, this, null)) }

    override fun start() {
        CoroutineScope(adapter.dispatcher).launch {
            scope = this

            val connectInfo = connect()
            val originHandle = connectInfo.originHandle
            val connectingHandle = connectInfo.connectingHandle

            backendContext = BackendContext(
                connectingHandle,
                scope,
                ProtoWireFormatProvider(),
                companion.adatMetadata,
                companion.adatWireFormat,
                true,
                LamportTimestamp(connectingHandle.clientId, 0),
            )

            backend = PropertyBackend(
                backendContext,
                itemId,
                null,
                null
            )

            frontend = AdatClassFrontend(
                backend,
                companion,
                null,
                null
            ) {
                latestValue = frontend.value
                latestValue?.validate()
                binding.targetFragment.setDirty(binding.indexInTargetState, true) // TODO make a separate binding for producers
            }

            backend.frontEnd = frontend

            adapter.backend.firstImpl<AutoWorker>().register(backend)

            val autoService = getService<AutoApi>()

            backend.addPeer(
                ServiceConnector(originHandle, autoService, scope, 1000),
                connectInfo.originTime
            )

            autoService.addPeer(originHandle, connectingHandle, backend.context.time)
        }
    }

    override fun stop() {
        adapter.backend.firstImpl<AutoWorker>().deregister(backend)
    }

    override fun toString(): String {
        return "AutoInstance($binding)"
    }

}