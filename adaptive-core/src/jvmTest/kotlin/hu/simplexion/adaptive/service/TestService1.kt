package hu.simplexion.adaptive.service

import hu.simplexion.adaptive.server.builtin.ServiceImpl
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.builtin.StringWireFormat

class TestService1(
    override val serviceContext: ServiceContext = ServiceContext()
) : TestApi1, ServiceImpl<TestService1> {

    override var serviceName = "hu.simplexion.adaptive.services.TestApi1"

    override fun newInstance(serviceContext: ServiceContext): TestService1 {
        return TestService1(serviceContext)
    }

    override suspend fun testFun(arg1: Int, arg2: String) =
        "i:$arg1 s:$arg2 $serviceContext"

    override suspend fun dispatch(funName: String, payload: WireFormatDecoder<*>): ByteArray =
        when (funName) {
            "testFun;IS" -> wireFormatEncoder().rawInstance(testFun(payload.int(1, "arg1"), payload.string(2, "arg2")), StringWireFormat)
            else -> throw IllegalStateException("unknown function: $funName")
        }.pack()

}