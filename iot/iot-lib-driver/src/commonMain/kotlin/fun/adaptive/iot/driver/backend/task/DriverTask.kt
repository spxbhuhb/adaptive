package `fun`.adaptive.iot.driver.backend.task

import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.iot.driver.backend.protocol.AbstractProtocolWorker
import `fun`.adaptive.iot.point.AioPointSpec
import kotlinx.coroutines.channels.Channel

abstract class DriverTask<NT : AioDeviceSpec, CT : AioDeviceSpec, PT : AioPointSpec> {

    abstract val responseChannel: Channel<Any>

    open fun dispatch(worker: AbstractProtocolWorker<NT, CT, PT>) {
        execute(worker)
    }

    abstract fun execute(worker: AbstractProtocolWorker<NT,CT,PT>)

}