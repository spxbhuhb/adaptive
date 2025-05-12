package `fun`.adaptive.value

import `fun`.adaptive.log.getLogger
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.utility.waitFor
import `fun`.adaptive.value.operation.AvValueOperation
import `fun`.adaptive.value.operation.AvoAddOrUpdate
import `fun`.adaptive.value.operation.AvoMarkerRemove
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class AvValueWorkerTest {

    @Test
    @JsName("shouldUpdateValuesWithNewerTimestamp")
    fun `should update values when newer timestamp is received`() = test { worker ->
        val valueId = AvValueId()
        val oldValue = AvValue(valueId, Instant.parse("2023-01-01T12:00:00Z"), spec = "OldValue")
        val newValue = AvValue(valueId, revision = 2L, lastChange = Instant.parse("2023-01-01T12:01:00Z"), spec = "NewValue")

        worker.queueAdd(oldValue)
        worker.queueUpdate(newValue)

        waitFor(1.seconds) { worker.isIdle }

        assertEquals(newValue, worker.get(valueId))
    }

    @Test
    @JsName("shouldNotUpdateValuesWithOlderTimestamp")
    fun `should not update values when older timestamp is received`() = test { worker ->
        val valueId = AvValueId()
        val oldValue = AvValue(valueId, Instant.parse("2023-01-01T12:01:00Z"), spec = "OldValue")
        val newValue = AvValue(valueId,  Instant.parse("2023-01-01T12:00:00Z"), spec = "NewValue")

        worker.queueAdd(oldValue)
        worker.queueUpdate(newValue)

        waitFor(1.seconds) { worker.isIdle }

        assertEquals(oldValue, worker.get(valueId))
    }

    @Test
    @JsName("shouldSubscribeAndReceiveInitialValues")
    fun `should subscribe and receive initial values`() = test { worker ->
        val valueId = AvValueId()
        val initialValue = AvValue(valueId, Instant.parse("2023-01-01T12:00:00Z"), spec = "InitialValue")
        worker.queueAdd(initialValue)

        val channel = Channel<AvValueOperation>(1)
        val subscription = AvChannelSubscription(uuid4(), condition(valueId), channel)

        worker.subscribe(listOf(subscription))

        val received = channel.receive()
        check(received is AvoAddOrUpdate)
        assertEquals(initialValue, received.value)
    }

    @Test
    @JsName("shouldNotifySubscribersOnUpdate")
    fun `should notify subscribers on update`() = test { worker ->
        val valueId = AvValueId()
        val channel = Channel<AvValueOperation>(1)
        val subscription = AvChannelSubscription(uuid4(), condition(valueId), channel)

        worker.subscribe(listOf(subscription))

        val newValue = AvValue(valueId, Instant.parse("2023-01-01T12:01:00Z"), spec = "NewValue")
        worker.queueAdd(newValue)

        val received = channel.receive()
        check(received is AvoAddOrUpdate)
        assertEquals(newValue, received.value)
    }

    @Test
    @JsName("shouldAllowMultipleSubscribersToReceiveUpdates")
    fun `should allow multiple subscribers to receive updates`() = test { worker ->
        val valueId = AvValueId()
        val channel1 = Channel<AvValueOperation>(1)
        val channel2 = Channel<AvValueOperation>(1)

        val subscription1 = AvChannelSubscription(uuid4(), condition(valueId), channel1)
        val subscription2 = AvChannelSubscription(uuid4(), condition(valueId), channel2)

        worker.subscribe(listOf(subscription1, subscription2))

        val newValue = AvValue(valueId, Instant.parse("2023-01-01T12:02:00Z"), spec = "MultiSubscriberValue")
        worker.queueAdd(newValue)

        assertEquals(newValue, (channel1.receive() as AvoAddOrUpdate).value)
        assertEquals(newValue, (channel2.receive() as AvoAddOrUpdate).value)
    }

    @Test
    @JsName("shouldUnsubscribeProperly")
    fun `should unsubscribe properly`() = test { worker ->
        val valueId = AvValueId()
        val channel = Channel<AvValueOperation>(1)
        val subscription = AvChannelSubscription(uuid4(), condition(valueId), channel)

        worker.subscribe(subscription)
        worker.unsubscribe(subscription.uuid)

        val newValue = AvValue(valueId, Instant.parse("2023-01-01T12:03:00Z"), spec = "AfterUnsubscribe")
        worker.queueAdd(newValue)

        waitFor(1.seconds) { worker.isIdle }

        assertTrue(channel.isEmpty)
    }

    fun addSensor(worker: AvValueWorker, markers: Set<AvMarker>? = null, refs: Map<AvRefLabel, AvValueId>? = null): AvValue<*> {
        val item = AvValue(
            name = "Temp Sensor",
            uuid = uuid7(),
            lastChange = Instant.parse("2023-01-01T12:00:00Z"),
            friendlyId = "1",
            refsOrNull = refs,
            markersOrNull = markers,
            spec = Unit
        )

        worker.queueAdd(item) // Add the AvItem to the worker

        return item
    }

    @Test
    @JsName("shouldReturnValuesMatchingMarkerQuery")
    fun `should return values matching the marker query`() = test { worker ->
        val marker = "temperature"
        val item = addSensor(worker, setOf(marker))

        val result = worker.queryByMarker(marker)

        assertEquals(listOf(item), result)
    }

    @Test
    @JsName("shouldNotReturnValuesIfMarkerQueryDoesNotMatch")
    fun `should not return values if the marker query does not match any values`() = test { worker ->
        val marker = "temperature"
        addSensor(worker, setOf("other-marker"))

        val result = worker.queryByMarker(marker)

        assertTrue(result.isEmpty())
    }

    @Test
    @JsName("shouldReturnMultipleValuesIfMarkersMatch")
    fun `should return multiple values if multiple markers match`() = test { worker ->
        val marker = "temperature"
        val item1 = addSensor(worker, setOf(marker))
        val item2 = addSensor(worker, setOf(marker))

        val result = worker.queryByMarker(marker)

        assertEquals(setOf(item1, item2), result.toSet())
    }

    @Test
    @JsName("shouldNotifySubscribersOnNewMarker")
    fun `should notify marker-based subscriptions when a new marker is added`() = test { worker ->
        val marker = "temperature"
        val initialItem = addSensor(worker, setOf("other-marker"))

        val channel = Channel<AvValueOperation>(1)
        val subscription = AvChannelSubscription(uuid4(), condition(marker), channel)

        worker.subscribe(listOf(subscription))

        val newValue = initialItem.copy(markersOrNull = setOf(marker))

        worker.queueUpdate(newValue)

        val received = channel.receive()
        check(received is AvoAddOrUpdate)
        assertEquals(newValue.uuid, received.value.uuid)
        assertEquals(newValue.markers, received.value.markers)
    }

    @Test
    @JsName("shouldNotifySubscribersOnMarkerRemove")
    fun `should notify marker-based subscriptions when a marker is removed`() = test { worker ->
        val marker = "temperature"

        val initialItem = addSensor(worker, markers = setOf(marker))
        val newValue = initialItem.copy(markersOrNull = null)

        val channel = Channel<AvValueOperation>(1)
        val subscription = AvChannelSubscription(uuid4(), condition(marker), channel)

        worker.subscribe(listOf(subscription))

        channel.receive() // drop the initial message which contains the initial value

        worker.queueUpdate(newValue)

        val received = channel.receive()
        check(received is AvoMarkerRemove)
        assertEquals(received.valueId, newValue.uuid)
        assertEquals(received.marker, "temperature")
    }

    fun test(timeout: Duration = 10.seconds, testFun: suspend (worker: AvValueWorker) -> Unit) =
        runTest(timeout = timeout) {
            val worker = AvValueWorker("general", proxy = false)
            worker.logger = getLogger("worker")
            val dispatcher = Dispatchers.Unconfined
            val scope = CoroutineScope(dispatcher)

            scope.launch {
                worker.mount()
                worker.run()
            }

            testFun(worker)
        }
}
