package `fun`.adaptive.iot.driver.backend.protocol

import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.iot.point.AioPointSpec
import `fun`.adaptive.iot.driver.backend.task.DriverCommTask
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import kotlinx.coroutines.isActive
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class FifoProtocolComm<NT : AioDeviceSpec, CT : AioDeviceSpec, PT : AioPointSpec>(
    override val worker: AbstractProtocolWorker<NT,CT,PT>
) : AbstractProtocolComm<NT,CT,PT>() {

    private val isActive
        get() = worker.scope.isActive

    private val commLock = getLock()

    private val commQueue = mutableListOf<DriverCommTask<NT,CT,PT>>()

    private val commWaiters = mutableListOf<Continuation<Unit>>()

    override suspend fun main() {
        try {
            while (isActive) {
                val task = dequeueCommTask() ?: continue
                try {
                    task.responseChannel.send(task.execute(worker))
                } catch (t: Throwable) {
                    task.responseChannel.close(t)
                }
            }
        } finally {
            resumeWaiters()
        }
    }

    override fun enqueueCommTask(newTask: DriverCommTask<NT,CT,PT>) {
        commLock.use {

            for ((index, task) in commQueue.withIndex()) {
                val mergedTask = task.mergeWith(newTask)
                if (mergedTask == null) continue

                commQueue[index] = mergedTask
                return@use
            }

            commQueue.add(newTask)
            resumeWaiters()
        }
    }

    private fun resumeWaiters() {
        commWaiters.forEach { it.resume(Unit) }
        commWaiters.clear()
    }

    internal suspend fun dequeueCommTask() : DriverCommTask<NT,CT,PT>? {
        while (isActive) {

            suspendCancellableCoroutine<Unit> {
                commLock.use {
                    if (commQueue.isEmpty()) commWaiters.add(it) else it.resume(Unit)
                }
            }

            if ( ! isActive) break

            val task = commLock.use { commQueue.removeFirstOrNull() }

            if (task != null) return task
        }

        return null
    }

}