package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.auto.internal.connector.AutoConnector
import `fun`.adaptive.auto.internal.origin.AutoInstance
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
import kotlin.time.Duration

abstract class AutoBackend<IT : AdatClass>(
    val instance: AutoInstance<*, *, *, IT>
) : AutoConnector() {

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

    abstract suspend fun syncPeer(
        connector: AutoConnector,
        peerTime: LamportTimestamp,
        syncBatch: MutableList<AutoModify>?,
        sendSyncEnd: Boolean = true,
    )

    // --------------------------------------------------------------------------------
    // Utility, common
    // --------------------------------------------------------------------------------

    fun close(operation: AutoOperation, commit: Boolean, fromBackend: Boolean) {

        if (commit) {
            instance.commit(initial = false, fromBackend)
            trace { "==== commit ====" }
        }

        val connectors = instance.connectors
        val closePeers = instance.closePeers
        val thisPeer = instance.handle.peerId

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
            requireNotNull(instance.defaultWireFormat) { "missing wireformat for $instance" }
        } else {
            (WireFormatRegistry[name] as AdatCompanion<*>).adatWireFormat
        }


    fun connectInfo(type: AutoConnectionType, itemId: ItemId? = null) =
        with(instance) {
            val time = nextTime()
            AutoConnectionInfo<Any>(type, handle, time, AutoHandle(handle.globalId, time.timestamp, itemId))
        }

    fun isSynced(connectInfo: AutoConnectionInfo<*>): Boolean {
        return instance.time.timestamp >= connectInfo.acceptingTime.timestamp
    }

    suspend fun waitForSync(connectInfo: AutoConnectionInfo<*>, timeout: Duration) {
        trace { "SYNC WAIT: $connectInfo" }
        waitFor(timeout) { isSynced(connectInfo) }
        trace { "SYNC WAIT END" }
    }

    @CallSiteName
    fun trace(callSiteName: String = "<unknown>", builder: () -> String) {
        instance.trace(callSiteName) { "[${callSiteName.substringAfterLast('.')} @ ${instance.time}] ${builder()}" }
    }

}