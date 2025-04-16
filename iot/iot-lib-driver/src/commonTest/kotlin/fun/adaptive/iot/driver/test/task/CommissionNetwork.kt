package `fun`.adaptive.iot.driver.test.task

import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.iot.driver.announcement.AdaNetworkCommissioned
import `fun`.adaptive.iot.driver.backend.protocol.AbstractProtocolWorker
import `fun`.adaptive.iot.driver.backend.task.DriverTask
import `fun`.adaptive.iot.point.AioPointSpec
import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.value.item.AvItem
import kotlinx.coroutines.channels.Channel

class CommissionNetwork<NT : AioDeviceSpec, CT : AioDeviceSpec, PT : AioPointSpec>(
    val item: AvItem<NT>
) : DriverTask<NT, CT, PT>() {

    override val responseChannel = Channel<Any>()

    override fun execute(worker: AbstractProtocolWorker<NT, CT, PT>) {

        val original = worker.networkOrNull

        when {
            original == null -> if (item.spec.enabled) startNetwork()
            ! original.spec.enabled && item.spec.enabled -> startNetwork()
            original.spec.enabled && ! item.spec.enabled -> stopNetwork()
        }

        worker.networkOrNull = item

        worker += AdaNetworkCommissioned(uuid7(), item.uuid, item)

    }

    fun startNetwork() {

    }

    fun stopNetwork() {

    }

}