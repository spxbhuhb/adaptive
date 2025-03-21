package `fun`.adaptive.value

import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.utility.waitForReal
import `fun`.adaptive.value.TestSupport.Companion.avValueTest
import `fun`.adaptive.value.builtin.AvString
import `fun`.adaptive.value.item.AvStatus
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

class AvValueClientSubscriptionTest {

    @Test
    fun basic() = avValueTest {
        val time = Instant.parse("2023-01-01T12:00:00Z")

        val valueId = AvValueId()
        val value = AvString(valueId, time, AvStatus.OK, "Value")

        val subscription = AvClientSubscription(uuid4(), condition(valueId), serverTransport, serverBackend.scope)

        serverWorker.subscribe(listOf(subscription))
        serverWorker.queueAdd(value)

        waitForReal(1.seconds) { clientWorker[valueId] != null }

        assertEquals(clientWorker[valueId], value)
    }

    @Test
    fun connectionBreak() = avValueTest {
        val time = Instant.parse("2023-01-01T12:00:00Z")

        val valueId = AvValueId()
        val value = AvString(valueId, time, AvStatus.OK, "Value")

        val subscription = AvClientSubscription(uuid4(), condition(valueId), serverTransport, serverBackend.scope)

        serverWorker.subscribe(listOf(subscription))

        serverTransport.disconnect()

        serverWorker.queueAdd(value)

        waitForReal(1.seconds) { subscription.worker == null }

        assertEquals(serverWorker.subscriptionCount(valueId), 0)
    }

}