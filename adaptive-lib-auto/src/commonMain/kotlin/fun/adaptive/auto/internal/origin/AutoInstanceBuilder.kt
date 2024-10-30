package `fun`.adaptive.auto.internal.origin

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.api.AutoApi
import `fun`.adaptive.auto.api.AutoCollectionListener
import `fun`.adaptive.auto.api.AutoGeneric
import `fun`.adaptive.auto.api.AutoItemListener
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.backend.AutoBackend
import `fun`.adaptive.auto.internal.connector.DirectConnector
import `fun`.adaptive.auto.internal.connector.ServiceConnector
import `fun`.adaptive.auto.internal.frontend.AutoFrontend
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.PeerId
import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.service.getService
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.utility.CleanupHandler
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.wireformat.WireFormatProvider
import kotlin.collections.get
import kotlin.time.Duration

class AutoInstanceBuilder<BE : AutoBackend<*>, FE : AutoFrontend<VT,IT>, VT, IT : AdatClass>(
    val origin: Boolean,
    val persistent: Boolean,
    val service: Boolean,
    val collection: Boolean,
    val value: VT?,
    val info: AutoConnectionInfo<VT>?,
    val infoFun: (suspend () -> AutoConnectionInfo<VT>)?,
    val defaultWireFormat: AdatClassWireFormat<*>?,
    val wireFormatProvider: WireFormatProvider,
    val itemListener: AutoItemListener<IT>?,
    val collectionListener: AutoCollectionListener<IT>?,
    val worker: AutoWorker?,
    val serviceContext: ServiceContext?,
    val trace: Boolean,
    val backendFun: (AutoInstanceBuilder<BE, FE, VT, IT>) -> BE,
    val frontendFun: (AutoInstanceBuilder<BE, FE, VT, IT>) -> FE,
) {

    val instance = AutoInstance<BE, FE, VT, IT>(defaultWireFormat, wireFormatProvider)

    lateinit var backend: BE
    lateinit var frontend: FE

    var handle : AutoHandle? = null

    fun build() {
        // Frontend exists both for origins and peers. We have to load it very early as
        // peer frontends may have persisted state which is used during initialization.

        frontend = frontendFun(this)
        frontend.load()

        if (origin) {
            buildOrigin()
        } else {
            buildPeer()
        }
    }

    // --------------------------------------------------------------------------------
    // Origin
    // --------------------------------------------------------------------------------

    fun buildOrigin() {

        if (frontend.handle == null) {
            check(frontend.valueOrNull == null) { "inconsistent frontend load, no handle with value" }
            check(value != null) { "cannot create origin without a value" }
            frontend.handle = AutoHandle.origin(collection)
            frontend.valueOrNull = value
        }

        backend = backendFun(this)

        addListener()
        registerWithWorker()
    }

    fun registerWithWorker() {
        if (! service) return

        requireNotNull(worker) { "cannot register without a worker" }

        worker.register(instance)

        if (serviceContext != null) {
            if (serviceContext.sessionOrNull != null) {
                serviceContext.addSessionCleanup(CleanupHandler { worker.deregister(instance) })
            } else {
                serviceContext.addContextCleanup(CleanupHandler { worker.deregister(instance) })
            }
        }
    }

    // --------------------------------------------------------------------------------
    // Peer
    // --------------------------------------------------------------------------------

    fun buildPeer() {
        // At this point we have a frontend which may or may not have a handle and a loaded value.
        // If we have handle and value we can go on, make everything and just launch the connection.
        // However, if we do not have these, we have to get a handle first.

        if (frontend.handle != null) {
            check(frontend.valueOrNull != null) { "inconsistent frontend load, handle with no value" }
            check(value == null) { "cannot use supplied value when frontend loaded another" }
            backend = backendFun(this)
        }
    }

    fun createServiceConnector() {

        val transport: ServiceCallTransport = requireNotNull(worker?.adapter?.transport) { "missing worker (cannot get transport)" },

        if (frontend.handle != null) {

        } else {
            checkNotNull(infoFun) { "cannot connect without a connection info function" }
            ServiceConnector(
                instance,
                getService<AutoApi>(transport),
                frontend.handle,
                infoFun,
                initiator = true,
                reconnect = true
            )
        }
    }

    suspend fun createDirectConnector(
        waitForSync: Duration? = null,
        connectInfoFun: suspend () -> AutoConnectionInfo<VT>,
    ): AutoInstance<BE, FE, VT, IT> {

        checkNotNull(worker) { "cannot connect directly without a worker" }

        val connectInfo = connectInfoFun()

        val peer = worker.instances[connectInfo.acceptingHandle.globalId]
        checkNotNull(peer) { "direct backend for ${connectInfo.acceptingHandle.globalId} is missing" }

        addPeer(DirectConnector(backend, peer), connectInfo.acceptingTime)
        peer.addPeer(DirectConnector(peer, backend), time)

        if (waitForSync != null) waitForSync(connectInfo, waitForSync)

        return this
    }


    // --------------------------------------------------------------------------------
    // Common
    // --------------------------------------------------------------------------------

    fun addListener() {
        if (itemListener != null) {
            instance.addListener(itemListener)
        }
        if (collectionListener != null) {
            check(collection) { "cannot add a collection listener to a non-collection instance" }
            instance.addListener(collectionListener)
        }
    }
}