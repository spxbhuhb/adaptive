package `fun`.adaptive.value.embedded

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.backend.query.firstImplOrNull
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.LifecycleBound
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.service.testing.DirectServiceTransport
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import `fun`.adaptive.utility.waitFor
import `fun`.adaptive.utility.waitForReal
import `fun`.adaptive.value.AvComputeFun
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.persistence.AbstractValuePersistence
import `fun`.adaptive.value.persistence.NoPersistence
import `fun`.adaptive.value.store.AvComputeContext
import `fun`.adaptive.wireformat.api.Json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class EmbeddedValueServer(
    val persistence : AbstractValuePersistence,
    traceTransport: Boolean = false,
    val traceWorker : Boolean = false
) : LifecycleBound {

    val lock = getLock()
    var used: Boolean = false

    val clientTransport = DirectServiceTransport(name = "client", wireFormatProvider = Json)
    val serverTransport = DirectServiceTransport(name = "server", wireFormatProvider = Json)

    init {
        if (traceTransport) {
            clientTransport.also { it.trace = true; it.transportLog.enableFine() }
            serverTransport.also { it.trace = true; it.transportLog.enableFine() }
        }
        clientTransport.peerTransport = serverTransport
        serverTransport.peerTransport = clientTransport
    }

    val serverDispatcher = Dispatchers.Unconfined
    lateinit var serverBackend: BackendAdapter

    val clientDispatcher = Dispatchers.Unconfined
    lateinit var clientBackend: BackendAdapter

    val serverWorker
        get() = serverBackend.firstImpl<AvValueWorker>()

    val clientWorker
        get() = clientBackend.firstImpl<AvValueWorker>()

    suspend fun waitForIdle(duration: Duration = 1.seconds, block: suspend () -> Unit = {}) {
        block()
        waitFor(duration) { serverWorker.isIdle && clientWorker.isIdle }
    }

    fun start(init: (AvComputeContext.() -> Unit)? = null): EmbeddedValueServer {
        lock.use {
            if (used) throw IllegalStateException("ValueTestSupport has been already used")
            used = true
        }

        // Switch to a coroutine context that is NOT a test context. The test context
        // skips delays that wreak havoc with service call timeouts that depend on
        // delays actually working.

        val serverScope = CoroutineScope(serverDispatcher)
        val clientScope = CoroutineScope(clientDispatcher)

        serverBackend = backend(serverTransport, dispatcher = serverDispatcher, scope = serverScope) {
            worker { AvValueWorker("server", proxy = false, persistence, trace = traceWorker) }
            service { AvEmbeddedServerService() }
        }

        if (init != null) {
            serverWorker.launch { serverWorker.execute(5.seconds, init) }
        }

        clientBackend = backend(clientTransport, dispatcher = clientDispatcher, scope = clientScope) {
            worker { AvValueWorker("client", proxy = true, trace = traceWorker) }
            service { AvEmbeddedClientService() }
        }

        return this
    }

    fun stop() {
        serverBackend.stop()
        clientBackend.stop()
    }

    suspend fun <T> execute(computeFun: AvComputeFun<T>) : T {
        return serverWorker.execute(5.seconds, computeFun)
    }

    override fun dispose(fragment: AdaptiveFragment, index : Int) {
        stop()
    }

    companion object {

        fun embeddedValueServer(
            persistence : AbstractValuePersistence = NoPersistence(),
            initFun: AvComputeContext.() -> Unit = {  }
        ) = EmbeddedValueServer(persistence).start(initFun)

        suspend fun withEmbeddedValueServer(testFun: suspend EmbeddedValueServer.() -> Unit) {

            with(EmbeddedValueServer(NoPersistence())) {
                start()

                waitForReal(5.seconds) { serverBackend.firstImplOrNull<AvEmbeddedServerService>() != null }
                waitForReal(5.seconds) { clientBackend.firstImplOrNull<AvEmbeddedClientService>() != null }

                withContext(clientDispatcher) {
                    testFun()
                }

                stop()
            }
        }
    }
}