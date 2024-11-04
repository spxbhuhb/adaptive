package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.toArray
import `fun`.adaptive.adat.wireformat.AdatPropertyWireFormat
import `fun`.adaptive.auto.internal.connector.AutoConnector
import `fun`.adaptive.auto.internal.origin.AutoInstance
import `fun`.adaptive.auto.model.AutoPropertyValue
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.operation.AutoModify
import `fun`.adaptive.auto.model.operation.AutoSyncEnd
import `fun`.adaptive.wireformat.WireFormat

class PropertyBackend<IT : AdatClass>(
    instance: AutoInstance<*, *, *, IT>,
    val wireFormatName: String?,
    initialValues: Array<Any?>?,
    propertyTimes: List<LamportTimestamp>? = null,
    override val itemId: ItemId = ItemId.CONNECTING,
) : AutoItemBackend<IT>(instance) {

    val wireFormat = wireFormatFor(wireFormatName)

    val properties: List<AdatPropertyWireFormat<*>> = wireFormat.propertyWireFormats

    operator fun List<AdatPropertyWireFormat<*>>.get(name: String): AdatPropertyWireFormat<*> =
        first { it.metadata.name == name }

    fun indexOf(name: String) =
        properties[name].metadata.index

    val values: Array<Any?> = initialValues?.copyOf() ?: arrayOfNulls(properties.size)

    val lastUpdate = propertyTimes?.max() ?: LamportTimestamp.CONNECTING

    val propertyTimes = propertyTimes?.toMutableList()?.also {
        // this happens when we extend the property list of the adat class
        while (it.size < values.size) {
            it += LamportTimestamp.CONNECTING
        }
    } ?: MutableList(properties.size) { LamportTimestamp.CONNECTING }

    init {
        trace { "INIT  itemId=$itemId  ${initialValues.contentToString()}" }
    }

    // --------------------------------------------------------------------------------
    // Operations from the frontend
    // --------------------------------------------------------------------------------

    override fun update(itemId: ItemId, propertyName: String, propertyValue: Any?) {
        val property = properties[propertyName]
        val index = property.index

        values[index] = propertyValue // must be BEFORE `encode`

        val payload = encode(property)

        val time = instance.nextTime()

        val operation = AutoModify(time, itemId, listOf(AutoPropertyValue(time, propertyName, payload)))

        propertyTimes[index] = operation.timestamp

        instance.commit(this, initial = false, fromPeer = false)

        trace { "FE -> BE  $propertyName=$propertyValue $operation" }

        distribute(operation)
    }

    override fun update(itemId: ItemId, new: IT) {
        val newValues = new.toArray()
        check(newValues.size <= values.size)

        val time = instance.nextTime()
        val changes = mutableListOf<AutoPropertyValue>()

        for ((index, propertyValue) in values.withIndex()) {
            val newValue = newValues[index]
            if (newValue == propertyValue) continue

            val property = properties[index]
            values[index] = newValue
            propertyTimes[index] = time
            changes += AutoPropertyValue(time, property.name, encode(property))
        }

        // TODO check if nextTime is necessary here or we can use `time`
        val operation = AutoModify(instance.nextTime(), itemId, changes)

        instance.commit(this, initial = false, fromPeer = false)

        trace { "FE -> BE  $operation" }

        distribute(operation)
    }

    // --------------------------------------------------------------------------------
    // Operations from peers
    // --------------------------------------------------------------------------------

    override fun update(operation: AutoModify, commit: Boolean) {
        var changed = false

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
                instance.receive(change.changeTime)

                trace { "BE -> BE  CHANGE ${change.propertyName}=$value" }

                changed = true

            } else {
                trace { "BE -> BE  SKIP   ${change.propertyName}" }
            }
        }

        if (changed) {
            trace { "BE -> BE  op=$operation" }
            instance.commit(this, initial = false, fromPeer = true)
            distribute(operation)
        } else {
            trace { "BE -> BE  SKIP   op=$operation" }
        }
    }

    override fun syncEnd(operation: AutoSyncEnd, commit: Boolean) {
        instance.receive(operation.timestamp)
        trace { "time=${instance.time}" }
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
        syncBatch: MutableList<AutoModify>?,
        sendSyncEnd: Boolean,
    ) {

        if (syncFrom >= lastUpdate) {
            trace { "SYNC SKIP: time= $lastUpdate peerTime=$syncFrom" }
            return
        } else {
            trace { "SYNC: itemId=$itemId time= $lastUpdate peerTime=$syncFrom" }
        }

        val changesToSend = mutableListOf<AutoPropertyValue>()

        for (index in propertyTimes.indices) {
            val change = propertyTimes[index]
            val property = properties[index]

            if (change <= syncFrom) continue
            changesToSend += AutoPropertyValue(change, property.name, encode(property))
        }

        val operation = AutoModify(instance.time, itemId, changesToSend)

        trace { "peerTime=$syncFrom op=$operation" }

        if (syncBatch != null) {
            syncBatch += operation
        } else {
            connector.send(operation)

            if (sendSyncEnd) {
                connector.send(AutoSyncEnd(instance.time))
            }
        }
    }

    // --------------------------------------------------------------------------------
    // Utility
    // --------------------------------------------------------------------------------

    override fun getItem(): IT {
        @Suppress("UNCHECKED_CAST")
        return wireFormat.newInstance(values) as IT
    }

    fun encode(property: AdatPropertyWireFormat<*>): ByteArray {
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
        } catch (e: Throwable) {
            instance.error("error while writing property: ${property.metadata}")
            throw e
        }
    }

}