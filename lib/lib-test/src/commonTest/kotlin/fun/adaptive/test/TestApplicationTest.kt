package `fun`.adaptive.test

@Suppress("OPT_IN_USAGE")
class TestApplicationTest {

//    @Test
//    fun basic() {
//        testServer {
//            worker { AvValueWorker(proxy = false) }
//        }.also {
//            it.start()
//            assertNotNull(it.backend.firstImpl<AvValueWorker>())
//        }
//    }
//
//    @Test
//    fun withModule() {
//        testServer {
//            module { ValueServerModule() }
//        }.also {
//            it.start()
//            assertNotNull(it.backend.firstImpl<AvValueWorker>())
//        }
//    }
//
//    @Test
//    fun serverClient() = runTest {
//        val server = testServer {
//            module { ValueServerModule() }
//        }
//
//        val client = testClient(server) {
//            backendModule { ValueClientModule() }
//        }
//
//        val serverWorker = server.backend.firstImpl<AvValueWorker>()
//        val value = serverWorker.executeOutOfBand {
//            addValue { AvValue(spec = "Hello World!") }
//        }
//
//        withContext(Dispatchers.Default.limitedParallelism(1)) {
//            val valueService = getService<AvValueApi>(client.backend.transport)
//            val readBack = valueService.get(value.uuid)
//
//            assertEquals(value.spec, readBack?.spec)
//        }
//    }
}