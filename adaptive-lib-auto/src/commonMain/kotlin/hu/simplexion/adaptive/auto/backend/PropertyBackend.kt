package hu.simplexion.adaptive.auto.backend

import hu.simplexion.adaptive.adat.wireformat.AdatPropertyWireFormat
import hu.simplexion.adaptive.auto.ItemId
import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.connector.AutoConnector
import hu.simplexion.adaptive.auto.model.operation.AutoModify
import hu.simplexion.adaptive.auto.model.operation.AutoTransaction
import hu.simplexion.adaptive.wireformat.WireFormat

class PropertyBackend(
    override val context: BackendContext,
    val itemId: LamportTimestamp,
    val properties: List<AdatPropertyWireFormat<*>>,
    initialValues: Array<Any?>? = null
) : BackendBase() {

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

    val operations: Array<AutoModify?> = arrayOfNulls(properties.size)

    // --------------------------------------------------------------------------------
    // Operations from the frontend
    // --------------------------------------------------------------------------------

    override fun modify(itemId: ItemId, propertyName: String, propertyValue: Any?) {
        val property = properties[propertyName]
        val index = property.index

        values[index] = propertyValue // must be BEFORE `encode`

        val payload = encode(property)

        val operation = AutoModify(context.nextTime(), itemId, propertyName, payload)
        trace { "FE -> BE  $propertyName=$propertyValue .. commit true .. distribute true .. $operation" }

        operations[index] = operation

        close(operation, commit = true, distribute = true)
    }

    // --------------------------------------------------------------------------------
    // Operations from peers
    // --------------------------------------------------------------------------------

    override fun modify(operation: AutoModify, commit: Boolean, distribute: Boolean) {
        context.wireFormatProvider.decoder(operation.propertyValue)
        val index = indexOf(operation.propertyName)

        val lastOperation = operations[index]

        if (lastOperation == null || lastOperation.timestamp < operation.timestamp) {

            @Suppress("UNCHECKED_CAST")
            val wireformat = properties[index].wireFormat as WireFormat<Any?>
            val value = context.wireFormatProvider.decoder(operation.propertyValue).asInstance(wireformat)

            values[index] = value
            operations[index] = operation
            context.receive(operation.timestamp)

            trace { "BE -> BE  ${operation.propertyName}=$value .. commit $commit .. distribute $distribute .. $operation" }

            close(operation, commit, distribute)

        } else {
            trace { "BE -> BE  SKIP  $operation" }
        }
    }

    override fun transaction(transaction: AutoTransaction, commit: Boolean, distribute: Boolean) {
        trace { "commit=$commit distribute=$distribute op=$transaction" }

        for (operation in transaction.modifications ?: emptyList()) {
            operation.apply(this, commit = false, distribute = false)
        }

        close(transaction, commit, distribute)
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

        val modifications = mutableListOf<AutoModify>()

        for (index in operations.indices) {
            val operation = operations[index]
            if (operation != null) {
                if (operation.timestamp <= peerTime) continue
                modifications += operation
            } else {
                val property = properties[index]
                modifications += AutoModify(time, itemId, property.name, encode(property))
            }
        }

        val transaction = AutoTransaction(time, modifications = modifications)

        trace { "peerTime=$peerTime op=$transaction" }

        connector.send(transaction)
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

}