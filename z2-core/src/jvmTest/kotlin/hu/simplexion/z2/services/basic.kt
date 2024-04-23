package hu.simplexion.z2.services

import hu.simplexion.z2.wireformat.WireFormatDecoder
import hu.simplexion.z2.wireformat.WireFormatProvider.Companion.defaultWireFormatProvider
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class BasicTest {

    fun box(): String {
        var response: String
        runBlocking {
            defaultServiceImplFactory += TestServiceImpl(BasicServiceContext())
            response = TestServiceConsumer.testFun(1, "hello")
        }
        return if (response.startsWith("i:1 s:hello BasicServiceContext(uuid=")) "OK" else "Fail (response=$response)"
    }

    @Test
    fun basicTest() {
        assertEquals("OK", box())
    }

}

interface TestService : Service {
    suspend fun testFun(arg1: Int, arg2: String): String
}

object TestServiceConsumer : TestService {

    override var fqName = "TestService"

    override suspend fun testFun(arg1: Int, arg2: String): String =
        wireFormatStandalone.decodeString(
            defaultServiceCallTransport.call(
                fqName,
                "testFun",
                wireFormatEncoder
                    .int(1, "arg1", arg1)
                    .string(2, "arg2", arg2)
                    .pack()
            )
        )

}

class TestServiceImpl(override val serviceContext: ServiceContext) : TestService, ServiceImpl<TestServiceImpl> {

    override var fqName = "TestService"

    override suspend fun dispatch(funName: String, payload: WireFormatDecoder): ByteArray =
        when (funName) {
            "testFun" -> defaultWireFormatProvider.standalone().encodeString(testFun(payload.int(1, "arg1"), payload.string(2, "arg2")))
            else -> throw IllegalStateException("unknown function: $funName")
        }

    override fun newInstance(serviceContext: ServiceContext): TestServiceImpl {
        return TestServiceImpl(serviceContext)
    }

    override suspend fun testFun(arg1: Int, arg2: String): String {
        return "i:$arg1 s:$arg2 $serviceContext"
    }

}