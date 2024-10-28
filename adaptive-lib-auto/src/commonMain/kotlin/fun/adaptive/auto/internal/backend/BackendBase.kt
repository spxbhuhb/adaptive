package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.auto.internal.connector.AutoConnector
import `fun`.adaptive.auto.internal.frontend.FrontendBase
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.auto.model.AutoConnectionType
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.operation.AutoModify
import `fun`.adaptive.auto.model.operation.AutoOperation
import `fun`.adaptive.auto.model.operation.AutoSyncEnd
import `fun`.adaptive.reflect.CallSiteName
import `fun`.adaptive.utility.waitFor
import `fun`.adaptive.wireformat.WireFormatRegistry
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlin.time.Duration

abstract class BackendBase(
) : AutoConnector() {

    abstract val context: BackendContext<*>

    val globalId
        get() = context.handle.globalId

    var frontend: FrontendBase? = null

    // --------------------------------------------------------------------------------
    // Operations from the frontend
    // --------------------------------------------------------------------------------

    abstract fun modify(itemId: ItemId, propertyName: String, propertyValue: Any?)

    // --------------------------------------------------------------------------------
    // Operations from peers
    // --------------------------------------------------------------------------------

    override fun send(operation: AutoOperation) {
        //  When the backend acts as connector, send simply has to call receive.
        receive(operation)
    }

    fun receive(operation: AutoOperation) {
        trace { operation.toString() }
        operation.apply(this, commit = true)
    }

    abstract fun modify(operation: AutoModify, commit: Boolean)

    abstract fun syncEnd(operation: AutoSyncEnd, commit: Boolean)

    // --------------------------------------------------------------------------------
    // Peer synchronization
    // --------------------------------------------------------------------------------

    /**
     * Add a connection to a peer. The backend launches [syncPeer] in to send all known
     * operations that happened after the [peerTime] to the peer.
     */
    suspend fun addPeer(connector: AutoConnector, peerTime: LamportTimestamp): LamportTimestamp {
        context.addConnector(connector)
        supervisorScope { launch { syncPeer(connector, peerTime, null) } }
        return context.time
    }

    abstract suspend fun syncPeer(
        connector: AutoConnector,
        peerTime: LamportTimestamp,
        syncBatch: MutableList<AutoModify>?,
        sendSyncEnd: Boolean = true
    )

    fun removePeer(handle: AutoHandle) {
        context.removeConnector(handle)
    }

    // --------------------------------------------------------------------------------
    // Lifecycle
    // --------------------------------------------------------------------------------

    override suspend fun disconnect() {
        context.stop()
    }

    override fun dispose() {
        // nothing to do with this
    }

    fun removed(fromBackend: Boolean) {
        frontend?.removed(fromBackend)
    }

    // --------------------------------------------------------------------------------
    // Utility, common
    // --------------------------------------------------------------------------------

    fun close(operation: AutoOperation, commit: Boolean, fromBackend: Boolean) {

        if (commit) {
            frontend?.commit(initial = false, fromBackend)
            trace { "==== commit ====" }
        }

        val connectors = context.connectors
        val closePeers = connectors.map { it.peerHandle.peerId }
        val thisPeer = context.handle.peerId

        for (connector in connectors) {
            val peerHandle = connector.peerHandle
            val sourcePeer = operation.timestamp.peerId

            // do not send operations back to the peer that created them
            if (peerHandle.peerId == sourcePeer) {
                // println("BackendBase.SKIP: this=${context.handle} peer=${peerHandle.peerId} item=${peerHandle.itemId} operation.timestamp=${operation.timestamp}")
                continue
            }

            // do not send list and non-item operations to single-item peers
            if (peerHandle.itemId != null) {
                if (operation !is AutoModify) {
                    // println("BackendBase.SKIP: this=${context.handle} peer=${peerHandle.peerId} item=${peerHandle.itemId} operation.timestamp=${operation.timestamp}")
                    continue
                }
                if (operation.itemId != peerHandle.itemId) {
                    // println("BackendBase.SKIP: this=${context.handle} peer=${peerHandle.peerId} item=${peerHandle.itemId} operation.timestamp=${operation.timestamp}")
                    continue
                }
            }

            // do not send operations originated from peers far away
            // FIXME operation send for far peers
            if (sourcePeer != thisPeer && sourcePeer !in closePeers) {
                // println("BackendBase.SKIP: this=${context.handle} peer=${peerHandle.peerId} item=${peerHandle.itemId} operation.timestamp=${operation.timestamp}")
                continue
            }

            // println("BackendBase.SEND: this=${context.handle} peer=${peerHandle.peerId} item=${peerHandle.itemId} operation.timestamp=${operation.timestamp}")
            connector.send(operation)
        }
    }

    fun wireFormatFor(name: String?) =
        if (name == null) {
            requireNotNull(context.defaultWireFormat) { "missing wireformat for ${context.handle}"}
        } else {
            (WireFormatRegistry[name] as AdatCompanion<*>).adatWireFormat
        }


    fun connectInfo(type: AutoConnectionType, itemId: ItemId? = null) =
        with(context) {
            val time = context.nextTime()
            AutoConnectionInfo<Any>(type, handle, time, AutoHandle(handle.globalId, time.timestamp, itemId))
        }

    fun isSynced(connectInfo: AutoConnectionInfo<*>): Boolean {
        return context.time.timestamp >= connectInfo.originTime.timestamp
    }

    suspend fun waitForSync(connectInfo: AutoConnectionInfo<*>, timeout: Duration) {
        trace { "SYNC WAIT: $connectInfo" }
        waitFor(timeout) { isSynced(connectInfo) }
        trace { "SYNC WAIT END" }
    }

    @CallSiteName
    inline fun trace(callSiteName: String = "<unknown>", builder: () -> String) {
        trace(builder(), callSiteName)
    }

    open fun trace(message: String, callSiteName: String) {
        context.logger.fine { "[${callSiteName.substringAfterLast('.')} @ ${context.time}] $message" }
    }

}