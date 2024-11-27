package `fun`.adaptive.auto.internal.instance

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
import `fun`.adaptive.auto.internal.persistence.AutoExport
import `fun`.adaptive.auto.internal.persistence.AutoItemExport
import `fun`.adaptive.auto.internal.persistence.AutoItemPersistence
import `fun`.adaptive.auto.internal.persistence.AutoPersistence
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.auto.model.AutoConnectionType
import `fun`.adaptive.auto.model.AutoMetadata
import `fun`.adaptive.auto.model.ITEM_ID_ORIGIN
import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.utility.CleanupHandler
import `fun`.adaptive.utility.untilSuccess
import `fun`.adaptive.wireformat.WireFormatProvider
import `fun`.adaptive.wireformat.api.Proto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class AutoInstanceBuilder<BE : AutoBackend<IT>, PT : AutoPersistence<VT, IT>, VT, IT : AdatClass>(
    val origin: Boolean,
    val collection: Boolean,
    val info: AutoConnectionInfo<VT>? = null,
    val infoFun: ((instance: AutoInstance<BE, PT, VT, IT>) -> AutoConnectionInfo<VT>)? = null,
    val infoFunSuspend: InfoFunSuspend<BE, PT, VT, IT>? = null,
    val directPeer: AutoInstance<BE, PT, VT, IT>? = null,
    val defaultWireFormat: AdatClassWireFormat<*>?,
    wireFormatProvider: WireFormatProvider = Proto,
    val itemListener: AutoItemListener<IT>? = null,
    val collectionListener: AutoCollectionListener<IT>? = null,
    val worker: AutoWorker? = null,
    val serviceContext: ServiceContext? = null,
    val scope: CoroutineScope,
    val trace: Boolean,
    val backendFun: (AutoInstanceBuilder<BE, PT, VT, IT>, export: AutoExport<VT>) -> BE,
    val persistenceFun: (AutoInstanceBuilder<BE, PT, VT, IT>) -> PT,
) {

    @Suppress("UNCHECKED_CAST")
    val instance = if (collection) {
        AutoCollection<AutoCollectionBackend<IT>, AutoCollectionPersistence<IT>, IT>(
            origin, defaultWireFormat, wireFormatProvider, scope
        )
    } else {
        AutoItem<AutoItemBackend<IT>, AutoItemPersistence<IT>, IT>(
            origin, defaultWireFormat, wireFormatProvider, scope
        )
    } as AutoInstance<BE, PT, VT, IT>

    fun build(
        initialValue: VT?,
    ): AutoInstance<BE, PT, VT, IT> {
        if (trace) instance.logger.enableFine()

        instance.persistence = persistenceFun(this)

        val loadedExport = instance.persistence.load()

        @Suppress("UNCHECKED_CAST")
        val loadedInfo = loadedExport.meta?.connection as? AutoConnectionInfo<VT>

        val connectionInfo = when {
            loadedInfo != null -> loadedInfo
            info != null -> info
            origin -> AutoConnectionInfo.origin<VT>(collection)
            else -> null
        }

        val export = buildExport(loadedInfo, loadedExport, connectionInfo, initialValue)

        instance.trace { "connectionInfo: $connectionInfo" }

        instance.backend = backendFun(this, export)

        addListener()
        addCleanup()

        if (origin) {
            instance.setInfo(connectionInfo !!, worker, trace)
            if (loadedInfo == null) instance.persistenceInit()
        } else {
            connect(connectionInfo)
        }

        return instance
    }

    @Suppress("UNCHECKED_CAST")
    fun buildExport(
        loadedInfo: AutoConnectionInfo<VT>?,
        loadedExport: AutoExport<VT>,
        connectionInfo: AutoConnectionInfo<VT>?,
        initialValue: VT?
    ) =
        when {
            loadedInfo != null -> loadedExport
            collection -> {
                var itemId = ITEM_ID_ORIGIN

                AutoCollectionExport<IT>(
                    connectionInfo?.let { AutoMetadata(it, null, null) },
                    (initialValue as Collection<IT>).map {
                        AutoItemExport<IT>(null, itemId, null, it).also { itemId = itemId.increment() }
                    }
                )
            }
            else -> {
                AutoItemExport<IT>(
                    connectionInfo?.let { AutoMetadata(it, null, null) },
                    connectionInfo?.connectingHandle?.itemId,
                    null,
                    initialValue as IT?
                )
            }
        } as AutoExport<VT>

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

    private fun connect(connectionInfo: AutoConnectionInfo<VT>?) {
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
            return
        }

        error("cannot connect service, all options are missing")
    }

    fun connector(connectionInfo: AutoConnectionInfo<VT>) {
        if (connectionInfo.connectionType == AutoConnectionType.Service) {
            createServiceConnector(connectionInfo)
        } else {
            createDirectConnector(connectionInfo)
        }
    }

    fun createServiceConnector(connectionInfo: AutoConnectionInfo<VT>) {

        checkNotNull(worker) { "cannot register without a worker" }
        val adapter = checkNotNull(worker.adapter) { "cannot register without an adapter (the passed worker does not have one $worker)" }

        val connector =
            ServiceConnector(
                instance,
                getService<AutoApi>(adapter.transport),
                connectionInfo.acceptingHandle,
                initiator = true
            )

        instance.addConnector(connector)
        scope.launch { supervisorScope { connector.run() } }

    }

    fun createDirectConnector(connectionInfo: AutoConnectionInfo<VT>) {

        val peer = if (directPeer != null) {
            directPeer
        } else {
            checkNotNull(worker) { "cannot connect directly without a worker" }
            worker.instances[connectionInfo.acceptingHandle.globalId]
        }

        checkNotNull(peer) { "direct backend for ${connectionInfo.acceptingHandle.globalId} is missing" }

        val thisConnector = DirectConnector(instance, peer)
        instance.addConnector(thisConnector)
        scope.launch { supervisorScope { thisConnector.run(connectionInfo.acceptingTime) } }

        val peerConnector = DirectConnector(peer, instance)
        peer.addConnector(peerConnector)
        scope.launch { supervisorScope { peerConnector.run(instance.time) } }

    }

}