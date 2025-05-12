package `fun`.adaptive.value.local

import `fun`.adaptive.general.ObservableListener
import `fun`.adaptive.utility.waitForReal
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.TestSupport.Companion.valueTest
import `fun`.adaptive.value.avById
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class AvSingleSubscriberTest {

    @Test
    fun `subscriber should receive and process matching value updates`() = valueTest {
        val valueId = AvValueId()
        val initialSpec = "initial"
        val updatedSpec = "updated"

        var value : String? = null

        waitForIdle {
            serverWorker.queueAdd(AvValue(valueId, spec = initialSpec))
        }

        val subscriber = AvSingleSubscriber(
            backend = clientBackend,
            specClass = String::class,
            condition = avById(valueId)
        )

        subscriber.addListener { value = it?.spec } // to start the subscriber

        waitForReal(1.seconds) { subscriber.value != null }

        assertEquals(initialSpec, subscriber.value?.spec)
        assertEquals(initialSpec, value)

        serverWorker.queueUpdate(AvValue(valueId, spec = updatedSpec))

        waitForReal(1.seconds) { subscriber.value?.revision == 2L }

        assertEquals(updatedSpec, subscriber.value?.spec)
        assertEquals(updatedSpec, value)
    }

    @Test
    fun `subscriber should ignore values with non-matching spec type`() = valueTest {
        val valueId = AvValueId()
        val stringSpec = "test string"
        val intSpec = 42

        // Create a subscriber expecting String specs
        val subscriber = AvSingleSubscriber(
            backend = clientBackend,
            specClass = String::class,
            condition = avById(valueId)
        )

        var receivedValue: String? = null
        subscriber.addListener { receivedValue = it?.spec }

        // Add value with a matching spec type
        waitForIdle {
            serverWorker.queueAdd(AvValue(valueId, spec = stringSpec))
        }

        waitForReal(1.seconds) { subscriber.value != null }
        assertEquals(stringSpec, receivedValue)

        // Add value with non-matching spec type (Int)
        waitForIdle {
            serverWorker.queueUpdate(AvValue(valueId, spec = intSpec))
        }

        // Value should not be updated since spec type doesn't match
        waitForReal(1.seconds)
        assertEquals(stringSpec, receivedValue)
        assertEquals(stringSpec, subscriber.value?.spec)
    }

    @Test
    fun `subscriber should notify listeners of value changes`() = valueTest {
        val valueId = AvValueId()
        val initialSpec = "initial"
        val updatedSpec = "updated"
        
        val receivedValues = mutableListOf<String?>()
        
        val subscriber = AvSingleSubscriber(
            backend = clientBackend,
            specClass = String::class,
            condition = avById(valueId)
        )

        // Add multiple listeners to ensure all are
        val listener1 = ObservableListener<AvValue<String>?> { receivedValues.add(it?.spec) }
        val listener2 = ObservableListener<AvValue<String>?> { receivedValues.add(it?.spec) }
        
        subscriber.addListener(listener1)
        subscriber.addListener(listener2)

        // Initial value
        waitForIdle {
            serverWorker.queueAdd(AvValue(valueId, spec = initialSpec))
        }

        waitForReal(1.seconds) { subscriber.value != null }
        
        // Should have two notifications (one per listener) with initial value
        assertEquals(2, receivedValues.count { it == initialSpec })

        // Update value
        serverWorker.queueUpdate(AvValue(valueId, spec = updatedSpec))
        
        waitForReal(1.seconds) { subscriber.value?.revision == 2L }
        
        // Should have two more notifications with updated value
        assertEquals(2, receivedValues.count { it == updatedSpec })
        
        // Remove one listener
        subscriber.removeListener(listener1)
        
        // Another update
        val finalSpec = "final"
        serverWorker.queueUpdate(AvValue(valueId, spec = finalSpec))
        
        waitForReal(1.seconds) { subscriber.value?.revision == 3L }
        
        // Should have only one notification since we removed a listener
        assertEquals(1, receivedValues.count { it == finalSpec })
    }
}