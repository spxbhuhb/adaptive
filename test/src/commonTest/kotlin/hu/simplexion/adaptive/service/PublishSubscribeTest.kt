package hu.simplexion.adaptive.service

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.adat.AdatCompanion
import hu.simplexion.adaptive.auth.context.publicAccess
import hu.simplexion.adaptive.server.builtin.ServiceImpl
import hu.simplexion.adaptive.service.model.RequestEnvelope
import hu.simplexion.adaptive.service.model.ResponseEnvelope
import hu.simplexion.adaptive.service.model.ServiceCallStatus
import hu.simplexion.adaptive.service.testing.TestServiceTransport
import hu.simplexion.adaptive.service.transport.ServiceCallTransport
import hu.simplexion.adaptive.service.transport.ServiceResponseListener
import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.utility.getLock
import hu.simplexion.adaptive.utility.use
import hu.simplexion.adaptive.wireformat.WireFormatProvider.Companion.decode
import hu.simplexion.adaptive.wireformat.WireFormatProvider.Companion.encode
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

class PublishSubscribeTest() {

    @Test
    fun basic() {
        val publisher = getService<PushCounterApi>()

        // test only, no need with defaultServiceCallTransport
        val service = PushCounterService()
        val transport = TestServiceTransport(service, dump = false)
        service.serviceCallTransport = transport
        (publisher as ServiceBase).serviceCallTransport = transport

        val endpoint: ServiceResponseEndpoint = UUID<RequestEnvelope>()

        val listener = CounterValueListener(publisher)

        transport.connect(endpoint, listener)

        runBlocking {
            publisher.subscribe(endpoint) // a message of 0 should arrive

            for (i in 0 until 3) {
                publisher.increment()
                listener.waitFor(i + 1)
                assertEquals(i, listener[i])
            }
        }
    }
}

@Adat
class CounterValue(
    val value: Int
) : AdatClass<CounterValue> {
    companion object : AdatCompanion<CounterValue>
}

@ServiceApi
interface PushCounterApi {

    suspend fun subscribe(endpoint: ServiceResponseEndpoint)

    suspend fun increment()

    suspend fun unsubscribe(endpoint: ServiceResponseEndpoint)

}

class CounterValueListener(
    val publisher: PushCounterApi
) : ServiceResponseListener {

    val lock = getLock()
    private val values = mutableListOf<Int>()

    override suspend fun receive(endpoint: ServiceResponseEndpoint, message: ResponseEnvelope) {
        val value = decode(message.payload, CounterValue)

        lock.use {
            values += value.value
        }

        if (value.value == 3) {
            publisher.unsubscribe(endpoint)
            defaultServiceCallTransport.disconnect(endpoint)
        }
    }

    suspend fun waitFor(size: Int) {
        withTimeout(1.seconds) {
            while (lock.use { values.size < size }) {
                delay(25L)
            }
        }
    }

    operator fun get(index: Int) =
        lock.use {
            values[index]
        }

}

class PushCounterService : PushCounterApi, ServiceImpl<PushCounterService> {

    companion object {
        val lock = getLock()
        val subscriptions = mutableMapOf<ServiceResponseEndpoint, ServiceContext>()
        var counter = 0
    }

    // test only code
    override var serviceCallTransport: ServiceCallTransport? = null

    override suspend fun subscribe(endpoint: ServiceResponseEndpoint) {
        publicAccess()

        lock.use {
            subscriptions[endpoint] = serviceContext
        }
    }

    override suspend fun increment() {
        publicAccess()

        lock.use {
            val value = CounterValue(counter ++)
            val payload = encode(value, CounterValue)
            for ((endpoint, serviceContext) in subscriptions) {
                serviceContext.send(ResponseEnvelope(endpoint, ServiceCallStatus.Ok, payload))
            }
        }
    }

    override suspend fun unsubscribe(endpoint: ServiceResponseEndpoint) {
        publicAccess()

        lock.use {
            subscriptions.remove(endpoint)
        }
    }

}