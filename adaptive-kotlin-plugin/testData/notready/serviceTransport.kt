package foo.bar

import hu.simplexion.adaptive.serialization.protobuf.*
import hu.simplexion.adaptive.service.ServiceResponseEndpoint
import hu.simplexion.adaptive.service.transport.ServiceResponseListener
import hu.simplexion.adaptive.services.*
import hu.simplexion.adaptive.services.transport.ServiceCallTransport
import kotlinx.coroutines.runBlocking
import kotlin.TODO

interface BasicService : Service {
    suspend fun a(arg1: Int): Int
}

@Suppress("UNCHECKED_CAST")
class DefaultTransport : ServiceCallTransport() {
    override suspend fun <T> call(serviceName: String, funName: String, payload: ByteArray, decoder: ProtoDecoder<T>): T {
        return 45 as T
    }

    override fun connect(endpoint: ServiceResponseEndpoint, listener: ServiceResponseListener) {
        TODO("Not yet implemented")
    }

    override fun disconnect(endpoint: ServiceResponseEndpoint) {
        TODO("Not yet implemented")
    }
}

@Suppress("UNCHECKED_CAST")
class SpecificTransport : ServiceCallTransport() {
    override suspend fun <T> call(serviceName: String, funName: String, payload: ByteArray, decoder: ProtoDecoder<T>): T {
        return 13 as T
    }

    override fun connect(endpoint: ServiceResponseEndpoint, listener: ServiceResponseListener) {
        TODO("Not yet implemented")
    }

    override fun disconnect(endpoint: ServiceResponseEndpoint) {
        TODO("Not yet implemented")
    }
}

var a = false

fun box(): String {
    return runBlocking {
        defaultServiceCallTransport = DefaultTransport()

        val b1 = getService<BasicService>()

        if (b1.a(12) != 45) return@runBlocking "Fail: through default"

        b1.serviceCallTransport = SpecificTransport()

        if (b1.a(12) != 13) return@runBlocking "Fail: through specific"

        return@runBlocking "OK"
    }
}
