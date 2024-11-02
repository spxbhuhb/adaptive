package `fun`.adaptive.auto.internal.origin

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.api.AutoApi
import `fun`.adaptive.auto.api.AutoCollectionListener
import `fun`.adaptive.auto.api.AutoItemListener
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.backend.AutoBackend
import `fun`.adaptive.auto.internal.connector.DirectConnector
import `fun`.adaptive.auto.internal.connector.ServiceConnector
import `fun`.adaptive.auto.internal.frontend.AutoFrontend
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.service.getService
import `fun`.adaptive.utility.CleanupHandler
import `fun`.adaptive.utility.untilSuccess
import `fun`.adaptive.wireformat.WireFormatProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AutoInstanceBuilder<BE : AutoBackend<IT>, FE : AutoFrontend<VT, IT>, VT, IT : AdatClass>(
    val origin: Boolean,
    val persistent: Boolean,
    val service: Boolean,
    val collection: Boolean,
    val info: AutoConnectionInfo<VT>?,
    val infoFun: ((instance : AutoInstance<BE, FE, VT, IT>) -> AutoConnectionInfo<VT>)?,
    val infoFunSuspend: (suspend (instance : AutoInstance<BE, FE, VT, IT>) -> AutoConnectionInfo<VT>)?,
    val defaultWireFormat: AdatClassWireFormat<*>?,
    wireFormatProvider: WireFormatProvider,
    val itemListener: AutoItemListener<IT>?,
    val collectionListener: AutoCollectionListener<IT>?,
    val worker: AutoWorker?,
    val serviceContext: ServiceContext?,
    val scope: CoroutineScope,
    val trace: Boolean,
    val backendFun: (AutoInstanceBuilder<BE, FE, VT, IT>, info: AutoConnectionInfo<VT>?, value: VT?) -> BE,
    val frontendFun: (AutoInstanceBuilder<BE, FE, VT, IT>) -> FE,
) {

    val instance = AutoInstance<BE, FE, VT, IT>(defaultWireFormat, wireFormatProvider, scope)

    lateinit var backend: BE
    lateinit var frontend: FE

    fun build(
        initialValue: VT?,
    ) {
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

        if (service) {
            registerWithWorker()
        }

        addCleanup()

        if (! origin) {
            createConnector(connectionInfo)
        } else {
            setLogger(connectionInfo !!)
        }
    }

    private fun setLogger(connectionInfo: AutoConnectionInfo<VT>) {
        val handle = connectionInfo.connectingHandle
        val base = "auto.${handle.globalId.toShort()}.${handle.peerId}"
        instance.logger = getLogger(base + (handle.itemId?.let { ".$it" } ?: ""))
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

    fun registerWithWorker() {
        requireNotNull(worker) { "cannot register without a worker" }
        worker.register(instance)
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
            setLogger(connectionInfo)
            connector(connectionInfo)
            return
        }

        if (infoFun != null) {
            infoFun(instance).also {
                setLogger(it)
                connector(it)
            }
            return
        }

        if (infoFunSuspend != null) {
            checkNotNull(scope)

            scope.launch {
                untilSuccess { infoFunSuspend(instance) }.also {
                    setLogger(it)
                    connector(it)
                }
            }
        }

        error("cannot connect service, all options are missing")
    }

    fun createServiceConnector(connectionInfo: AutoConnectionInfo<VT>) {

        checkNotNull(worker) { "cannot register without a worker" }
        val adapter = checkNotNull(worker.adapter) { "cannot register without an adapter (the passed worker does not have one $worker)" }

        ServiceConnector(
            instance,
            getService<AutoApi>(adapter.transport),
            connectionInfo
        )
    }

    fun createDirectConnector(connectionInfo: AutoConnectionInfo<VT>) {

        checkNotNull(worker) { "cannot connect directly without a worker" }

        val peer = worker.instances[connectionInfo.acceptingHandle.globalId]
        checkNotNull(peer) { "direct backend for ${connectionInfo.acceptingHandle.globalId} is missing" }

        instance.addPeer(DirectConnector(instance, peer), connectionInfo.acceptingTime)
        peer.addPeer(DirectConnector(peer, instance), instance.time)

    }
}