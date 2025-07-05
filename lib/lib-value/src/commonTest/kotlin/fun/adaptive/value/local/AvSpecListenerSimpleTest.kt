package `fun`.adaptive.value.local

import `fun`.adaptive.service.api.getService
import `fun`.adaptive.utility.UUID.Companion.uuid4
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
import kotlin.time.Duration.Companion.seconds

class AvSpecListenerSimpleTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    @JsName("testSpecListenerWithValueId")
    fun `test spec listener with value id`() = runTest {
        // Create a test value ID
        val valueId = uuid4<AvValue<*>>()

        println("[DEBUG_LOG] Created valueId: $valueId")

        // Set up the embedded value server with a test value
        val env = embeddedValueServer {
            addValue {
                AvValue(
                    uuid = valueId,
                    spec = 42
                )
            }
        }

        println("[DEBUG_LOG] Created embedded value server")

        // Pull the value from remote to local
        val service = getService<AvValueApi>(env.clientTransport)
        withContext(Dispatchers.Default.limitedParallelism(1)) {
            service.subscribe(listOf(AvSubscribeCondition(valueId)))
        }

        println("[DEBUG_LOG] Subscribed to value")

        // Wait for the value to be available in the client worker
        waitForReal(5.seconds) {
            val value = env.clientWorker.getOrNull<Int>(valueId)
            println("[DEBUG_LOG] Checking if value is available: ${value != null}")
            value != null
        }

        println("[DEBUG_LOG] Value is available in client worker")

        // Create a mutable list to track processed values
        val processedValues = mutableListOf<Int>()

        // Create and start the spec listener with valueId condition
        val listener = AvSpecListener<Int>(
            values = env.clientWorker,
            conditions = listOf(AvSubscribeCondition(valueId)),
            specClass = Int::class,
            processFun = { value ->
                println("[DEBUG_LOG] Processing value: ${value.spec}")
                processedValues.add(value.spec)
            }
        )

        println("[DEBUG_LOG] Created spec listener")

        listener.start()

        println("[DEBUG_LOG] Started spec listener")

        // Update the value to trigger an operation that the listener will process
        println("[DEBUG_LOG] Updating value to trigger an operation")
        env.serverWorker.queueUpdate(
            AvValue(
                uuid = valueId,
                revision = 2L,
                spec = 42
            )
        )

        // Wait for the value to be processed
        waitForReal(5.seconds) {
            println("[DEBUG_LOG] Checking if value was processed: ${processedValues.isNotEmpty()}")
            processedValues.isNotEmpty()
        }

        println("[DEBUG_LOG] Value was processed")

        // Verify the value was processed correctly
        assertEquals(2, processedValues.size)
        assertEquals(42, processedValues[0])
        assertEquals(42, processedValues[1])

        // Clean up
        listener.stop()

        println("[DEBUG_LOG] Test completed successfully")
    }
}
