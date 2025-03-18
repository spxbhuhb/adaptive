package `fun`.adaptive.iot.value

import `fun`.adaptive.iot.item.AioStatus
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.utility.waitFor
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class AioValueWorkerTest {

    @Test
    fun `should update values when newer timestamp is received`() = test { worker ->
        val valueId = AioValueId()
        val oldValue = AioValue(valueId, Instant.parse("2023-01-01T12:00:00Z"), AioStatus.OK, "OldValue")
        val newValue = AioValue(valueId, Instant.parse("2023-01-01T12:01:00Z"), AioStatus.OK, "NewValue")

        worker.update(oldValue)
        worker.update(newValue)

        waitFor(1.seconds) { worker.isIdle }

        assertEquals(newValue, worker[valueId])
    }

    @Test
    fun `should not update values when older timestamp is received`() = test { worker ->
        val valueId = AioValueId()
        val oldValue = AioValue(valueId, Instant.parse("2023-01-01T12:01:00Z"), AioStatus.OK, "OldValue")
        val newValue = AioValue(valueId, Instant.parse("2023-01-01T12:00:00Z"), AioStatus.OK, "NewValue")

        worker.update(oldValue)
        worker.update(newValue)

        waitFor(1.seconds) { worker.isIdle }

        assertEquals(oldValue, worker[valueId])
    }

    @Test
    fun `should subscribe and receive initial values`() = test { worker ->
        val valueId = AioValueId()
        val initialValue = AioValue(valueId, Instant.parse("2023-01-01T12:00:00Z"), AioStatus.OK, "InitialValue")
        worker.update(initialValue)

        val channel = Channel<AioValue>(1)
        val subscription = AioValueChannelSubscription(uuid4(), listOf(valueId), channel)

        worker.subscribe(listOf(subscription))

        val receivedValue = channel.receive()
        assertEquals(initialValue, receivedValue)
    }

    @Test
    fun `should notify subscribers on update`() = test { worker ->
        val valueId = AioValueId()
        val channel = Channel<AioValue>(1)
        val subscription = AioValueChannelSubscription(uuid4(), listOf(valueId), channel)

        worker.subscribe(listOf(subscription))

        val newValue = AioValue(valueId, Instant.parse("2023-01-01T12:01:00Z"), AioStatus.OK, "NewValue")
        worker.update(newValue)

        val receivedValue = channel.receive()
        assertEquals(newValue, receivedValue)
    }

    @Test
    fun `should allow multiple subscribers to receive updates`() = test { worker ->
        val valueId = AioValueId()
        val channel1 = Channel<AioValue>(1)
        val channel2 = Channel<AioValue>(1)

        val subscription1 = AioValueChannelSubscription(uuid4(), listOf(valueId), channel1)
        val subscription2 = AioValueChannelSubscription(uuid4(), listOf(valueId), channel2)

        worker.subscribe(listOf(subscription1, subscription2))

        val newValue = AioValue(valueId, Instant.parse("2023-01-01T12:02:00Z"), AioStatus.OK, "MultiSubscriberValue")
        worker.update(newValue)

        assertEquals(newValue, channel1.receive())
        assertEquals(newValue, channel2.receive())
    }

    @Test
    fun `should unsubscribe properly`() = test { worker ->
        val valueId = AioValueId()
        val channel = Channel<AioValue>(1)
        val subscription = AioValueChannelSubscription(uuid4(), listOf(valueId), channel)

        worker.subscribe(subscription)
        worker.unsubscribe(subscription.uuid)

        val newValue = AioValue(valueId, Instant.parse("2023-01-01T12:03:00Z"), AioStatus.OK, "AfterUnsubscribe")
        worker.update(newValue)

        waitFor(1.seconds) { worker.isIdle }

        assertTrue(channel.isEmpty)
    }

    fun test(timeout: Duration = 10.seconds, testFun: suspend (worker: AioValueWorker) -> Unit) =
        runTest(timeout = timeout) {
            val worker = AioValueWorker()
            val dispatcher = Dispatchers.Default
            val scope = CoroutineScope(dispatcher)

            scope.launch {
                worker.run()
            }

            testFun(worker)

            worker.close()
        }
}
