package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatContext
import `fun`.adaptive.adat.wireformat.AdatPropertyWireFormat
import `fun`.adaptive.auto.internal.connector.AutoConnector
import `fun`.adaptive.auto.internal.origin.AutoInstance
import `fun`.adaptive.auto.model.AutoPropertyValue
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.operation.AutoAdd
import `fun`.adaptive.auto.model.operation.AutoRemove
import `fun`.adaptive.auto.model.operation.AutoSyncEnd
import `fun`.adaptive.auto.model.operation.AutoUpdate
import `fun`.adaptive.utility.RequireLock
import `fun`.adaptive.wireformat.WireFormat

class PropertyBackend<IT : AdatClass>(
    instance: AutoInstance<*, *, *, IT>,
    val wireFormatName: String?,
    initialValues: Array<Any?>?,
    initialPropertyTimes: List<LamportTimestamp>?,
    @RequireLock
    override var itemId: ItemId,
) : AutoItemBackend<IT>(instance) {

    override val wireFormat = wireFormatFor(wireFormatName)

    private val properties: List<AdatPropertyWireFormat<*>> = wireFormat.propertyWireFormats

    private val values: Array<Any?> = initialValues?.copyOf() ?: arrayOfNulls(properties.size)

    private val propertyTimes = initPropertyTimes(initialPropertyTimes)

    var lastUpdate = propertyTimes.max()
        private set

    private var item: IT? = null

    init {
        trace { "itemId=$itemId  lastUpdate=$lastUpdate  values=${initialValues.contentToString()}" }
    }

    private fun initPropertyTimes(propertyTimes: List<LamportTimestamp>?): Array<LamportTimestamp> {

        if (propertyTimes == null) {
            if (instance.origin) {
                return Array(properties.size) { LamportTimestamp.ORIGIN }
            } else {
                return Array(properties.size) { LamportTimestamp.CONNECTING }
            }
        }

        if (propertyTimes.size == properties.size) {
            return Array(properties.size) { propertyTimes[it] }
        }

        // this happens when we extend the property list of the adat class, in that
        // case the property time array does not contain the times for the new
        // properties

        return Array(properties.size) {
            if (it < propertyTimes.size) {
                propertyTimes[it]
            } else {
                LamportTimestamp.CONNECTING
            }
        }
    }

    // --------------------------------------------------------------------------------
    // Local operations
    // --------------------------------------------------------------------------------

    override fun localAdd(timestamp: LamportTimestamp, item: IT): Pair<AutoAdd, IT> {
        throw UnsupportedOperationException("auto item does not support adding items ($this)")
    }

    override fun localUpdate(
        timestamp: LamportTimestamp,
        itemId: ItemId,
        updates: Collection<Pair<String, Any?>>,
    ): Pair<AutoUpdate, IT> {

        val parts = mutableListOf<AutoPropertyValue>()

        for ((propertyName, propertyValue) in updates) {
            val property = properties[propertyName]
            val index = property.index

            values[index] = propertyValue // must be BEFORE `encode`

            val payload = encode(property)
            val time = timestamp

            parts += AutoPropertyValue(time, propertyName, payload)

            propertyTimes[index] = timestamp
        }

        item = null
        return AutoUpdate(timestamp, itemId, parts) to getItem()
    }

    override fun localRemove(timestamp: LamportTimestamp, itemId: ItemId): Pair<AutoRemove, IT?> {
        throw UnsupportedOperationException("auto item does not support removing items ($this)")
    }

    // --------------------------------------------------------------------------------
    // Operations from peers
    // --------------------------------------------------------------------------------

    override fun remoteAdd(operation: AutoAdd): Pair<LamportTimestamp?, IT> {
        throw UnsupportedOperationException("auto item does not support adding items ($this)")
    }

    override fun remoteUpdate(operation: AutoUpdate): Triple<LamportTimestamp?, IT?, IT> {
        var changed = false

        val original = item

        for (change in operation.values) {

            val index = indexOf(change.propertyName)

            val lastChange = propertyTimes[index]

            if (lastChange < change.changeTime) {

                val property = properties[index]
                val decoder = instance.wireFormatProvider.decoder(change.payload)

                @Suppress("UNCHECKED_CAST")
                val wireformat = property.wireFormat as WireFormat<Any?>
                val value = if (property.metadata.isNullable) {
                    decoder.asInstanceOrNull(wireformat)
                } else {
                    decoder.asInstance(wireformat)
                }

                values[index] = value
                propertyTimes[index] = change.changeTime
                lastUpdate = maxOf(lastUpdate, change.changeTime)

                trace { "APPLIED  :: ${change.propertyName} = $value" }

                changed = true

            } else {
                trace { "SKIPPED  :: ${change.propertyName}" }
            }
        }

        if (changed) {
            item = null
            return Triple(lastUpdate, original, getItem())
        } else {
            return Triple(null, original !!, original)
        }
    }

    override fun remove(operation: AutoRemove): Pair<LamportTimestamp?, Set<Pair<ItemId, IT>>> {
        throw UnsupportedOperationException("auto item does not support removing items ($this)")
    }

    // --------------------------------------------------------------------------------
    // Peer synchronization
    // --------------------------------------------------------------------------------

    /**
     * Send any changes that happened after [syncFrom] to the peer.
     */
    override suspend fun syncPeer(
        connector: AutoConnector,
        syncFrom: LamportTimestamp,
        syncBatch: MutableList<AutoUpdate>?,
        sendSyncEnd: Boolean,
    ) {

        if (syncFrom >= lastUpdate) {
            trace { "SKIPPED  :: SYNC time=$lastUpdate peerTime=$syncFrom" }
            return
        } else {
            trace { "STARTED  :: itemId=$itemId time=$lastUpdate peerTime=$syncFrom" }
        }

        val changesToSend = mutableListOf<AutoPropertyValue>()

        for (index in propertyTimes.indices) {
            val change = propertyTimes[index]
            val property = properties[index]

            if (change <= syncFrom) continue
            changesToSend += AutoPropertyValue(change, property.name, encode(property))
        }

        val operation = AutoUpdate(instance.time, itemId, changesToSend)

        trace { "SENT     :: peerTime=$syncFrom op=$operation" }

        if (syncBatch != null) {
            syncBatch += operation
        } else {
            connector.send(operation)

            if (sendSyncEnd) {
                connector.send(AutoSyncEnd(instance.time))
            }
        }

        trace { "FINISHED :: itemId=$itemId time=$lastUpdate peerTime=$syncFrom" }
    }

    // --------------------------------------------------------------------------------
    // Utility
    // --------------------------------------------------------------------------------

    override fun getItem(itemId: ItemId): IT? {
        check(itemId == this.itemId) { "item id mismatch : $itemId != ${this.itemId}" }
        return getItem()
    }

    @Suppress("UNCHECKED_CAST")
    override fun getItem(): IT =
        item
            ?: (wireFormat.newInstance(values) as IT).also {
                it.adatContext = AdatContext(itemId, store = instance.store)
                item = it
            }

    private operator fun List<AdatPropertyWireFormat<*>>.get(name: String): AdatPropertyWireFormat<*> =
        first { it.metadata.name == name }

    private fun indexOf(name: String) =
        properties[name].metadata.index

    private fun encode(property: AdatPropertyWireFormat<*>): ByteArray {
        try {
            val index = property.index
            val value = values[index]

            @Suppress("UNCHECKED_CAST")
            val wireformat = property.wireFormat as WireFormat<Any?>
            val encoder = instance.wireFormatProvider.encoder()
            if (property.metadata.isNullable) {
                encoder.rawInstanceOrNull(value, wireformat)
            } else {
                encoder.rawInstance(value, wireformat)
            }
            return encoder.pack()
        } catch (e: Exception) {
            instance.error("error while writing property: ${property.metadata}", e)
            throw e
        }
    }

    override fun encode() =
        wireFormat
            .wireFormatEncode(instance.wireFormatProvider.encoder(), values)
            .pack()

}