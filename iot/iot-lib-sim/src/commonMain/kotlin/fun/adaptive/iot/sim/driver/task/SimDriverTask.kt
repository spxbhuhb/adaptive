package `fun`.adaptive.iot.sim.driver.task

import `fun`.adaptive.iot.sim.driver.SimProtocolWorker
import kotlinx.coroutines.channels.Channel

abstract class SimDriverTask(
    val taskFun: () -> Unit,
    val responseChannel: Channel<Any>
) {

    abstract fun dispatch(worker : SimProtocolWorker)

    abstract fun execute()

}