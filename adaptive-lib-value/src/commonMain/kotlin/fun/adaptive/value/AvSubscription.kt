package `fun`.adaptive.value

import `fun`.adaptive.value.item.AvMarker
import `fun`.adaptive.value.operation.AvValueOperation
import `fun`.adaptive.value.operation.AvoAddOrUpdate
import `fun`.adaptive.value.operation.AvoMarkerRemove
import `fun`.adaptive.value.operation.AvoTransaction

abstract class AvSubscription(
    val uuid: AvValueSubscriptionId,
    val conditions: List<AvSubscribeCondition>
) {

    var worker: AvValueWorker? = null

    val transaction = mutableListOf<AvValueOperation>()

    fun add(value: AvValue) {
        transaction.add(AvoAddOrUpdate(value))
    }

    fun markerRemove(valueId: AvValueId, marker: AvMarker) {
        transaction.add(AvoMarkerRemove(valueId, marker))
    }

    fun commit() {
        if (transaction.isEmpty()) return

        val operation = if (transaction.size == 1) {
            (transaction.first())
        } else {
            AvoTransaction(transaction.toList())
        }

        send(operation)

        transaction.clear()
    }

    abstract fun send(operation: AvValueOperation)

}