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
import `fun`.adaptive.auto.internal.persistence.AutoCollectionExport
import `fun`.adaptive.auto.internal.persistence.AutoCollectionPersistence
import `fun`.adaptive.auto.internal.persistence.AutoItemExport
import `fun`.adaptive.auto.internal.persistence.AutoItemPersistence
import `fun`.adaptive.auto.internal.persistence.AutoPersistence
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

class AutoInstanceBuilder<BE : AutoBackend<IT>, PT : AutoPersistence<VT, IT>, VT, IT : AdatClass>(
    val origin: Boolean,
    val persistent: Boolean,
    val service: Boolean,
    val collection: Boolean,
    val info: AutoConnectionInfo<VT>? = null,
    val infoFun: ((instance: AutoInstance<BE, PT, VT, IT>) -> AutoConnectionInfo<VT>)? = null,
    val infoFunSuspend: InfoFunSuspend<BE, PT, VT, IT>? = null,
    val defaultWireFormat: AdatClassWireFormat<*>?,
    wireFormatProvider: WireFormatProvider = Proto,
    val itemListener: AutoItemListener<IT>? = null,
    val collectionListener: AutoCollectionListener<IT>? = null,
    val worker: AutoWorker? = null,
    val serviceContext: ServiceContext? = null,
    val scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
    val trace: Boolean,
    val backendFun: (AutoInstanceBuilder<BE, PT, VT, IT>, info: AutoConnectionInfo<VT>?, value: VT?) -> BE,
    val persistenceFun: (AutoInstanceBuilder<BE, PT, VT, IT>) -> PT,
) {

    @Suppress("UNCHECKED_CAST")
    val instance = if (collection) {
        AutoCollection<AutoCollectionBackend<IT>, AutoCollectionPersistence<IT>, IT>(defaultWireFormat, wireFormatProvider, scope)
    } else {
        AutoItem<AutoItemBackend<IT>, AutoItemPersistence<IT>, IT>(defaultWireFormat, wireFormatProvider, scope)
    } as AutoInstance<BE, PT, VT, IT>

    fun build(
        initialValue: VT?,
    ): AutoInstance<BE, PT, VT, IT> {
        if (trace) instance.logger.enableFine()

        instance.persistence = persistenceFun(this)

        val (loadedInfo, loadedValue) = load()

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

        instance.trace("BUILD") { "connectionInfo: $connectionInfo value: $value" }

        instance.backend = backendFun(this, connectionInfo, value)

        addListener()
        addCleanup()

        if (origin) {
            instance.setInfo(connectionInfo !!, worker, trace)
        } else {
            createConnector(connectionInfo)
        }

        return instance
    }

    @Suppress("UNCHECKED_CAST")
    fun load(): Pair<AutoConnectionInfo<VT>?, VT> {
        val export = instance.persistence.load()
        when (export) {
            // FIXME the first cast ( as AutoConnectionInfo<VT>) is here because the compiler plugin does not handle the type correctly
            is AutoItemExport<*> -> return (export.meta?.connection as? AutoConnectionInfo<VT> to export.item as VT)
            is AutoCollectionExport<*> -> return (export.meta?.connection as? AutoConnectionInfo<VT> to export.items as VT)
            else -> error("unknown persistence class: ${instance.persistence}")
        }
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
            instance.setInfo(connectionInfo, worker, trace)
            connector(connectionInfo)
            return
        }

        if (infoFun != null) {
            infoFun(instance).also {
                instance.setInfo(it, worker, trace)
                connector(it)
            }
            return
        }

        if (infoFunSuspend != null) {
            checkNotNull(scope)

            scope.launch {
                untilSuccess { infoFunSuspend(instance) }.also {
                    instance.setInfo(it, worker, trace)
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