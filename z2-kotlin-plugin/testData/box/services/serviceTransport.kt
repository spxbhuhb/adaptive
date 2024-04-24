package foo.bar

import hu.simplexion.z2.serialization.protobuf.*
import hu.simplexion.z2.services.*
import hu.simplexion.z2.services.transport.ServiceCallTransport
import hu.simplexion.z2.services.transport.LocalServiceCallTransport

import kotlinx.coroutines.runBlocking

interface BasicService : Service {
    suspend fun a(arg1: Int): Int
}

class BasicServiceImpl : BasicService, ServiceImpl<BasicServiceImpl> {

    override suspend fun a(arg1: Int): Int {
        return arg1 + 1
    }

}

@Suppress("UNCHECKED_CAST")
class DefaultTransport : ServiceCallTransport {
    override suspend fun <T> call(serviceName: String, funName: String, payload: ByteArray, decoder: ProtoDecoder<T>): T {
        return 45 as T
    }
}

var a = false

fun box(): String {
    return runBlocking {
        defaultServiceCallTransport = DefaultTransport()
        defaultServiceImplFactory += BasicServiceImpl()

        val b1 = getService<BasicService>()

        if (b1.a(12) != 45) return@runBlocking "Fail: through default"

        b1.serviceCallTransport = LocalServiceCallTransport()

        if (b1.a(12) != 13) return@runBlocking "Fail: through local"

        return@runBlocking "OK"
    }
}
