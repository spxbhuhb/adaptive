package hu.simplexion.adaptive.auto.backend

import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.adat.AdatCompanion
import hu.simplexion.adaptive.auto.ItemId
import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.connector.AutoConnector
import hu.simplexion.adaptive.auto.operation.AutoModify
import hu.simplexion.adaptive.auto.operation.AutoOperation
import hu.simplexion.adaptive.auto.operation.AutoTransaction
import hu.simplexion.adaptive.utility.UUID
import kotlinx.coroutines.CoroutineScope

class AutoInstance<A : AdatClass<A>>(
    override val globalId: UUID<AutoBackend>,
    override val scope: CoroutineScope,
    time: LamportTimestamp,
    val companion: AdatCompanion<A>,
    initialValue: A? = null,
    val onChange: ((newValue: A) -> Unit)? = null
) : AutoBackend(time) {

    var value: A? = initialValue

    val metadata = companion.adatMetadata

    val operations: Array<AutoModify?> = if (initialValue == null) {
        arrayOfNulls(metadata.properties.size)
    } else {
        Array(metadata.properties.size) {
            AutoModify(time, time, metadata.properties[it].name, initialValue.getValue(it))
        }
    }

    // --------------------------------------------------------------------------------
    // Incoming from other backends
    // --------------------------------------------------------------------------------

    override fun receive(operation: AutoOperation, distribute: Boolean) {
        if (trace) trace("distribute=$distribute op=$operation")

        operation.apply(this, commit = true, distribute)

        time = time.receive(operation.timestamp)
    }

    override fun transaction(transaction: AutoTransaction, commit: Boolean, distribute: Boolean) {
        if (trace) trace("commit=$commit distribute=$distribute op=$transaction")

        for (operation in transaction.operations) {
            operation.apply(this, commit = false, distribute = false)
        }

        close(transaction, commit, distribute)
    }

    override fun modify(operation: AutoModify, commit: Boolean, distribute: Boolean) {
        if (trace) trace("commit=$commit distribute=$distribute op=$operation")

        check(value != null || ! commit) { "cannot modify auto instance before synchronization with origin is complete" }

        val propertyIndex = metadata[operation.propertyName].index
        val lastOperation = operations[propertyIndex]

        if (lastOperation == null || lastOperation.timestamp < operation.timestamp) {
            operations[propertyIndex] = operation
        }

        close(operation, commit, distribute)
    }

    // --------------------------------------------------------------------------------
    // Changes from the frontend
    // --------------------------------------------------------------------------------

    fun modify(item: ItemId, propertyName: String, propertyValue: Any?) {
        modify(
            AutoModify(nextTime(), item, propertyName, propertyValue),
            commit = true,
            distribute = true
        )
    }

    // --------------------------------------------------------------------------------
    // Peer synchronization
    // --------------------------------------------------------------------------------

    /**
     * Send any changes that happened after [peerTime] to the peer.
     * TODO check if `peerTime.timestamp` is 0 and is  and if so, send the whole instance at once
     */
    override fun syncPeer(connector: AutoConnector, peerTime: LamportTimestamp) {
        if (peerTime.timestamp >= time.timestamp) return

        val update = mutableListOf<AutoOperation>()

        for (operation in operations) {
            if (operation == null) continue
            if (peerTime >= operation.timestamp) continue
            update += operation
        }

        val transaction = AutoTransaction(time, update)

        if (trace) trace("peerTime=$peerTime op=$transaction")

        connector.send(
            transaction,
            distribute = false
        )
    }

    fun close(operation: AutoOperation, commit: Boolean, distribute: Boolean) {

        if (commit) {
            if (trace) trace("==== commit ====\n")

            val newValue = companion.newInstance(operations.map { it?.propertyValue }.toTypedArray())
            value = newValue
            onChange?.invoke(newValue)
        }

        if (distribute) {
            for (connector in connectors) {
                connector.send(
                    operation,
                    distribute = false
                )
            }
        }

    }

}