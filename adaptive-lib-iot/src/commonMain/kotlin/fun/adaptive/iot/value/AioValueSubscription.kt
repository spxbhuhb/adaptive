package `fun`.adaptive.iot.value

import `fun`.adaptive.iot.item.AioMarker
import `fun`.adaptive.iot.value.operation.AioValueOperation
import `fun`.adaptive.iot.value.operation.AvoAddOrUpdate
import `fun`.adaptive.iot.value.operation.AvoMarkerRemove
import `fun`.adaptive.iot.value.operation.AvoTransaction

abstract class AioValueSubscription(
    val uuid: AuiValueSubscriptionId,
    val valueIds: List<AioValueId>,
    val markers: List<AioMarker>
) {

    var worker: AioValueWorker? = null

    val transaction = mutableListOf<AioValueOperation>()

    fun add(value: AioValue) {
        transaction.add(AvoAddOrUpdate(value))
    }

    fun markerRemove(valueId: AioValueId, marker: AioMarker) {
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

    abstract fun send(operation: AioValueOperation)

}