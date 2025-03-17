package `fun`.adaptive.iot.curval

import `fun`.adaptive.iot.model.AioStatus
import `fun`.adaptive.iot.model.AioValueId
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
class CurValWorkerTest {

    @Test
    fun `should update values when newer timestamp is received`() = curValTest { worker ->
        val valueId = AioValueId()
        val oldValue = CurVal(valueId, Instant.parse("2023-01-01T12:00:00Z"), AioStatus.OK, "OldValue")
        val newValue = CurVal(valueId, Instant.parse("2023-01-01T12:01:00Z"), AioStatus.OK, "NewValue")

        worker.update(oldValue)
        worker.update(newValue)

        waitFor(1.seconds) { worker.updates.isEmpty }

        assertEquals(newValue, worker.values[valueId])
    }

    @Test
    fun `should not update values when older timestamp is received`() = curValTest { worker ->
        val valueId = AioValueId()
        val oldValue = CurVal(valueId, Instant.parse("2023-01-01T12:01:00Z"), AioStatus.OK, "OldValue")
        val newValue = CurVal(valueId, Instant.parse("2023-01-01T12:00:00Z"), AioStatus.OK, "NewValue")

        worker.update(oldValue)
        worker.update(newValue)

        waitFor(1.seconds) { worker.updates.isEmpty }

        assertEquals(oldValue, worker.values[valueId])
    }

    @Test
    fun `should subscribe and receive initial values`() = curValTest { worker ->
        val valueId = AioValueId()
        val initialValue = CurVal(valueId, Instant.parse("2023-01-01T12:00:00Z"), AioStatus.OK, "InitialValue")
        worker.update(initialValue)

        val channel = Channel<CurVal>(1)
        val subscription = CurValSubscription(valueId, channel)

        worker.subscribe(listOf(subscription))

        val receivedValue = channel.receive()
        assertEquals(initialValue, receivedValue)
    }

    @Test
    fun `should notify subscribers on update`() = curValTest { worker ->
        val valueId = AioValueId()
        val channel = Channel<CurVal>(1)
        val subscription = CurValSubscription(valueId, channel)

        worker.subscribe(listOf(subscription))

        val newValue = CurVal(valueId, Instant.parse("2023-01-01T12:01:00Z"), AioStatus.OK, "NewValue")
        worker.update(newValue)

        val receivedValue = channel.receive()
        assertEquals(newValue, receivedValue)
    }

    @Test
    fun `should allow multiple subscribers to receive updates`() = curValTest { worker ->
        val valueId = AioValueId()
        val channel1 = Channel<CurVal>(1)
        val channel2 = Channel<CurVal>(1)

        val subscription1 = CurValSubscription(valueId, channel1)
        val subscription2 = CurValSubscription(valueId, channel2)

        worker.subscribe(listOf(subscription1, subscription2))

        val newValue = CurVal(valueId, Instant.parse("2023-01-01T12:02:00Z"), AioStatus.OK, "MultiSubscriberValue")
        worker.update(newValue)

        assertEquals(newValue, channel1.receive())
        assertEquals(newValue, channel2.receive())
    }

    @Test
    fun `should unsubscribe properly`() = curValTest { worker ->
        val valueId = AioValueId()
        val channel = Channel<CurVal>(1)
        val subscription = CurValSubscription(valueId, channel)

        worker.subscribe(listOf(subscription))
        worker.unsubscribe(listOf(subscription))

        val newValue = CurVal(valueId, Instant.parse("2023-01-01T12:03:00Z"), AioStatus.OK, "AfterUnsubscribe")
        worker.update(newValue)

        waitFor(1.seconds) { worker.updates.isEmpty }

        assertTrue(channel.isEmpty)
    }

    fun curValTest(timeout: Duration = 10.seconds, testFun: suspend (worker: CurValWorker) -> Unit) =
        runTest(timeout = timeout) {
            val worker = CurValWorker()
            val dispatcher = Dispatchers.Default
            val scope = CoroutineScope(dispatcher)

            scope.launch {
                worker.run()
            }

            testFun(worker)

            worker.updates.close()
        }
}
