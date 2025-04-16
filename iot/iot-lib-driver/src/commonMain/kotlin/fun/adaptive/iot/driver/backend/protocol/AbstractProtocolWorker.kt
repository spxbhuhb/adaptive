package `fun`.adaptive.iot.driver.backend.protocol

import `fun`.adaptive.adat.encodeToJsonByteArray
import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.iot.driver.announcement.AioDriverAnnouncement
import `fun`.adaptive.iot.driver.announcement.AioDriverAnnouncementWrapper
import `fun`.adaptive.iot.driver.backend.AioDriverWorker
import `fun`.adaptive.iot.driver.backend.task.DriverTask
import `fun`.adaptive.iot.point.AioPointSpec
import `fun`.adaptive.value.item.AvItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

abstract class AbstractProtocolWorker<NT : AioDeviceSpec, CT : AioDeviceSpec, PT : AioPointSpec> : WorkerImpl<AbstractProtocolWorker<*, *, *>> {

    protected val taskQueue = Channel<DriverTask<NT, CT, PT>>()

    abstract val comm: AbstractProtocolComm<NT, CT, PT>

    var networkOrNull: AvItem<NT>? = null

    val driverWorker by worker<AioDriverWorker<*, *, *>>()

    override suspend fun run() {
        scope.launch { comm.main() }

        for (task in taskQueue) {
            task.dispatch(this)
        }
    }

    operator fun plusAssign(task: DriverTask<NT, CT, PT>) {
        taskQueue.trySend(task).getOrThrow()
    }

    operator fun plusAssign(announcement: AioDriverAnnouncement) {
        driverWorker.announcementQueue.enqueue(AioDriverAnnouncementWrapper(announcement).encodeToJsonByteArray())
    }

}