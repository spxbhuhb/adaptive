package `fun`.adaptive.value.local

import `fun`.adaptive.service.api.getService
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import `fun`.adaptive.utility.waitForReal
import `fun`.adaptive.value.*
import `fun`.adaptive.value.embedded.EmbeddedValueServer.Companion.embeddedValueServer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

class AvSpecListenerTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    @JsName("testSpecListenerWithValueAlreadyInLocal")
    fun `test spec listener with value already in local`() = runTest {
        // Create a test value ID
        val valueId = uuid4<AvValue<*>>()
        val marker = "test-marker"

        // Set up the embedded value server with a test value
        val env = embeddedValueServer {
            addValue { 
                AvValue(
                    uuid = valueId, 
                    spec = 42,
                    markersOrNull = setOf(marker)
                ) 
            }
        }

        // Pull the value from remote to local
        val service = getService<AvValueApi>(env.clientTransport)
        withContext(Dispatchers.Default.limitedParallelism(1)) {
            service.subscribe(listOf(AvSubscribeCondition(valueId)))
        }

        // Wait for the value to be available in the client worker
        waitForReal(5.seconds) { env.clientWorker.getOrNull<Int>(valueId) != null }

        // Create a mutable list to track processed values
        val processedValues = mutableListOf<Int>()

        // Create and start the spec listener
        val listener = AvSpecListener(
            values = env.clientWorker,
            marker = marker,
            specClass = Int::class
        ) { value ->
            processedValues.add(value.spec)
        }

        listener.start()

        // Update the value to trigger an operation that the listener will process
        env.serverWorker.queueUpdate(
            AvValue(
                uuid = valueId, 
                revision = 2L,
                spec = 42,
                markersOrNull = setOf(marker)
            )
        )

        // Wait for the value to be processed
        waitForReal(5.seconds) { processedValues.size == 2 }

        // Verify the value was processed correctly
        assertEquals(2, processedValues.size)
        assertEquals(42, processedValues[0])
        assertEquals(42, processedValues[1])

        // Clean up
        listener.stop()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    @JsName("testSpecListenerWithValueChange")
    fun `test spec listener with value change`() = runTest {
        // Create a test value ID
        val valueId = uuid4<AvValue<*>>()
        val marker = "test-marker"

        // Set up the embedded value server with a test value
        val env = embeddedValueServer {
            addValue { 
                AvValue(
                    uuid = valueId, 
                    spec = 42,
                    markersOrNull = setOf(marker)
                ) 
            }
        }

        // Pull the value from remote to local
        val service = getService<AvValueApi>(env.clientTransport)
        withContext(Dispatchers.Default.limitedParallelism(1)) {
            service.subscribe(listOf(AvSubscribeCondition(valueId)))
        }

        // Wait for the value to be available in the client worker
        waitForReal(5.seconds) { env.clientWorker.getOrNull<Int>(valueId) != null }

        // Create a mutable list to track processed values
        val processedValues = mutableListOf<Int>()

        // Create and start the spec listener
        val listener = AvSpecListener(
            values = env.clientWorker,
            marker = marker,
            specClass = Int::class
        ) { value ->
            processedValues.add(value.spec)
        }

        listener.start()

        // Update the value on the server to trigger initial processing
        env.serverWorker.queueUpdate(
            AvValue(
                uuid = valueId, 
                revision = 2L,
                spec = 42,
                markersOrNull = setOf(marker)
            )
        )

        // Wait for the initial value to be processed
        waitForReal(5.seconds) { processedValues.isNotEmpty() }

        // Update the value again with a new value
        env.serverWorker.queueUpdate(
            AvValue(
                uuid = valueId, 
                revision = 3L,
                spec = 84,
                markersOrNull = setOf(marker)
            )
        )

        // Wait for the updated value to be processed
        waitForReal(5.seconds) { processedValues.size > 2 }

        // Verify both values were processed correctly
        assertEquals(3, processedValues.size)
        assertEquals(42, processedValues[0])
        assertEquals(42, processedValues[1])
        assertEquals(84, processedValues[2])

        // Clean up
        listener.stop()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    @JsName("testSpecListenerWithMultipleConditions")
    fun `test spec listener with multiple conditions`() = runTest {
        // Create test value IDs
        val valueId1 = uuid4<AvValue<*>>()
        val valueId2 = uuid4<AvValue<*>>()
        val marker1 = "test-marker-1"
        val marker2 = "test-marker-2"

        println("[DEBUG_LOG] Created valueIds: $valueId1, $valueId2")
        println("[DEBUG_LOG] Created markers: $marker1, $marker2")

        // Set up the embedded value server with test values
        val env = embeddedValueServer {
            addValue { 
                AvValue(
                    uuid = valueId1, 
                    spec = 42,
                    markersOrNull = setOf(marker1)
                ) 
            }
            addValue { 
                AvValue(
                    uuid = valueId2, 
                    spec = 84,
                    markersOrNull = setOf(marker2)
                ) 
            }
        }

        println("[DEBUG_LOG] Created embedded value server")

        // Pull the values from remote to local
        val service = getService<AvValueApi>(env.clientTransport)
        withContext(Dispatchers.Default.limitedParallelism(1)) {
            service.subscribe(listOf(
                AvSubscribeCondition(valueId1),
                AvSubscribeCondition(valueId2)
            ))
        }

        println("[DEBUG_LOG] Subscribed to values")

        // Wait for the values to be available in the client worker
        waitForReal(5.seconds) { 
            val value1 = env.clientWorker.getOrNull<Int>(valueId1)
            val value2 = env.clientWorker.getOrNull<Int>(valueId2)
            println("[DEBUG_LOG] Checking if values are available: ${value1 != null}, ${value2 != null}")
            value1 != null && value2 != null 
        }

        println("[DEBUG_LOG] Values are available in client worker")

        // Create a mutable list to track processed values
        val lock = getLock()
        val processedValues = mutableListOf<Int>()

        // Create and start separate spec listeners for each value
        val listener1 = AvSpecListener<Int>(
            values = env.clientWorker,
            conditions = listOf(AvSubscribeCondition(valueId = valueId1)),
            specClass = Int::class,
            processFun = { value ->
                println("[DEBUG_LOG] Processing value 1: ${value.spec}")
                lock.use {
                    processedValues.add(value.spec)
                }
            }
        )

        val listener2 = AvSpecListener<Int>(
            values = env.clientWorker,
            conditions = listOf(AvSubscribeCondition(valueId = valueId2)),
            specClass = Int::class,
            processFun = { value ->
                println("[DEBUG_LOG] Processing value 2: ${value.spec}")
                lock.use {
                    processedValues.add(value.spec)
                }
            }
        )

        println("[DEBUG_LOG] Created spec listeners")

        listener1.start()
        listener2.start()

        println("[DEBUG_LOG] Started spec listeners")

        // Update both values to trigger operations that the listeners will process
        println("[DEBUG_LOG] Updating value 1")
        env.serverWorker.queueUpdate(
            AvValue(
                uuid = valueId1, 
                revision = 2L,
                spec = 42,
                markersOrNull = setOf(marker1)
            )
        )

        println("[DEBUG_LOG] Updating value 2")
        env.serverWorker.queueUpdate(
            AvValue(
                uuid = valueId2, 
                revision = 2L,
                spec = 84,
                markersOrNull = setOf(marker2)
            )
        )

        // Wait for both values to be processed
        waitForReal(5.seconds) {
            lock.use {
                println("[DEBUG_LOG] Checking if values were processed: ${processedValues.size}")
                processedValues.size > 3
            }
        }

        println("[DEBUG_LOG] Values were processed")

        // Verify both values were processed
        assertEquals(4, processedValues.size)
        assertTrue(processedValues.contains(42))
        assertTrue(processedValues.contains(84))

        // Clean up
        listener1.stop()
        listener2.stop()

        println("[DEBUG_LOG] Test completed")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    @JsName("testSpecListenerStop")
    fun `test spec listener stop`() = runTest {
        // Create a test value ID
        val valueId = uuid4<AvValue<*>>()
        val marker = "test-marker"

        // Set up the embedded value server with a test value
        val env = embeddedValueServer {
            addValue { 
                AvValue(
                    uuid = valueId, 
                    spec = 42,
                    markersOrNull = setOf(marker)
                ) 
            }
        }

        // Pull the value from remote to local
        val service = getService<AvValueApi>(env.clientTransport)
        withContext(Dispatchers.Default.limitedParallelism(1)) {
            service.subscribe(listOf(AvSubscribeCondition(valueId)))
        }

        // Wait for the value to be available in the client worker
        waitForReal(5.seconds) { env.clientWorker.getOrNull<Int>(valueId) != null }

        // Create a mutable list to track processed values
        val processedValues = mutableListOf<Int>()

        // Create and start the spec listener
        val listener = AvSpecListener(
            values = env.clientWorker,
            marker = marker,
            specClass = Int::class
        ) { value ->
            processedValues.add(value.spec)
        }

        listener.start()

        // Update the value to trigger an operation that the listener will process
        env.serverWorker.queueUpdate(
            AvValue(
                uuid = valueId, 
                revision = 2L,
                spec = 42,
                markersOrNull = setOf(marker)
            )
        )

        // Wait for the value to be processed
        waitForReal(5.seconds) { processedValues.size == 2 }

        // Stop the listener
        listener.stop()

        // Update the value on the server again
        env.serverWorker.queueUpdate(
            AvValue(
                uuid = valueId, 
                revision = 3L,
                spec = 84,
                markersOrNull = setOf(marker)
            )
        )

        // Wait a bit to ensure the update has time to propagate
        waitForReal(2.seconds)

        // Verify only the initial value was processed (since the listener was stopped)
        assertEquals(2, processedValues.size)
        assertEquals(42, processedValues[0])
        assertEquals(42, processedValues[1])

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    @JsName("testSpecListenerWithServerWorker")
    fun `test spec listener with server worker and value already in server worker`() = runTest {
        // Create a test value ID
        val valueId = uuid4<AvValue<*>>()
        val marker = "test-marker"

        println("[DEBUG_LOG] Created valueId: $valueId with marker: $marker")

        // Set up the embedded value server with a test value
        val env = embeddedValueServer {
            addValue { 
                AvValue(
                    uuid = valueId, 
                    spec = 42,
                    markersOrNull = setOf(marker)
                ) 
            }
        }

        println("[DEBUG_LOG] Created embedded value server")

        // Verify the value is in the server worker
        waitForReal(5.seconds) { 
            val value = env.serverWorker.getOrNull<Int>(valueId)
            println("[DEBUG_LOG] Value in server worker: ${value != null}")
            value != null 
        }

        // Create a mutable list to track processed values
        val processedValues = mutableListOf<Int>()

        println("[DEBUG_LOG] Creating spec listener with server worker")

        // Create and start the spec listener with the server worker
        val listener = AvSpecListener(
            values = env.serverWorker, // Using server worker instead of client worker
            marker = marker,
            specClass = Int::class
        ) { value ->
            println("[DEBUG_LOG] Processing value: ${value.spec}")
            processedValues.add(value.spec)
        }

        println("[DEBUG_LOG] Starting spec listener")
        listener.start()

        // Wait for the value to be processed
        waitForReal(5.seconds) { 
            println("[DEBUG_LOG] Checking if value was processed: ${processedValues.isNotEmpty()}")
            processedValues.isNotEmpty() 
        }

        // Verify the value was processed correctly
        assertEquals(1, processedValues.size, "Expected one value to be processed")
        assertEquals(42, processedValues[0], "Expected processed value to be 42")

        // Clean up
        listener.stop()
        println("[DEBUG_LOG] Test completed successfully")
    }
}
