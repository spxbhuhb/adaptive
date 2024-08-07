package hu.simplexion.adaptive.auto.backend

import hu.simplexion.adaptive.adat.wireformat.AdatPropertyWireFormat
import hu.simplexion.adaptive.auto.ItemId
import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.connector.AutoConnector
import hu.simplexion.adaptive.auto.frontend.AbstractFrontend
import hu.simplexion.adaptive.auto.model.operation.AutoModify
import hu.simplexion.adaptive.auto.model.operation.AutoOperation
import hu.simplexion.adaptive.auto.model.operation.AutoTransaction
import hu.simplexion.adaptive.wireformat.WireFormat

class PropertyBackend(
    override val context: BackendContext,
    val itemId: LamportTimestamp,
    val properties: List<AdatPropertyWireFormat<*>>,
    initialValues: Array<Any?>? = null
) : AbstractBackend() {

    var frontEnd: AbstractFrontend? = null

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
    // Incoming from other backends
    // --------------------------------------------------------------------------------

    override fun send(operation: AutoOperation) {
        receive(operation, false)
    }

    override fun syncEnd(peerTime: LamportTimestamp) {
        context.time = context.time.receive(peerTime)
    }

    override fun receive(operation: AutoOperation, distribute: Boolean) {
        trace { "distribute=$distribute op=$operation" }

        operation.apply(this, commit = true, distribute)

        context.receive(operation.timestamp)
    }

    override fun transaction(transaction: AutoTransaction, commit: Boolean, distribute: Boolean) {
        trace { "commit=$commit distribute=$distribute op=$transaction" }

        for (operation in transaction.modify) {
            operation.apply(this, commit = false, distribute = false)
        }

        close(transaction, commit, distribute)
    }

    override fun modify(operation: AutoModify, commit: Boolean, distribute: Boolean) {
        trace { "commit=$commit distribute=$distribute op=$operation" }

        context.wireFormatProvider.decoder(operation.propertyValue)
        val index = indexOf(operation.propertyName)

        val lastOperation = operations[index]

        if (lastOperation == null || lastOperation.timestamp < operation.timestamp) {

            @Suppress("UNCHECKED_CAST")
            val wireformat = properties[index].wireFormat as WireFormat<Any?>
            val value = context.wireFormatProvider.decoder(operation.propertyValue).asInstance(wireformat)

            values[index] = value
            operations[index] = operation

            close(operation, commit, distribute)

        }
    }

    // --------------------------------------------------------------------------------
    // Changes from the frontend
    // --------------------------------------------------------------------------------

    fun modify(item: ItemId, propertyName: String, propertyValue: Any?) {
        val property = properties[propertyName]
        val index = property.index

        values[index] = propertyValue // must be BEFORE `encode`

        val payload = encode(property)

        val operation = AutoModify(context.nextTime(), item, propertyName, payload)
        trace { "commit=true distribute=true op=$operation" }

        operations[index] = operation

        close(operation, commit = true, distribute = true)
    }

    // --------------------------------------------------------------------------------
    // Peer synchronization
    // --------------------------------------------------------------------------------

    /**
     * Send any changes that happened after [peerTime] to the peer.
     * TODO check if `peerTime.timestamp` is 0 and is  and if so, send the whole instance at once
     */
    override suspend fun syncPeer(connector: AutoConnector, peerTime: LamportTimestamp) {
        val time = context.time
        if (peerTime.timestamp >= time.timestamp) {
            trace { "SKIP SYNC: time= $time peerTime=$peerTime" }
            return
        }

        val update = mutableListOf<AutoModify>()

        for (index in operations.indices) {
            val operation = operations[index]
            if (operation != null) {
                update += operation
            } else {
                val property = properties[index]
                update += AutoModify(time, itemId, property.name, encode(property))
            }

        }

        val transaction = AutoTransaction(time, update)

        trace { "peerTime=$peerTime op=$transaction" }

        connector.send(transaction)
    }

    fun close(operation: AutoOperation, commit: Boolean, distribute: Boolean) {

        if (commit) {
            frontEnd?.commit()
            trace { "==== commit ====\n" }
        }

        if (distribute) {
            for (connector in connectors) {
                connector.send(operation)
            }
        }

    }

    fun encode(property: AdatPropertyWireFormat<*>): ByteArray {
        val index = property.index
        val value = values[index]

        @Suppress("UNCHECKED_CAST")
        val wireformat = property.wireFormat as WireFormat<Any?>
        return context.wireFormatProvider.encoder().rawInstance(value, wireformat).pack()
    }

}