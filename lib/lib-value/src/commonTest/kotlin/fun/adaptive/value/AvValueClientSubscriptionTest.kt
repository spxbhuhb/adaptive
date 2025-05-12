package `fun`.adaptive.value

import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.utility.waitForReal
import `fun`.adaptive.value.TestSupport.Companion.valueTest
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

class AvValueClientSubscriptionTest {

    @Test
    fun basic() = valueTest {
        val time = Instant.parse("2023-01-01T12:00:00Z")

        val valueId = AvValueId()
        val value = AvValue(valueId, time, spec = "Value")

        val subscription = AvClientSubscription(uuid4(), condition(valueId), serverTransport, serverBackend.scope)

        serverWorker.subscribe(listOf(subscription))
        serverWorker.queueAdd(value)

        waitForReal(1.seconds) { clientWorker.getOrNull(valueId) != null }

        assertEquals(clientWorker.get(valueId), value)
    }

    @Test
    fun connectionBreak() = valueTest {
        val time = Instant.parse("2023-01-01T12:00:00Z")

        val valueId = AvValueId()
        val value = AvValue(valueId, time, spec = "Value")

        val subscription = AvClientSubscription(uuid4(), condition(valueId), serverTransport, serverBackend.scope)

        serverWorker.subscribe(listOf(subscription))

        serverTransport.disconnect()

        serverWorker.queueAdd(value)

        waitForReal(1.seconds) { subscription.store == null }

        assertEquals(serverWorker.subscriptionCount(valueId), 0)
    }

}