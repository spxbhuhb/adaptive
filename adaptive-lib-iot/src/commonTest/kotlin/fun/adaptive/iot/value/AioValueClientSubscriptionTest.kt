package `fun`.adaptive.iot.value

import `fun`.adaptive.iot.value.TestSupport.Companion.auoValueTest
import `fun`.adaptive.iot.item.AioStatus
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.utility.waitFor
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

class AioValueClientSubscriptionTest {

    @Test
    fun basic() = auoValueTest {
        val time = Instant.parse("2023-01-01T12:00:00Z")

        val valueId = AioValueId()
        val value = AioValue(valueId, time, AioStatus.OK, "Value")

        val subscription = AioValueClientSubscription(uuid4(), listOf(valueId), serverTransport, serverBackend.scope)

        serverWorker.subscribe(listOf(subscription))
        serverWorker.update(value)

        waitFor(1.seconds) { clientWorker[valueId] != null }

        assertEquals(clientWorker[valueId], value)
    }

    @Test
    fun connectionBreak() = auoValueTest {
        val time = Instant.parse("2023-01-01T12:00:00Z")

        val valueId = AioValueId()
        val value = AioValue(valueId, time, AioStatus.OK, "Value")

        val subscription = AioValueClientSubscription(uuid4(), listOf(valueId), serverTransport, serverBackend.scope)

        serverWorker.subscribe(listOf(subscription))

        serverTransport.disconnect()

        serverWorker.update(value)

        waitFor(1.seconds) { subscription.worker == null }

        assertEquals(serverWorker.subscriptionCount(valueId), 0)
    }

}