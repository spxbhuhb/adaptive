package `fun`.adaptive.iot.point.computed

import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.iot.point.PointMarkers
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.value.AvChannelSubscription
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.operation.AvValueOperation
import `fun`.adaptive.value.operation.AvoAdd
import `fun`.adaptive.value.operation.AvoAddOrUpdate
import `fun`.adaptive.value.operation.AvoUpdate
import kotlinx.coroutines.channels.Channel

class AioPointComputeWorker : WorkerImpl<AioPointComputeWorker> {

    val valueWorker by worker<AvValueWorker>()

    val channel = Channel<AvValueOperation>(Channel.UNLIMITED)

    override suspend fun run() {
        subscribe()

        for (operation in channel) {
            operation.forEach { process(it) }
        }
    }

    private fun subscribe() {
        val condition = AvSubscribeCondition(marker = PointMarkers.COMPUTED_POINT)
        val subscription = AvChannelSubscription(uuid4(), listOf(condition), channel)
        valueWorker.subscribe(subscription)
    }

    private fun process(operation : AvValueOperation) {
        when (operation) {
            is AvoAddOrUpdate -> TODO()
            is AvoUpdate -> TODO()
            is AvoAdd -> TODO()
            else -> Unit
        }
    }
}