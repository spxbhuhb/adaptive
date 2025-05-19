package `fun`.adaptive.value.local

import `fun`.adaptive.utility.waitForReal
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.ValueTestSupport.Companion.valueTest
import `fun`.adaptive.value.avById
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.js.JsName
import kotlin.test.*
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class AvListSubscriberTest {

    @Test
    @JsName("shouldCollectAndMaintainListOfMatchingValues")
    fun `should collect and maintain list of matching values`() = valueTest {
        val valueId1 = AvValueId()
        val valueId2 = AvValueId()
        val spec1 = "first"
        val spec2 = "second"

        val subscriber = AvListSubscriber(
            backend = clientBackend,
            specClass = String::class,
            avById(valueId1), avById(valueId2)
        )

        var receivedList: List<AvValue<String>>? = null
        subscriber.addListener { receivedList = it }

        waitForIdle {
            serverWorker.queueAdd(AvValue(valueId1, spec = spec1))
            serverWorker.queueAdd(AvValue(valueId2, spec = spec2))
        }

        waitForReal(1.seconds) { subscriber.value.size == 2 }

        assertEquals(2, receivedList?.size)
        assertTrue(receivedList?.any { it.uuid == valueId1 && it.spec == spec1 } == true)
        assertTrue(receivedList.any { it.uuid == valueId2 && it.spec == spec2 })

        subscriber.stop()
        waitForReal(3.seconds) { subscriber.job == null }
    }

    @Test
    @JsName("shouldIgnoreValuesWithNonMatchingSpecType")
    fun `should ignore values with non-matching spec type`() = valueTest {
        val valueId = AvValueId()
        val stringSpec = "test"
        val intSpec = 42

        val subscriber = AvListSubscriber(
            backend = clientBackend,
            specClass = String::class,
            avById(valueId)
        )

        var receivedList: List<AvValue<String>>? = null
        subscriber.addListener { receivedList = it }

        waitForIdle {
            serverWorker.queueAdd(AvValue(valueId, spec = stringSpec))
        }

        waitForReal(1.seconds) { subscriber.value.isNotEmpty() }
        assertEquals(1, receivedList?.size)
        assertEquals(stringSpec, receivedList?.first()?.spec)

        subscriber.stop()
        waitForReal(3.seconds) { subscriber.job == null }
    }

    @Test
    @JsName("shouldApplyTransformFunctionToValues")
    fun `should apply transform function to values`() = valueTest {
        val valueId1 = AvValueId()
        val valueId2 = AvValueId()
        val spec1 = "first"
        val spec2 = "second"

        // Transform that sorts values by spec
        val transform: (Map<AvValueId, AvValue<String>>) -> List<AvValue<String>> = { map ->
            map.values.sortedBy { it.spec }
        }

        val subscriber = AvListSubscriber(
            backend = clientBackend,
            specClass = String::class,
            transform = transform,
            avById(valueId1), avById(valueId2)
        )

        var receivedList: List<AvValue<String>>? = null
        subscriber.addListener { receivedList = it }

        waitForIdle {
            // Add in reverse order to test sorting
            serverWorker.queueAdd(AvValue(valueId2, spec = spec2))
            serverWorker.queueAdd(AvValue(valueId1, spec = spec1))
        }

        waitForReal(1.seconds) { subscriber.value.size == 2 }

        assertEquals(2, receivedList?.size)
        // Verify sorting by spec
        assertEquals(spec1, receivedList?.first()?.spec)
        assertEquals(spec2, receivedList?.last()?.spec)

        subscriber.stop()
        waitForReal(3.seconds) { subscriber.job == null }
    }

    @Test
    @JsName("shouldNotifyListenersOnValueUpdates")
    fun `should notify listeners on value updates`() = valueTest {
        val valueId = AvValueId()
        val initialSpec = "initial"
        val updatedSpec = "updated"

        val notificationCount = mutableListOf<Int>() // Track list sizes received

        val subscriber = AvListSubscriber(
            backend = clientBackend,
            specClass = String::class,
            avById(valueId)
        )

        subscriber.addListener { list -> notificationCount.add(list.size) }

        waitForIdle {
            serverWorker.queueAdd(AvValue(valueId, spec = initialSpec))
        }

        waitForReal(1.seconds) { subscriber.value.isNotEmpty() }
        assertEquals(1, notificationCount.last())

        serverWorker.queueUpdate(AvValue(valueId, spec = updatedSpec))

        waitForReal(1.seconds) { subscriber.value.first().spec == updatedSpec }
        assertTrue(notificationCount.size > 1) // Should have received multiple notifications
        assertEquals(1, notificationCount.last()) // List size should still be 1

        subscriber.stop()
        waitForReal(3.seconds) { subscriber.job == null }
    }

    @Test
    @JsName("shouldClearCachedValueOnUpdates")
    fun `should clear cached value on updates`() = valueTest {
        val valueId = AvValueId()
        val initialSpec = "initial"
        val updatedSpec = "updated"

        waitForIdle {
            serverWorker.queueAdd(AvValue(valueId, spec = initialSpec))
        }

        val subscriber = AvListSubscriber(
            backend = clientBackend,
            specClass = String::class,
            avById(valueId)
        )

        subscriber.addListener { } // Add listener to start subscriber

        waitForReal(1.seconds) { subscriber.value.isNotEmpty() }

        val firstAccess = subscriber.value
        assertNotNull(firstAccess)

        // Update the value
        serverWorker.queueUpdate(AvValue(valueId, spec = updatedSpec))

        waitForReal(1.seconds) { subscriber.value.first().spec == updatedSpec }

        // Cached value should be different from first access
        assertNotEquals(firstAccess, subscriber.value)

        subscriber.stop()
        waitForReal(3.seconds) { subscriber.job == null }
    }
}