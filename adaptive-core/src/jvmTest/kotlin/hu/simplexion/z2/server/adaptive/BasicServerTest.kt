package hu.simplexion.z2.server.adaptive

//class BasicServerTest {
//
//    @Test
//    fun basic() {
//        adaptive(AdaptiveServerAdapter()) {
//            service { TestServiceImpl() }
//            worker { TestWorkerImpl() }
//            process {  }
//        }
//    }
//
//    private class TestServiceImpl : ServiceImpl<TestServiceImpl> {
//        override var fqName = "TestService"
//    }
//
//    private class TestWorkerImpl : WorkerImpl<TestWorkerImpl> {
//        override suspend fun run(scope: CoroutineScope) {
//
//        }
//    }
//
//    private class TestProcessImpl() {
//
//    }
//}