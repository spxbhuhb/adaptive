package `fun`.adaptive.lib.util.testing

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.BackendFragment
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.foundation.query.firstOrNull
import `fun`.adaptive.wireformat.WireFormatProvider
import `fun`.adaptive.wireformat.api.Json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.reflect.KClass

abstract class AbstractTestSupport(
    traceClient: Boolean = false,
    traceServer: Boolean = false,
    val wireFormatProvider: WireFormatProvider = Json,
    val workerClass: KClass<*>? = null
) {

    open val clientTransport = makeTransport("client", traceClient)
    open val serverTransport = makeTransport("server", traceServer)

    fun makeTransport(name: String, trace: Boolean) =
        TestServiceTransport(
            name = name,
            wireFormatProvider = wireFormatProvider,
        ).also {
            if (trace) {
                it.trace = true
                it.transportLog.enableFine()
            }
        }

    init {
        clientTransport.peerTransport = serverTransport
        serverTransport.peerTransport = clientTransport
    }

    lateinit var serverBackend: BackendAdapter

    lateinit var clientBackend: BackendAdapter

    val serverWorker
        get() = serverBackend.firstOrNull { workerClass?.isInstance((it as? BackendFragment)?.impl) == true }

    val clientWorker
        get() = clientBackend.firstOrNull { workerClass?.isInstance((it as? BackendFragment)?.impl) == true }

    open val serverWorkers = emptyList<WorkerImpl<*>>()
    open val serverServices = emptyList<ServiceImpl<*>>()
    open val clientWorkers = emptyList<WorkerImpl<*>>()
    open val clientServices = emptyList<ServiceImpl<*>>()

    suspend fun test(testFun: suspend () -> Unit) {

        // Switch to a coroutine context that is NOT a test context. The test context
        // skips delays which wreaks havoc with service call timeouts that depend on
        // delays actually working.

        val serverDispatcher = Dispatchers.Unconfined
        val serverScope = CoroutineScope(serverDispatcher)

        val clientDispatcher = Dispatchers.Unconfined
        val clientScope = CoroutineScope(clientDispatcher)

        serverBackend = backend(serverTransport, dispatcher = serverDispatcher, scope = serverScope) {
            for (worker in serverWorkers) {
                worker { worker }
            }
            for (service in serverServices) {
                service { service }
            }
        }

        clientBackend = backend(clientTransport, dispatcher = clientDispatcher, scope = clientScope) {
            for (worker in clientWorkers) {
                worker { worker }
            }
            for (service in clientServices) {
                service { service }
            }
        }

        withContext(clientDispatcher) {
            testFun()
        }

        clientBackend.stop()
        serverBackend.stop()
    }

}