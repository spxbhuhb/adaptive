package `fun`.adaptive.service.transport

import `fun`.adaptive.service.TestApi1
import `fun`.adaptive.service.TestService1
import `fun`.adaptive.service.factory.BasicServiceImplFactory
import `fun`.adaptive.wireformat.api.Json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.PipedInputStream
import java.io.PipedOutputStream
import kotlin.test.assertTrue

class StreamServiceCallTransportTest {

    @Test
    fun basic() {
        val serviceImplFactory = BasicServiceImplFactory()
        serviceImplFactory += TestService1()

        val outputStream1 = PipedOutputStream()
        val inputStream1 = PipedInputStream(outputStream1)

        val outputStream2 = PipedOutputStream()
        val inputStream2 = PipedInputStream(outputStream2)

        val t1 = StreamServiceCallTransport(
            CoroutineScope(Dispatchers.IO),
            inputStream1,
            outputStream2,
            Json
        )

        val t2 = StreamServiceCallTransport(
            CoroutineScope(Dispatchers.IO),
            inputStream2,
            outputStream1,
            Json
        )

        t1.trace = true
        t2.trace = true

        runBlocking {
            try {
                t1.start(serviceImplFactory)
                t2.start(serviceImplFactory)

                val consumer = TestApi1.Consumer()
                consumer.serviceCallTransport = t1

                val result = consumer.testFun(12, "ab")

                assertTrue(result.startsWith("i:12 s:ab ServiceContext("))

            } finally {
                t1.stop()
                t2.stop()
            }
        }
    }
}
