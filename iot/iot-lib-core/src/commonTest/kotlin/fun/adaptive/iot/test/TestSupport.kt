package `fun`.adaptive.iot.test

import `fun`.adaptive.auth.app.NoAuthServerModule
import `fun`.adaptive.auth.context.ensureLoggedIn
import `fun`.adaptive.auth.model.Session
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.iot.app.IotServerModule
import `fun`.adaptive.iot.device.AioDeviceApi
import `fun`.adaptive.iot.device.AioDeviceService
import `fun`.adaptive.iot.point.AioPointApi
import `fun`.adaptive.iot.point.AioPointService
import `fun`.adaptive.iot.space.AioSpaceApi
import `fun`.adaptive.iot.space.AioSpaceService
import `fun`.adaptive.lib.util.app.UtilServerModule
import `fun`.adaptive.service.testing.DirectServiceTransport
import `fun`.adaptive.test.TestServerApplication
import `fun`.adaptive.test.TestServerApplication.Companion.testServer
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.utility.ensure
import `fun`.adaptive.utility.resolve
import `fun`.adaptive.value.AvValueClientService
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.app.ValueServerModule
import `fun`.adaptive.value.persistence.FilePersistence
import `fun`.adaptive.wireformat.api.Json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlinx.io.files.Path
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class TestSupport {

    val serverTransport = DirectServiceTransport(name = "node", wireFormatProvider = Json) // .also { it.trace = true; it.transportLog.enableFine() }
    val clientTransport = DirectServiceTransport(name = "client", wireFormatProvider = Json) // .also { it.trace = true; it.transportLog.enableFine() }

    init {
        serverTransport.peerTransport = clientTransport
        clientTransport.peerTransport = serverTransport
    }

    lateinit var clientBackend: BackendAdapter

    lateinit var serverApp : TestServerApplication

    val serverValueWorker
        get() = serverApp.backend.firstImpl<AvValueWorker>()

    val pointService : AioPointApi
        get() = serverApp.backend.firstImpl<AioPointService>().newInstance(Session.contextFor(principalId = uuid4(), roleId = uuid4()))

    val spaceService : AioSpaceApi
        get() = serverApp.backend.firstImpl<AioSpaceService>().newInstance(Session.contextFor(principalId = uuid4(), roleId = uuid4()))

    val deviceService : AioDeviceApi
        get() = serverApp.backend.firstImpl<AioDeviceService>().newInstance(Session.contextFor(principalId = uuid4(), roleId = uuid4()))

    companion object {

        @OptIn(ExperimentalCoroutinesApi::class)
        fun serverTest(testPath: Path, timeout: Duration = 10.seconds, testFun: suspend TestSupport.() -> Unit) =

            runTest(timeout = timeout) {
                with(TestSupport()) {

                    serverApp = testServer {
                        module { UtilServerModule() }
                        module { ValueServerModule("general", { ensureLoggedIn() }, FilePersistence(testPath.resolve("values").ensure(), 2)) }
                        module { NoAuthServerModule() }
                        module { IotServerModule() }
                    }.start(
                        serverTransport
                    )

                    val driverDispatcher = Dispatchers.Unconfined
                    val driverScope = CoroutineScope(driverDispatcher)

                    clientBackend = backend(clientTransport, dispatcher = driverDispatcher, scope = driverScope) {
                        worker {
                            AvValueWorker("driver")
                        }

                        service {
                            AvValueClientService()
                        }
                    }

                    withContext(serverApp.backend.dispatcher) {
                        testFun()
                    }

                    serverApp.backend.stop()
                    clientBackend.stop()
                }
            }
    }
}