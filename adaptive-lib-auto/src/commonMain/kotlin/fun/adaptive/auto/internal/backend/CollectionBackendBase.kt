package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.toArray
import `fun`.adaptive.auto.model.ClientId
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.operation.AutoAdd
import `fun`.adaptive.auto.model.operation.AutoEmpty
import `fun`.adaptive.auto.model.operation.AutoOperation
import `fun`.adaptive.auto.model.operation.AutoRemove
import `fun`.adaptive.reflect.CallSiteName

abstract class CollectionBackendBase(
    clientId: ClientId
) : BackendBase(clientId) {

    abstract val items: MutableMap<ItemId, PropertyBackend>

    abstract val defaultWireFormatName: String

    // --------------------------------------------------------------------------------
    // Operations from the frontend
    // --------------------------------------------------------------------------------

    fun add(item: AdatClass<*>, parentItemId: ItemId?, commit: Boolean, distribute: Boolean) {
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

        trace { "FE -> BE  itemId=$itemId .. commit true .. distribute true .. $operation" }

        close(operation, commit, distribute)
    }

    abstract fun remove(itemId: ItemId, commit: Boolean, distribute: Boolean)

    abstract fun removeAll(itemIds: Set<ItemId>, commit: Boolean, distribute: Boolean)

    // --------------------------------------------------------------------------------
    // Operations from peers
    // --------------------------------------------------------------------------------

    abstract fun empty(operation: AutoEmpty, commit: Boolean, distribute: Boolean)

    abstract fun add(operation: AutoAdd, commit: Boolean, distribute: Boolean)

    abstract fun remove(operation: AutoRemove, commit: Boolean, distribute: Boolean)

    // --------------------------------------------------------------------------------
    // Helpers
    // --------------------------------------------------------------------------------

    abstract fun addItem(itemId: ItemId, parentItemId: ItemId?, value: AdatClass<*>)

    fun wireFormatNameOrNull(item: AdatClass<*>): String? {
        val itemWireFormatName = item.adatCompanion.wireFormatName
        return if (itemWireFormatName == defaultWireFormatName) null else itemWireFormatName
    }

    @CallSiteName
    fun closeListOp(operation: AutoOperation, itemIds: Set<ItemId>, commit: Boolean, distribute: Boolean, callSiteName: String = "") {
        if (context.time < operation.timestamp) {
            context.receive(operation.timestamp)
            trace(callSiteName) { "BE -> BE  itemIds=${itemIds} .. commit $commit .. distribute $distribute .. $operation" }
            close(operation, commit, distribute)
        } else {
            trace(callSiteName) { "BE -> BE  SKIP  $operation" }
        }
    }

    fun encode(wireFormatName: String?, values: Array<Any?>) =
        wireFormatFor(wireFormatName)
            .wireFormatEncode(context.wireFormatProvider.encoder(), values)
            .pack()

    fun decode(wireFormatName: String?, payload: ByteArray) =
        wireFormatFor(wireFormatName)
            .wireFormatDecode(context.wireFormatProvider.decoder(payload))
}