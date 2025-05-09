package `fun`.adaptive.iot.driver.test.task

import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.iot.driver.announcement.AdaControllerCommissioned
import `fun`.adaptive.iot.driver.backend.protocol.AbstractProtocolWorker
import `fun`.adaptive.iot.driver.backend.task.DriverTask
import `fun`.adaptive.iot.point.AioPointSpec
import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.value.AvValue
import kotlinx.coroutines.channels.Channel

class CommissionController<NT : AioDeviceSpec, CT : AioDeviceSpec, PT : AioPointSpec>(
    val item: AvValue<CT>
) : DriverTask<NT, CT, PT>() {

    override val responseChannel = Channel<Any>()

    override fun execute(worker: AbstractProtocolWorker<NT, CT, PT>) {

        val network = checkNotNull(worker.networkOrNull)
        worker += AdaControllerCommissioned(uuid7(), network.uuid, item)

    }

}