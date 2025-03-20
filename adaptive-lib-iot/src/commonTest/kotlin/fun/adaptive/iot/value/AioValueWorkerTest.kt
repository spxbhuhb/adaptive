package `fun`.adaptive.iot.value

import `fun`.adaptive.iot.item.AioItem
import `fun`.adaptive.iot.item.AioMarkerMap
import `fun`.adaptive.iot.item.AioStatus
import `fun`.adaptive.iot.item.AmvItemIdList
import `fun`.adaptive.iot.value.builtin.AvString
import `fun`.adaptive.iot.value.operation.AioValueOperation
import `fun`.adaptive.iot.value.operation.AvoAddOrUpdate
import `fun`.adaptive.iot.value.operation.AvoMarkerRemove
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.utility.waitFor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class AioValueWorkerTest {

    @Test
    fun `should update values when newer timestamp is received`() = test { worker ->
        val valueId = AioValueId()
        val oldValue = AvString(valueId, Instant.parse("2023-01-01T12:00:00Z"), AioStatus.OK, "OldValue")
        val newValue = AvString(valueId, Instant.parse("2023-01-01T12:01:00Z"), AioStatus.OK, "NewValue")

        worker.add(oldValue)
        worker.update(newValue)

        waitFor(1.seconds) { worker.isIdle }

        assertEquals(newValue, worker[valueId])
    }

    @Test
    fun `should not update values when older timestamp is received`() = test { worker ->
        val valueId = AioValueId()
        val oldValue = AvString(valueId, Instant.parse("2023-01-01T12:01:00Z"), AioStatus.OK, "OldValue")
        val newValue = AvString(valueId, Instant.parse("2023-01-01T12:00:00Z"), AioStatus.OK, "NewValue")

        worker.add(oldValue)
        worker.update(newValue)

        waitFor(1.seconds) { worker.isIdle }

        assertEquals(oldValue, worker[valueId])
    }

    @Test
    fun `should subscribe and receive initial values`() = test { worker ->
        val valueId = AioValueId()
        val initialValue = AvString(valueId, Instant.parse("2023-01-01T12:00:00Z"), AioStatus.OK, "InitialValue")
        worker.add(initialValue)

        val channel = Channel<AioValueOperation>(1)
        val subscription = AioValueChannelSubscription(uuid4(), condition(valueId), channel)

        worker.subscribe(listOf(subscription))

        val received = channel.receive()
        check(received is AvoAddOrUpdate)
        assertEquals(initialValue, received.value)
    }

    @Test
    fun `should notify subscribers on update`() = test { worker ->
        val valueId = AioValueId()
        val channel = Channel<AioValueOperation>(1)
        val subscription = AioValueChannelSubscription(uuid4(), condition(valueId), channel)

        worker.subscribe(listOf(subscription))

        val newValue = AvString(valueId, Instant.parse("2023-01-01T12:01:00Z"), AioStatus.OK, "NewValue")
        worker.add(newValue)

        val received = channel.receive()
        check(received is AvoAddOrUpdate)
        assertEquals(newValue, received.value)
    }

    @Test
    fun `should allow multiple subscribers to receive updates`() = test { worker ->
        val valueId = AioValueId()
        val channel1 = Channel<AioValueOperation>(1)
        val channel2 = Channel<AioValueOperation>(1)

        val subscription1 = AioValueChannelSubscription(uuid4(), condition(valueId), channel1)
        val subscription2 = AioValueChannelSubscription(uuid4(), condition(valueId), channel2)

        worker.subscribe(listOf(subscription1, subscription2))

        val newValue = AvString(valueId, Instant.parse("2023-01-01T12:02:00Z"), AioStatus.OK, "MultiSubscriberValue")
        worker.add(newValue)

        assertEquals(newValue, (channel1.receive() as AvoAddOrUpdate).value)
        assertEquals(newValue, (channel2.receive() as AvoAddOrUpdate).value)
    }

    @Test
    fun `should unsubscribe properly`() = test { worker ->
        val valueId = AioValueId()
        val channel = Channel<AioValueOperation>(1)
        val subscription = AioValueChannelSubscription(uuid4(), condition(valueId), channel)

        worker.subscribe(subscription)
        worker.unsubscribe(subscription.uuid)

        val newValue = AvString(valueId, Instant.parse("2023-01-01T12:03:00Z"), AioStatus.OK, "AfterUnsubscribe")
        worker.add(newValue)

        waitFor(1.seconds) { worker.isIdle }

        assertTrue(channel.isEmpty)
    }

    fun addSensor(worker: AioValueWorker, markers: AioMarkerMap): AioItem {
        val item = AioItem(
            name = "Temp Sensor",
            type = "sensor",
            uuid = uuid7(),
            timestamp = Instant.parse("2023-01-01T12:00:00Z"),
            status = AioStatus.OK,
            friendlyId = 1,
            markersOrNull = markers,
            parentId = null
        )

        worker.add(item) // Add the AioItem to the worker

        return item
    }

    @Test
    fun `should return values matching the marker query`() = test { worker ->
        val marker = "temperature"
        val item = addSensor(worker, mapOf(marker to uuid7()))

        val result = worker.queryByMarker(marker)

        assertEquals(listOf(item), result)
    }

    @Test
    fun `should not return values if the marker query does not match any values`() = test { worker ->
        val marker = "temperature"
        addSensor(worker, mapOf("humidity" to uuid7()))

        val result = worker.queryByMarker(marker)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `should return multiple values if multiple markers match`() = test { worker ->
        val marker = "temperature"
        val item1 = addSensor(worker, mapOf(marker to uuid7()))
        val item2 = addSensor(worker, mapOf(marker to uuid7()))

        val result = worker.queryByMarker(marker)

        assertEquals(setOf(item1, item2), result.toSet())
    }

    @Test
    fun `should notify marker-based subscriptions when a new marker is added`() = test { worker ->
        val marker = "temperature"
        val initialItem = addSensor(worker, mapOf(marker to uuid7()))

        val channel = Channel<AioValueOperation>(1)
        val subscription = AioValueChannelSubscription(uuid4(), condition(marker), channel)

        worker.subscribe(listOf(subscription))

        val newValue = initialItem.copyWith(marker, AmvItemIdList(initialItem.uuid, marker, emptyList()))

        worker.update(newValue)

        val received = channel.receive()
        check(received is AvoAddOrUpdate)
        assertEquals(newValue.uuid, received.value.uuid)
        assertNotEquals(newValue.markers, (received.value as AioItem).markers)
    }

    @Test
    fun `should notify marker-based subscriptions when a marker is removed`() = test { worker ->
        val marker = "temperature"

        val initialItem = addSensor(worker, mapOf(marker to uuid7()))
        val newValue = initialItem.copy(markersOrNull = null)

        val channel = Channel<AioValueOperation>(1)
        val subscription = AioValueChannelSubscription(uuid4(), condition(marker), channel)

        worker.subscribe(listOf(subscription))

        channel.receive() // drop the initial message which contains the initial value

        worker.update(newValue)

        val received = channel.receive()
        check(received is AvoMarkerRemove)
        assertEquals(received.valueId, newValue.uuid)
        assertEquals(received.marker, "temperature")
    }

    fun test(timeout: Duration = 10.seconds, testFun: suspend (worker: AioValueWorker) -> Unit) =
        runTest(timeout = timeout) {
            val worker = AioValueWorker()
            worker.logger = getLogger("worker")
            val dispatcher = Dispatchers.Unconfined
            val scope = CoroutineScope(dispatcher)

            scope.launch {
                worker.run()
            }

            testFun(worker)

            worker.close()
        }
}
