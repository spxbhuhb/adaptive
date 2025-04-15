package `fun`.adaptive.iot.sim.driver.task

import `fun`.adaptive.iot.sim.driver.SimProtocolWorker
import kotlinx.coroutines.channels.Channel

abstract class SimDriverCommTask(
    val taskFun: () -> Unit,
    val responseChannel: Channel<Any>
) {

    fun dispatch(worker : SimProtocolWorker) {
        worker.enqueueCommTask(this)
    }

    abstract fun mergeWith(task: SimDriverCommTask) : SimDriverCommTask?

    abstract fun execute()

}