package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.toArray
import `fun`.adaptive.auto.internal.frontend.AutoCollectionFrontend
import `fun`.adaptive.auto.internal.origin.AutoInstance
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.operation.AutoAdd
import `fun`.adaptive.auto.model.operation.AutoEmpty
import `fun`.adaptive.auto.model.operation.AutoModify
import `fun`.adaptive.auto.model.operation.AutoRemove
import `fun`.adaptive.auto.model.operation.AutoSyncEnd

abstract class AutoCollectionBackend<IT : AdatClass>(
    instance: AutoInstance<AutoCollectionBackend<IT>, AutoCollectionFrontend<IT>, Collection<IT>, IT>
) : AutoBackend<IT>(instance) {

    /**
     * Contains modification that should be applied when [AutoSyncEnd] arrives.
     */
    val afterSync = mutableListOf<AutoModify>()

    // --------------------------------------------------------------------------------
    // Operations from the frontend
    // --------------------------------------------------------------------------------

    fun add(item: IT, parentItemId: ItemId?) {
        val time = instance.nextTime()
        val itemId = time.asItemId()

        addItem(itemId, parentItemId, item, false)

        val wireFormatName = wireFormatNameOrNull(item)

        val operation = AutoAdd(
            timestamp = time,
            itemId = itemId,
            wireFormatName,
            parentItemId,
            encode(wireFormatName, item.toArray()) // FIXME clean up the array/adat class confusion
        )

        trace { "FE -> BE  itemId=$itemId .. commit true .. $operation" }

        instance.commit(null, initial = false, fromPeer = false)

        distribute(operation)
    }

    abstract fun remove(itemId: ItemId, commit: Boolean)

    abstract fun removeAll(itemIds: Set<ItemId>, commit: Boolean)

    // --------------------------------------------------------------------------------
    // Operations from peers
    // --------------------------------------------------------------------------------

    abstract fun empty(operation: AutoEmpty, commit: Boolean)

    abstract fun add(operation: AutoAdd, commit: Boolean)

    abstract fun remove(operation: AutoRemove, commit: Boolean)

    // --------------------------------------------------------------------------------
    // Helpers
    // --------------------------------------------------------------------------------

    abstract fun addItem(itemId: ItemId, parentItemId: ItemId?, value: IT, fromBackend: Boolean)

    fun wireFormatNameOrNull(item: AdatClass): String? {
        val itemWireFormatName = item.adatCompanion.wireFormatName
        // TODO I'm not sure about using null here, might cause problems during runtime if the configurations mismatch
        return if (itemWireFormatName == instance.defaultWireFormat?.wireFormatName) null else itemWireFormatName
    }

    fun encode(wireFormatName: String?, values: Array<Any?>) =
        wireFormatFor(wireFormatName)
            .wireFormatEncode(instance.wireFormatProvider.encoder(), values)
            .pack()

    fun decode(wireFormatName: String?, payload: ByteArray) =
        wireFormatFor(wireFormatName)
            .wireFormatDecode(instance.wireFormatProvider.decoder(payload))

}