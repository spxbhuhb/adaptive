package `fun`.adaptive.auto.backend

import `fun`.adaptive.adat.wireformat.AdatPropertyWireFormat
import `fun`.adaptive.auto.ItemId
import `fun`.adaptive.auto.LamportTimestamp
import `fun`.adaptive.auto.MetadataId
import `fun`.adaptive.auto.connector.AutoConnector
import `fun`.adaptive.auto.model.AutoPropertyValue
import `fun`.adaptive.auto.model.operation.AutoModify
import `fun`.adaptive.wireformat.WireFormat

class PropertyBackend(
    override val context: BackendContext,
    val itemId: LamportTimestamp,
    val metadataId: MetadataId?,
    initialValues: Array<Any?>?
) : BackendBase() {

    val properties: List<AdatPropertyWireFormat<*>> = context.defaultWireFormat.propertyWireFormats

    operator fun List<AdatPropertyWireFormat<*>>.get(name: String): AdatPropertyWireFormat<*> =
        first { it.property.name == name }

    fun indexOf(name: String) =
        properties[name].property.index

    val values: Array<Any?> = if (initialValues == null) {
        arrayOfNulls(properties.size)
    } else {
        check(initialValues.size == properties.size)
        Array(properties.size) { initialValues[it] }
    }

    class Change(
        val time: LamportTimestamp,
        val payload: ByteArray
    )

    val changes: Array<Change?> = arrayOfNulls(properties.size)

    // --------------------------------------------------------------------------------
    // Operations from the frontend
    // --------------------------------------------------------------------------------

    override fun modify(itemId: ItemId, propertyName: String, propertyValue: Any?) {
        val property = properties[propertyName]
        val index = property.index

        values[index] = propertyValue // must be BEFORE `encode`

        val payload = encode(property)

        val operation = AutoModify(context.nextTime(), itemId, listOf(AutoPropertyValue(propertyName, payload)))
        trace { "FE -> BE  $propertyName=$propertyValue .. commit true .. distribute true .. $operation" }

        changes[index] = Change(operation.timestamp, payload)

        close(operation, commit = true, distribute = true)
    }

    // --------------------------------------------------------------------------------
    // Operations from peers
    // --------------------------------------------------------------------------------

    override fun modify(operation: AutoModify, commit: Boolean, distribute: Boolean) {
        var changed = false

        for (change in operation.values) {

            val index = indexOf(change.propertyName)

            val lastChange = changes[index]

            if (lastChange == null || lastChange.time < operation.timestamp) {

                @Suppress("UNCHECKED_CAST")
                val wireformat = properties[index].wireFormat as WireFormat<Any?>
                val value = context.wireFormatProvider.decoder(change.payload).asInstance(wireformat)

                values[index] = value
                changes[index] = Change(operation.timestamp, change.payload)
                context.receive(operation.timestamp)

                trace { "BE -> BE  CHANGE ${change.propertyName}=$value" }

                changed = true

            } else {
                trace { "BE -> BE  SKIP   ${change.propertyName}" }
            }
        }

        if (changed) {
            trace { "BE -> BE  commit=$commit distribute=$distribute op=$operation" }
            close(operation, commit, distribute)
        } else {
            trace { "BE -> BE  SKIP   op=$operation" }
        }
    }

    // --------------------------------------------------------------------------------
    // Peer synchronization
    // --------------------------------------------------------------------------------

    /**
     * Send any changes that happened after [peerTime] to the peer.
     */
    override suspend fun syncPeer(connector: AutoConnector, peerTime: LamportTimestamp) {
        val time = context.time
        if (peerTime.timestamp >= time.timestamp) {
            trace { "SKIP SYNC: time= $time peerTime=$peerTime" }
            return
        }

        val changesToSend = mutableListOf<AutoPropertyValue>()

        for (index in changes.indices) {
            val change = changes[index]
            val property = properties[index]

            if (change != null) {
                if (change.time <= peerTime) continue
                changesToSend += AutoPropertyValue(property.name, change.payload)
            } else {
                changesToSend += AutoPropertyValue(property.name, encode(property))
            }
        }

        val operation = AutoModify(time, itemId, changesToSend)

        trace { "peerTime=$peerTime op=$operation" }

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
        return context.wireFormatProvider.encoder().rawInstance(value, wireformat).pack()
    }

    fun encode() =
        context.defaultWireFormat.wireFormatEncode(context.wireFormatProvider.encoder(), values).pack()

}