package `fun`.adaptive.iot.driver.backend

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.iot.driver.api.AioDriverApi
import `fun`.adaptive.iot.driver.request.AdrCommissionController
import `fun`.adaptive.iot.driver.request.AdrCommissionNetwork
import `fun`.adaptive.iot.driver.request.AdrCommissionPoint
import `fun`.adaptive.iot.driver.request.AdrDisable
import `fun`.adaptive.iot.driver.request.AdrEnable
import `fun`.adaptive.iot.driver.request.AdrPing
import `fun`.adaptive.iot.driver.request.AdrReadPoint
import `fun`.adaptive.iot.driver.request.AdrStartControllerDiscovery
import `fun`.adaptive.iot.driver.request.AdrStartTrace
import `fun`.adaptive.iot.driver.request.AdrStopTrace
import `fun`.adaptive.iot.driver.request.AdrWritePoint
import `fun`.adaptive.iot.driver.request.AioDriverRequest
import `fun`.adaptive.iot.point.AioPointSpec

class AioDriverService<NT : AioDeviceSpec, CT : AioDeviceSpec, PT : AioPointSpec> : ServiceImpl<AioDriverService<NT, CT, PT>>(), AioDriverApi {

    companion object {
        lateinit var worker : AioDriverWorker<*,*,*>
    }

    override fun mount() {
        worker = safeAdapter.firstImpl<AioDriverWorker<*,*,*>>()
    }

    override suspend fun process(request: AioDriverRequest) =
        when (request) {
            is AdrWritePoint<*> -> worker.writePoint(request)
            is AdrReadPoint -> worker.readPoint(request)
            is AdrPing -> worker.ping(request)

            is AdrStartControllerDiscovery -> worker.startControllerDiscovery(request)
            is AdrCommissionNetwork<*> -> worker.commissionNetwork(request)
            is AdrCommissionController<*> -> worker.commissionController(request)
            is AdrCommissionPoint<*> -> worker.commissionPoint(request)

            is AdrDisable -> worker.disable(request)
            is AdrEnable -> worker.enable(request)

            is AdrStartTrace -> worker.startTrace(request)
            is AdrStopTrace -> worker.stopTrace(request)

            else -> throw IllegalArgumentException("unknown request type: ${request.adatCompanion.wireFormatName}")
        }

}