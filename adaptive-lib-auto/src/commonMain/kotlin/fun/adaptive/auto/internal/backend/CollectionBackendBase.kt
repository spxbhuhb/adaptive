package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.toArray
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.operation.AutoAdd
import `fun`.adaptive.auto.model.operation.AutoEmpty
import `fun`.adaptive.auto.model.operation.AutoModify
import `fun`.adaptive.auto.model.operation.AutoOperation
import `fun`.adaptive.auto.model.operation.AutoRemove
import `fun`.adaptive.auto.model.operation.AutoSyncEnd
import `fun`.adaptive.reflect.CallSiteName

abstract class CollectionBackendBase<A : AdatClass>(
    peerHandle: AutoHandle,
) : BackendBase(peerHandle) {

    abstract val items: MutableMap<ItemId, PropertyBackend<A>>

    /**
     * Contains modification that should be applied when [AutoSyncEnd] arrives.
     */
    val afterSync = mutableListOf<AutoModify>()

    // --------------------------------------------------------------------------------
    // Operations from the frontend
    // --------------------------------------------------------------------------------

    fun add(item: A, parentItemId: ItemId?, commit: Boolean) {
        val itemId = context.nextTime()
        addItem(itemId, parentItemId, item)

        val wireFormatName = wireFormatNameOrNull(item)

        val operation = AutoAdd(
            timestamp = itemId,
            itemId = itemId,
            wireFormatName,
            parentItemId,
            encode(wireFormatName, item.toArray()) // FIXME clean up the array/adat class confusion
        )

        trace { "FE -> BE  itemId=$itemId .. commit true .. $operation" }

        close(operation, commit)
    }

    abstract fun remove(itemId: ItemId, commit: Boolean)

    abstract fun removeAll(itemIds: Set<ItemId>, commit: Boolean)

    // --------------------------------------------------------------------------------
    // Operations from peers
    // --------------------------------------------------------------------------------

    abstract fun empty(operation: AutoEmpty, commit: Boolean)

    abstract fun syncEnd(operation: AutoSyncEnd, commit: Boolean)

    abstract fun add(operation: AutoAdd, commit: Boolean)

    abstract fun remove(operation: AutoRemove, commit: Boolean)

    // --------------------------------------------------------------------------------
    // Helpers
    // --------------------------------------------------------------------------------

    abstract fun addItem(itemId: ItemId, parentItemId: ItemId?, value: A)

    fun wireFormatNameOrNull(item: AdatClass): String? {
        val itemWireFormatName = item.adatCompanion.wireFormatName
        // TODO I'm not sure about using null here, might cause problems during runtime if the configurations mismatch
        return if (itemWireFormatName == context.defaultWireFormat?.wireFormatName) null else itemWireFormatName
    }

    @CallSiteName
    fun closeListOp(operation: AutoOperation, itemIds: Set<ItemId>, commit: Boolean, callSiteName: String = "") {
//        if (context.time < operation.timestamp) {
//            context.receive(operation.timestamp)
            trace(callSiteName) { "BE -> BE  itemIds=${itemIds} .. commit $commit .. $operation" }
            close(operation, commit)
//        } else {
//            trace(callSiteName) { "BE -> BE  SKIP  $operation" }
//        }
    }

    fun encode(wireFormatName: String?, values: Array<Any?>) =
        wireFormatFor(wireFormatName)
            .wireFormatEncode(context.wireFormatProvider.encoder(), values)
            .pack()

    fun decode(wireFormatName: String?, payload: ByteArray) =
        wireFormatFor(wireFormatName)
            .wireFormatDecode(context.wireFormatProvider.decoder(payload))
}