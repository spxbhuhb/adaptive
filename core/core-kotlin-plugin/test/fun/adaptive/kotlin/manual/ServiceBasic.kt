package `fun`.adaptive.kotlin.manual

import `fun`.adaptive.backend.BackendFragment
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.log.AdaptiveLogger
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.service.ServiceConsumer
import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.service.ServiceProvider
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.service.testing.TestServiceTransport
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.wireformat.WireFormatDecoder
import `fun`.adaptive.wireformat.builtin.StringWireFormat
import kotlinx.coroutines.runBlocking

interface TestService1 {

    suspend fun testFun(arg1: Int, arg2: String): String

    class Consumer : TestService1, ServiceConsumer {

        override var serviceName: String = "fun.adaptive.service.TestService1"

        override var serviceCallTransport: ServiceCallTransport = TestServiceTransport().also { it.serviceImpl = TestService1Impl() }

        override suspend fun testFun(arg1: Int, arg2: String): String {
            return wireFormatDecoder(
                payload = callService(
                    funName = "testFun(IT)T",
                    payload = wireFormatEncoder()
                        .pseudoInstanceStart()
                        .int(fieldNumber = 1, fieldName = "arg1", value = arg1)
                        .string(fieldNumber = 2, fieldName = "arg2", value = arg2)
                        .pseudoInstanceEnd()
                )
            ).asInstance(wireFormat = StringWireFormat)
        }

    }

}

val testServiceConsumer: TestService1 = getService<TestService1>(
    transport = TestServiceTransport().also { it.serviceImpl = TestService1Impl() },
    consumer = TestService1.Consumer()
)

@ServiceProvider
class TestService1Impl : TestService1, ServiceImpl<TestService1Impl>() {

    override suspend fun testFun(arg1: Int, arg2: String): String {
        return "i:$arg1 s:$arg2 $serviceContext"
    }

    override var serviceName = "fun.adaptive.service.TestService1"

    override var fragment: BackendFragment? = null

    override fun newInstance(serviceContext: ServiceContext): TestService1Impl {
        val tmp0 = TestService1Impl()
        tmp0.serviceContextOrNull = serviceContext
        tmp0.fragment = fragment
        return tmp0
    }

    override suspend fun dispatch(funName: String, payload: WireFormatDecoder<*>): ByteArray {
        return when (funName) {
            "testFun(IT)T" ->
                wireFormatEncoder()
                    .rawInstance(
                        value = testFun(
                            arg1 = payload.int(fieldNumber = 1, fieldName = "arg1"),
                            arg2 = payload.string(fieldNumber = 2, fieldName = "arg2")
                        ), wireFormat = StringWireFormat
                    )

            else -> unknownFunction(funName = funName)
        }.pack()
    }

    override var logger: AdaptiveLogger = getLogger(name = "TestService1Impl")

}

fun box(): String {
    var response: String
    runBlocking{
        response = testServiceConsumer.testFun(arg1 = 1, arg2 = "hello")
    }
    return when {
        response.startsWith(prefix = "i:1 s:hello ServiceContext(") -> "OK"
        else -> "Fail (response=$response)"
    }
}