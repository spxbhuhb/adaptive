package `fun`.adaptive.iot.driver.backend.protocol

import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.iot.point.AioPointSpec
import `fun`.adaptive.iot.driver.backend.task.DriverCommTask

abstract class AbstractProtocolComm<NT : AioDeviceSpec, CT : AioDeviceSpec, PT : AioPointSpec> {

    abstract val worker: AbstractProtocolWorker<NT, CT, PT>

    abstract suspend fun main()

    abstract fun enqueueCommTask(newTask: DriverCommTask<NT, CT, PT>)

}