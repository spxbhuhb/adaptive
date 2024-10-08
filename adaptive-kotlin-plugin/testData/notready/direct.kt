/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package foo.bar

import `fun`.adaptive.serialization.protobuf.*
import `fun`.adaptive.services.*
import `fun`.adaptive.services.transport.ServiceCallTransport
import kotlinx.coroutines.runBlocking

interface BasicService : Service {
    suspend fun a(arg1: Int): Int
}

val basicServiceConsumer = getService<BasicService>()

class BasicServiceImpl : BasicService, ServiceImpl<BasicServiceImpl> {

    override suspend fun a(arg1: Int): Int {
        return arg1 + 1
    }

}

fun box(): String {
    runBlocking {
        defaultServiceCallTransport = DumpTransport()
        defaultServiceImplFactory += BasicServiceImpl()

        val b1 = getService<BasicService>()
        val b2 = BasicServiceImpl()

        if (b1.a(12) != 13) return@runBlocking "Fail: through transport"
        if (b2.a(12) != 13) return@runBlocking "Fail: direct"
    }
    return "OK"
}

class DumpTransport : ServiceCallTransport {
    override suspend fun <T> call(serviceName: String, funName: String, payload: ByteArray, decoder: ProtoDecoder<T>): T {
        println("==== REQUEST ====")
        println(serviceName)
        println(funName)
        println(payload.dumpProto())

        val service = requireNotNull(defaultServiceImplFactory[serviceName, ServiceContext()])

        val responseBuilder = ProtoMessageBuilder()

        service.dispatch(funName, ProtoMessage(payload), responseBuilder)

        val responsePayload = responseBuilder.pack()
        println("==== RESPONSE ====")
        println(responsePayload.dumpProto())
        println(decoder::class.qualifiedName)

        return decoder.decodeProto(ProtoMessage(responseBuilder.pack()))
    }
}