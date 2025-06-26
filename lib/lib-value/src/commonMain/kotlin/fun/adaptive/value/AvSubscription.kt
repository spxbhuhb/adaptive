package `fun`.adaptive.value

import `fun`.adaptive.value.operation.AvValueOperation
import `fun`.adaptive.value.operation.AvoAddOrUpdate
import `fun`.adaptive.value.operation.AvoMarkerRemove
import `fun`.adaptive.value.operation.AvoRemove
import `fun`.adaptive.value.operation.AvoTransaction
import `fun`.adaptive.value.store.AvValueStore

abstract class AvSubscription(
    val uuid: AvSubscriptionId,
    val conditions: List<AvSubscribeCondition>
) {

    var store: AvValueStore? = null

    val transaction = mutableListOf<AvValueOperation>()

    fun add(value: AvValue<*>) {
        transaction.add(AvoAddOrUpdate(value))
    }

    fun remove(valueId : AvValueId) {
        transaction.add(AvoRemove(valueId))
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