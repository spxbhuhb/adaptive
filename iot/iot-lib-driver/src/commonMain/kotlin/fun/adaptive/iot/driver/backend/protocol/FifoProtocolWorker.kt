package `fun`.adaptive.iot.driver.backend.protocol

import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.iot.point.AioPointSpec

class FifoProtocolWorker<NT : AioDeviceSpec, CT : AioDeviceSpec, PT : AioPointSpec>: AbstractProtocolWorker<NT,CT,PT>() {

    override val comm = FifoProtocolComm<NT,CT,PT>(this)

}