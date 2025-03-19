package `fun`.adaptive.iot.value

import `fun`.adaptive.iot.item.AioStatus
import `fun`.adaptive.iot.value.TestSupport.Companion.aioValueTest
import `fun`.adaptive.iot.value.builtin.AvString
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.utility.waitForReal
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

class AioValueClientSubscriptionTest {

    @Test
    fun basic() = aioValueTest {
        val time = Instant.parse("2023-01-01T12:00:00Z")

        val valueId = AioValueId()
        val value = AvString(valueId, time, AioStatus.OK, "Value")

        val subscription = AioValueClientSubscription(uuid4(), listOf(valueId), emptyList(), serverTransport, serverBackend.scope)

        serverWorker.subscribe(listOf(subscription))
        serverWorker.add(value)

        waitForReal(1.seconds) { clientWorker[valueId] != null }

        assertEquals(clientWorker[valueId], value)
    }

    @Test
    fun connectionBreak() = aioValueTest {
        val time = Instant.parse("2023-01-01T12:00:00Z")

        val valueId = AioValueId()
        val value = AvString(valueId, time, AioStatus.OK, "Value")

        val subscription = AioValueClientSubscription(uuid4(), listOf(valueId), emptyList(), serverTransport, serverBackend.scope)

        serverWorker.subscribe(listOf(subscription))

        serverTransport.disconnect()

        serverWorker.add(value)

        waitForReal(1.seconds) { subscription.worker == null }

        assertEquals(serverWorker.subscriptionCount(valueId), 0)
    }

}