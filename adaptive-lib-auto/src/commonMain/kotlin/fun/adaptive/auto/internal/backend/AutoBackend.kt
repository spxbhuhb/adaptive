package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.auto.internal.connector.AutoConnector
import `fun`.adaptive.auto.internal.origin.AutoInstance
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.operation.AutoAdd
import `fun`.adaptive.auto.model.operation.AutoRemove
import `fun`.adaptive.auto.model.operation.AutoUpdate
import `fun`.adaptive.auto.model.operation.AutoSyncEnd
import `fun`.adaptive.reflect.CallSiteName
import `fun`.adaptive.utility.waitFor
import `fun`.adaptive.wireformat.WireFormatRegistry
import kotlin.time.Duration

abstract class AutoBackend<IT : AdatClass>(
    val instance: AutoInstance<*, *, *, IT>,
) {

    abstract fun getItem(itemId: ItemId): IT?

    // --------------------------------------------------------------------------------
    // Operations from the frontend
    // --------------------------------------------------------------------------------

    abstract fun add(timestamp: LamportTimestamp, item : IT) : Pair<AutoAdd, IT>

    abstract fun update(timestamp: LamportTimestamp, itemId: ItemId, updates : Collection<Pair<String, Any?>>) : Pair<AutoUpdate,IT>

    abstract fun remove(timestamp: LamportTimestamp, itemId : ItemId) : Pair<AutoRemove,IT?>

    // --------------------------------------------------------------------------------
    // Operations from peers
    // --------------------------------------------------------------------------------

    abstract fun add(operation: AutoAdd): Pair<LamportTimestamp?,IT>

    abstract fun update(operation: AutoUpdate): Triple<LamportTimestamp?,IT,IT>

    abstract fun remove(operation: AutoRemove): Pair<LamportTimestamp?, IT?>

    open fun syncEnd(operation: AutoSyncEnd) = Unit

    // --------------------------------------------------------------------------------
    // Peer synchronization
    // --------------------------------------------------------------------------------

    abstract suspend fun syncPeer(
        connector: AutoConnector,
        peerTime: LamportTimestamp,
        syncBatch: MutableList<AutoUpdate>?,
        sendSyncEnd: Boolean = true,
    )

    fun wireFormatFor(name: String?) =
        if (name == null) {
            requireNotNull(instance.defaultWireFormat) { "missing wireformat for $instance" }
        } else {
            (WireFormatRegistry[name] as AdatCompanion<*>).adatWireFormat
        }

    fun isSynced(connectInfo: AutoConnectionInfo<*>): Boolean {
        return instance.time.timestamp >= connectInfo.acceptingTime.timestamp
    }

    suspend fun waitForSync(connectInfo: AutoConnectionInfo<*>, timeout: Duration) {
        trace { "SYNC WAIT: $connectInfo" }
        waitFor(timeout) { isSynced(connectInfo) }
        trace { "SYNC WAIT END" }
    }

    // --------------------------------------------------------------------------------
    // Utility, access
    // --------------------------------------------------------------------------------


    @CallSiteName
    fun trace(callSiteName: String = "<unknown>", builder: () -> String) {
        instance.trace(callSiteName) { "[${callSiteName.substringAfterLast('.')} @ ${instance.time}] ${builder()}" }
    }

}