package hu.simplexion.z2.server.adaptive

import hu.simplexion.z2.adaptive.adaptive
import hu.simplexion.z2.adaptive.testing.AdaptiveTestAdapter
import hu.simplexion.z2.server.adaptive.components.process
import hu.simplexion.z2.server.adaptive.components.service
import hu.simplexion.z2.server.adaptive.components.worker
import hu.simplexion.z2.server.worker.WorkerImpl
import hu.simplexion.z2.services.ServiceImpl
import kotlinx.coroutines.CoroutineScope
import kotlin.test.Test

class BasicServerTest {

    @Test
    fun basic() {
        val adapter = AdaptiveTestAdapter()

        adaptive {
            service { TestServiceImpl() }
            worker { TestWorkerImpl() }
            process {  }
        }
    }

    private class TestServiceImpl : ServiceImpl<TestServiceImpl> {
        override var fqName = "TestService"
    }

    private class TestWorkerImpl : WorkerImpl<TestWorkerImpl> {
        override suspend fun run(scope: CoroutineScope) {

        }
    }

    private class TestProcessImpl() {

    }
}