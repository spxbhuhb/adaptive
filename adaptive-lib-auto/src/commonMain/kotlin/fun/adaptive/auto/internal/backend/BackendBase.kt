package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.auto.internal.connector.AutoConnector
import `fun`.adaptive.auto.internal.frontend.FrontendBase
import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.operation.AutoModify
import `fun`.adaptive.auto.model.operation.AutoOperation
import `fun`.adaptive.reflect.CallSiteName
import `fun`.adaptive.utility.safeLaunch
import `fun`.adaptive.utility.waitFor
import `fun`.adaptive.wireformat.WireFormatRegistry
import kotlin.time.Duration

abstract class BackendBase(
    peerHandle: AutoHandle
) : AutoConnector(peerHandle) {

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

    // --------------------------------------------------------------------------------
    // Peer synchronization
    // --------------------------------------------------------------------------------

    /**
     * Add a connection to a peer. The backend launches [syncPeer] in to send all known
     * operations that happened after the [peerTime] to the peer.
     */
    fun addPeer(connector: AutoConnector, peerTime: LamportTimestamp): LamportTimestamp {
        val scope = checkNotNull(context.scope) { "cannot add a peer when there is no worker passed during creation" }
        context.addConnector(connector)
        scope.safeLaunch(context.logger) { syncPeer(connector, peerTime) }
        return context.time
    }

    abstract suspend fun syncPeer(connector: AutoConnector, peerTime: LamportTimestamp)

    fun removePeer(handle: AutoHandle) {
        context.removeConnector(handle)
    }

    // --------------------------------------------------------------------------------
    // Lifecycle
    // --------------------------------------------------------------------------------

    override suspend fun disconnect() {
        context.stop()
    }

    override fun onDisconnect() {
        // nothing to do with this
    }

    fun removed() {
        frontend?.removed()
    }

    // --------------------------------------------------------------------------------
    // Utility, common
    // --------------------------------------------------------------------------------

    fun close(operation: AutoOperation, commit: Boolean) {

        if (commit) {
            frontend?.commit()
            trace { "==== commit ====" }
        }

        for (connector in context.connectors) {
            // do not send operations back to the peer that created them
            if (connector.peerHandle.peerId == operation.timestamp.peerId) continue
            // do not send list operations to single-item peers
            if (connector.peerHandle.itemId != null && operation !is AutoModify) continue

            connector.send(operation)
        }
    }

    fun wireFormatFor(name: String?) =
        if (name == null) {
            requireNotNull(context.defaultWireFormat)
        } else {
            (WireFormatRegistry[name] as AdatCompanion<*>).adatWireFormat
        }


    fun connectInfo(itemId : ItemId? = null) =
        with(context) {
            val time = context.nextTime()
            AutoConnectInfo<Any>(handle, time, AutoHandle(handle.globalId, time.timestamp, itemId))
        }

    fun isSynced(connectInfo: AutoConnectInfo<*>): Boolean {
        return context.time.timestamp >= connectInfo.originTime.timestamp
    }

    suspend fun waitForSync(connectInfo: AutoConnectInfo<*>, timeout: Duration) {
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