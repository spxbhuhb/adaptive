package `fun`.adaptive.auto.producer

import `fun`.adaptive.adat.AdatChange
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.AdatContext
import `fun`.adaptive.adat.applyContext
import `fun`.adaptive.adat.deepCopy
import `fun`.adaptive.adat.store.AdatStore
import `fun`.adaptive.auto.LamportTimestamp
import `fun`.adaptive.auto.api.AutoApi
import `fun`.adaptive.auto.backend.BackendContext
import `fun`.adaptive.auto.backend.PropertyBackend
import `fun`.adaptive.auto.connector.ServiceConnector
import `fun`.adaptive.auto.frontend.AdatClassFrontend
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

/**
 * Connect to peers with [AutoApi] and produce an instance that is
 * synchronized between peers.
 *
 * Property changes (on any peer) generate a new instance (on all peers).
 *
 * Each new instance is validated by default, so fragments that use values
 * produced by [autoInstance] can safely use the validation result as it is
 * up-to-date all the time.
 *
 * @param    onChange       Called after a new instance is generated, but before the
 *                          state of the fragment is updated.
 * @param    connect        A function to get the connection info. Typically, this is created by
 *                          a service call.
 *                          of this instance, validates that copy and then returns with it.
 * @param    binding        Set by the compiler plugin, ignore it.
 *
 * @return   `null` (it takes time to connect and synchronize)
 */
@Producer
fun <A : AdatClass<A>> autoInstance(
    onChange: ((newValue: A) -> Unit)? = null,
    binding: AdaptiveStateVariableBinding<A>? = null,
    connect: suspend () -> AutoConnectInfo
): A? {
    checkNotNull(binding)
    checkNotNull(binding.adatCompanion)

    val store = AutoInstance(binding, connect, onChange)

    binding.targetFragment.addProducer(store)

    return null
}

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
            )

            backend.frontEnd = frontend

            adapter.backend.firstImpl<AutoWorker>().register(backend)

            val autoService = getService<AutoApi>()

            backend.addPeer(
                ServiceConnector(originHandle, autoService, scope, 1000),
                connectInfo.originTime
            )

            autoService.addPeer(originHandle, connectingHandle, backend.context.time)

            // TODO waitForSync(originWorker, originHandle, connectingWorker, connectingHandle)

        }
    }

    override fun stop() {
        // copy store is event-driven
    }

    override fun toString(): String {
        return "AutoInstance($binding)"
    }

}