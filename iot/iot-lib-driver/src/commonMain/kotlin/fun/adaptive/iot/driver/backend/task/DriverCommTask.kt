package `fun`.adaptive.iot.driver.backend.task

import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.iot.driver.backend.protocol.AbstractProtocolWorker
import `fun`.adaptive.iot.point.AioPointSpec

abstract class DriverCommTask<NT : AioDeviceSpec, CT : AioDeviceSpec, PT : AioPointSpec> : DriverTask<NT, CT, PT>() {

    override fun dispatch(worker: AbstractProtocolWorker<NT,CT,PT>) {
        worker.comm.enqueueCommTask(this)
    }

    abstract fun mergeWith(other: DriverCommTask<NT,CT,PT>): DriverCommTask<NT,CT,PT>?

}