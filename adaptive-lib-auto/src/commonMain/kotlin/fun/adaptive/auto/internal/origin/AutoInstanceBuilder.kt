package `fun`.adaptive.auto.internal.origin

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.api.AutoApi
import `fun`.adaptive.auto.api.AutoCollectionListener
import `fun`.adaptive.auto.api.AutoItemListener
import `fun`.adaptive.auto.api.InfoFunSuspend
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.backend.AutoBackend
import `fun`.adaptive.auto.internal.backend.AutoCollectionBackend
import `fun`.adaptive.auto.internal.backend.AutoItemBackend
import `fun`.adaptive.auto.internal.connector.DirectConnector
import `fun`.adaptive.auto.internal.connector.ServiceConnector
import `fun`.adaptive.auto.internal.frontend.AutoCollectionFrontend
import `fun`.adaptive.auto.internal.frontend.AutoFrontend
import `fun`.adaptive.auto.internal.frontend.AutoItemFrontend
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.service.getService
import `fun`.adaptive.utility.CleanupHandler
import `fun`.adaptive.utility.untilSuccess
import `fun`.adaptive.wireformat.WireFormatProvider
import `fun`.adaptive.wireformat.api.Proto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class AutoInstanceBuilder<BE : AutoBackend<IT>, FE : AutoFrontend<VT, IT>, VT, IT : AdatClass>(
    val origin: Boolean,
    val persistent: Boolean,
    val service: Boolean,
    val collection: Boolean,
    val info: AutoConnectionInfo<VT>? = null,
    val infoFun: ((instance: AutoInstance<BE, FE, VT, IT>) -> AutoConnectionInfo<VT>)? = null,
    val infoFunSuspend: InfoFunSuspend<BE, FE, VT, IT>? = null,
    val defaultWireFormat: AdatClassWireFormat<*>?,
    wireFormatProvider: WireFormatProvider = Proto,
    val itemListener: AutoItemListener<IT>? = null,
    val collectionListener: AutoCollectionListener<IT>? = null,
    val worker: AutoWorker? = null,
    val serviceContext: ServiceContext? = null,
    val scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
    val trace: Boolean,
    val backendFun: (AutoInstanceBuilder<BE, FE, VT, IT>, info: AutoConnectionInfo<VT>?, value: VT?) -> BE,
    val frontendFun: (AutoInstanceBuilder<BE, FE, VT, IT>) -> FE,
) {

    @Suppress("UNCHECKED_CAST")
    val instance = if (collection) {
        AutoCollection<AutoCollectionBackend<IT>, AutoCollectionFrontend<IT>, IT>(defaultWireFormat, wireFormatProvider, scope)
    } else {
        AutoItem<AutoItemBackend<IT>, AutoItemFrontend<IT>, IT>(defaultWireFormat, wireFormatProvider, scope)
    } as AutoInstance<BE, FE, VT, IT>

    lateinit var backend: BE
    lateinit var frontend: FE

    fun build(
        initialValue: VT?,
    ): AutoInstance<BE, FE, VT, IT> {
        instance.frontend = frontendFun(this)

        val (loadedInfo, loadedValue) = frontend.load()

        val connectionInfo = when {
            loadedInfo != null -> loadedInfo
            info != null -> info
            origin -> AutoConnectionInfo.origin<VT>(collection)
            else -> null
        }

        val value = when {
            loadedValue != null -> loadedValue
            initialValue != null -> initialValue
            else -> null
        }

        backend = backendFun(this, connectionInfo, value)

        addListener()
        addCleanup()

        if (origin) {
            instance.setInfo(connectionInfo !!, worker)
        } else {
            createConnector(connectionInfo)
        }

        return instance
    }

    fun addListener() {
        if (itemListener != null) {
            instance.addListener(itemListener)
        }
        if (collectionListener != null) {
            check(collection) { "cannot add a collection listener to a non-collection instance" }
            instance.addListener(collectionListener)
        }
    }

    fun addCleanup() {
        if (serviceContext != null && worker != null) {
            if (serviceContext.sessionOrNull != null) {
                serviceContext.addSessionCleanup(CleanupHandler { worker.deregister(instance) })
            } else {
                serviceContext.addContextCleanup(CleanupHandler { worker.deregister(instance) })
            }
        }
    }

    fun createConnector(connectionInfo: AutoConnectionInfo<VT>?) {
        if (service) {
            connect(connectionInfo) { createServiceConnector(it) }
        } else {
            connect(connectionInfo) { createDirectConnector(it) }
        }
    }

    private fun connect(connectionInfo: AutoConnectionInfo<VT>?, connector: (AutoConnectionInfo<VT>) -> Unit) {
        if (connectionInfo != null) {
            instance.setInfo(connectionInfo, worker)
            connector(connectionInfo)
            return
        }

        if (infoFun != null) {
            infoFun(instance).also {
                instance.setInfo(it, worker)
                connector(it)
            }
            return
        }

        if (infoFunSuspend != null) {
            checkNotNull(scope)

            scope.launch {
                untilSuccess { infoFunSuspend(instance) }.also {
                    instance.setInfo(it, worker)
                    connector(it)
                }
            }
        }

        error("cannot connect service, all options are missing")
    }

    fun createServiceConnector(connectionInfo: AutoConnectionInfo<VT>) {

        checkNotNull(worker) { "cannot register without a worker" }
        val adapter = checkNotNull(worker.adapter) { "cannot register without an adapter (the passed worker does not have one $worker)" }

        val connector =
            ServiceConnector(
                instance,
                getService<AutoApi>(adapter.transport),
                connectionInfo.acceptingHandle,
                connecting = true
            )

        instance.addConnector(connector)
        scope.launch { supervisorScope { connector.run(connectionInfo.acceptingTime) } }

    }

    fun createDirectConnector(connectionInfo: AutoConnectionInfo<VT>) {

        checkNotNull(worker) { "cannot connect directly without a worker" }

        val peer = worker.instances[connectionInfo.acceptingHandle.globalId]
        checkNotNull(peer) { "direct backend for ${connectionInfo.acceptingHandle.globalId} is missing" }

        val thisConnector = DirectConnector(instance, peer)
        instance.addConnector(thisConnector)
        scope.launch { supervisorScope { thisConnector.run(connectionInfo.acceptingTime) } }

        val peerConnector = DirectConnector(peer, instance)
        peer.addConnector(peerConnector)
        scope.launch { supervisorScope { peerConnector.run(instance.time) } }

    }

}