package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.wireformat.AdatPropertyWireFormat
import `fun`.adaptive.auto.internal.connector.AutoConnector
import `fun`.adaptive.auto.model.AutoPropertyValue
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.operation.AutoModify
import `fun`.adaptive.wireformat.WireFormat

class PropertyBackend<A : AdatClass>(
    override val context: BackendContext<A>,
    val itemId: LamportTimestamp,
    val wireFormatName: String?,
    initialValues: Array<Any?>,
    propertyTimes : List<LamportTimestamp> = MutableList(initialValues.size) { itemId }
) : BackendBase(context.handle) {

    val wireFormat = wireFormatFor(wireFormatName)

    val properties: List<AdatPropertyWireFormat<*>> = wireFormat.propertyWireFormats

    operator fun List<AdatPropertyWireFormat<*>>.get(name: String): AdatPropertyWireFormat<*> =
        first { it.property.name == name }

    fun indexOf(name: String) =
        properties[name].property.index

    val values: Array<Any?> = initialValues.copyOf()

    val lastUpdate = propertyTimes.max()

    val propertyTimes = propertyTimes.toMutableList()

    init {
        trace { "INIT  itemId=$itemId  ${initialValues.contentToString()}"}
    }

    // --------------------------------------------------------------------------------
    // Operations from the frontend
    // --------------------------------------------------------------------------------

    override fun modify(itemId: ItemId, propertyName: String, propertyValue: Any?) {
        val property = properties[propertyName]
        val index = property.index

        values[index] = propertyValue // must be BEFORE `encode`

        val payload = encode(property)

        val operation = AutoModify(context.nextTime(), itemId, listOf(AutoPropertyValue(propertyName, payload)))
        trace { "FE -> BE  $propertyName=$propertyValue .. commit true $operation" }

        propertyTimes[index] = operation.timestamp

        close(operation, commit = true)
    }

    // --------------------------------------------------------------------------------
    // Operations from peers
    // --------------------------------------------------------------------------------

    override fun modify(operation: AutoModify, commit: Boolean) {
        var changed = false

        for (change in operation.values) {

            val index = indexOf(change.propertyName)

            val lastChange = propertyTimes[index]

            if (lastChange < operation.timestamp) {

                @Suppress("UNCHECKED_CAST")
                val wireformat = properties[index].wireFormat as WireFormat<Any?>
                val value = context.wireFormatProvider.decoder(change.payload).asInstance(wireformat)

                values[index] = value
                propertyTimes[index] = operation.timestamp
                context.receive(operation.timestamp)

                trace { "BE -> BE  CHANGE ${change.propertyName}=$value" }

                changed = true

            } else {
                trace { "BE -> BE  SKIP   ${change.propertyName}" }
            }
        }

        if (changed) {
            trace { "BE -> BE  commit=$commit .. op=$operation" }
            close(operation, commit)
        } else {
            trace { "BE -> BE  SKIP   op=$operation" }
        }
    }

    // --------------------------------------------------------------------------------
    // Peer synchronization
    // --------------------------------------------------------------------------------

    /**
     * Send any changes that happened after [syncFrom] to the peer.
     */
    override suspend fun syncPeer(connector: AutoConnector, syncFrom: LamportTimestamp) {

        if (syncFrom >= lastUpdate) {
            trace { "SKIP SYNC: time= $lastUpdate peerTime=$syncFrom" }
            return
        } else {
            trace { "SYNC: itemId=$itemId time= $lastUpdate peerTime=$syncFrom" }
        }

        val changesToSend = mutableListOf<AutoPropertyValue>()

        for (index in propertyTimes.indices) {
            val change = propertyTimes[index]
            val property = properties[index]

            if (change <= syncFrom) continue
            changesToSend += AutoPropertyValue(property.name, encode(property))
        }

        val operation = AutoModify(context.time, itemId, changesToSend)

        trace { "peerTime=$syncFrom op=$operation" }

        connector.send(operation)
    }

    // --------------------------------------------------------------------------------
    // Utility, common
    // --------------------------------------------------------------------------------

    fun encode(property: AdatPropertyWireFormat<*>): ByteArray {
        val index = property.index
        val value = values[index]

        @Suppress("UNCHECKED_CAST")
        val wireformat = property.wireFormat as WireFormat<Any?>
        val encoder = context.wireFormatProvider.encoder()
        if (property.nullable) {
            encoder.rawInstanceOrNull(value, wireformat)
        } else {
            encoder.rawInstance(value, wireformat)
        }
        return encoder.pack()
    }

}