package `fun`.adaptive.iot.driver

import `fun`.adaptive.adat.decodeFromJson
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.iot.device.DeviceMarkers
import `fun`.adaptive.iot.app.WsItemTypes
import `fun`.adaptive.iot.driver.announcement.AdaNetworkCommissioned
import `fun`.adaptive.iot.driver.announcement.AioDriverAnnouncementWrapper
import `fun`.adaptive.iot.driver.api.AioDriverApi
import `fun`.adaptive.iot.driver.backend.AioDriverService
import `fun`.adaptive.iot.driver.backend.AioDriverWorker
import `fun`.adaptive.iot.driver.backend.protocol.FifoProtocolWorker
import `fun`.adaptive.iot.driver.request.AdrCommissionNetwork
import `fun`.adaptive.iot.driver.request.AioDriverRequest
import `fun`.adaptive.iot.driver.test.TestControllerSpec
import `fun`.adaptive.iot.driver.test.TestNetworkSpec
import `fun`.adaptive.iot.driver.test.TestPointSpec
import `fun`.adaptive.iot.driver.test.TestProtocolPlugin
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.service.testing.DirectServiceTransport
import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.utility.ensure
import `fun`.adaptive.utility.resolve
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.wireformat.api.Json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlinx.io.files.Path
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class TestSupport {

    val nodeTransport = DirectServiceTransport(name = "node", wireFormatProvider = Json) // .also { it.trace = true; it.transportLog.enableFine() }
    val driverTransport = DirectServiceTransport(name = "driver", wireFormatProvider = Json) // .also { it.trace = true; it.transportLog.enableFine() }

    init {
        nodeTransport.peerTransport = driverTransport
        driverTransport.peerTransport = nodeTransport
    }

    lateinit var driverBackend: BackendAdapter

    lateinit var nodeBackend: BackendAdapter

    val driverValueWorker
        get() = driverBackend.firstImpl<AvValueWorker>()

    val driverWorker
        get() = driverBackend.firstImpl<AioDriverWorker<*, *, *>>()

    val driverService by lazy { getService<AioDriverApi>(nodeTransport) }

    fun newNetwork(spec : () -> TestNetworkSpec) =
        AvValue(
            name = "test-network",
            type = WsItemTypes.WSIT_DEVICE,
            friendlyId = "test-network",
            markersOrNull = mapOf(DeviceMarkers.NETWORK to null),
            spec = spec()
        )

    fun newController(networkId: AvValueId, spec: () -> TestControllerSpec) =
        AvValue(
            name = "test-controller",
            type = WsItemTypes.WSIT_DEVICE,
            friendlyId = "test-controller",
            parentId = networkId,
            markersOrNull = mapOf(DeviceMarkers.CONTROLLER to null),
            spec = spec()
        )

    suspend fun AioDriverRequest.process() {
        driverService.process(this)
    }

    suspend fun commissionTestNetwork(): AvValue<TestNetworkSpec> {
        val network = newNetwork { TestNetworkSpec() }
        AdrCommissionNetwork(uuid7(), network.uuid, network).process()
        getAnnouncement<AdaNetworkCommissioned<TestNetworkSpec>>()
        return network
    }

    val isAnnouncementQueueEmpty: Boolean
        get() = driverWorker.announcementQueue.isEmpty

    inline fun <reified T> getAnnouncement(): T {
        val announcementData = driverWorker.announcementQueue.dequeueOrNull()
        assertNotNull(announcementData)

        val wrapper = AioDriverAnnouncementWrapper.decodeFromJson(announcementData)
        val announcement = wrapper.announcement

        assertIs<T>(announcement)

        return announcement
    }

    companion object {

        @OptIn(ExperimentalCoroutinesApi::class)
        fun driverTest(testPath : Path, timeout: Duration = 10.seconds, testFun: suspend TestSupport.() -> Unit) =

            runTest(timeout = timeout) {
                with(TestSupport()) {

                    // Switch to a coroutine context that is NOT a test context. The test context
                    // skips delays which wreaks havoc with service call timeouts that depend on
                    // delays actually working.

                    val driverDispatcher = Dispatchers.Unconfined
                    val driverScope = CoroutineScope(driverDispatcher)

                    val nodeDispatcher = Dispatchers.Unconfined
                    val nodeScope = CoroutineScope(nodeDispatcher)

                    driverBackend = backend(driverTransport, dispatcher = driverDispatcher, scope = driverScope) {
                        worker {
                            AvValueWorker("driver")
                        }

                        service {
                            AioDriverService<TestNetworkSpec, TestControllerSpec, TestPointSpec>()
                        }

                        worker {
                            AioDriverWorker(
                                TestProtocolPlugin(),
                                testPath.resolve("announcements").ensure(),
                                TestNetworkSpec::class,
                                TestControllerSpec::class,
                                TestPointSpec::class
                            )
                        }

                        worker {
                            FifoProtocolWorker<TestNetworkSpec, TestControllerSpec, TestPointSpec>()
                        }
                    }

                    nodeBackend = backend(nodeTransport, dispatcher = nodeDispatcher, scope = nodeScope) {
                        worker { AvValueWorker("node") }
                    }

                    withContext(nodeDispatcher) {
                        testFun()
                    }

                    nodeBackend.stop()
                    driverBackend.stop()
                }
            }
    }
}