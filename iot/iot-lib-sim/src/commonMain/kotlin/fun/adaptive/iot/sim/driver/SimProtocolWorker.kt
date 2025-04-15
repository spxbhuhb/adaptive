package `fun`.adaptive.iot.sim.driver

import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.iot.sim.driver.task.SimDriverCommTask
import `fun`.adaptive.iot.sim.driver.task.SimDriverTask
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class SimProtocolWorker : WorkerImpl<SimProtocolWorker> {

    private val taskQueue = Channel<SimDriverTask>()

    override suspend fun run() {
        scope.launch { commMain() }
        for (task in taskQueue) {
            task.dispatch(this)
        }
    }

    operator fun plusAssign(task: SimDriverTask) {
        taskQueue.trySend(task).getOrThrow()
    }

    // --------------------------------------------------------------------------------
    // Comm
    // --------------------------------------------------------------------------------

    private val commLock = getLock()

    private val commQueue = mutableListOf<SimDriverCommTask>()

    private val commWaiters = mutableListOf<Continuation<Unit>>()

    internal fun enqueueCommTask(newTask: SimDriverCommTask) {
        commLock.use {

            for ((index, task) in commQueue.withIndex()) {
                val mergedTask = task.mergeWith(newTask)
                if (mergedTask == null) continue

                commQueue[index] = mergedTask
                return@use
            }

            commQueue.add(newTask)
            commWaiters.forEach { it.resume(Unit) }
            commWaiters.clear()
        }
    }

    internal suspend fun dequeueCommTask() : SimDriverCommTask? {
        while (isActive) {

            suspendCancellableCoroutine<Unit> {
                commLock.use {
                    if (commQueue.isEmpty()) commWaiters.add(it) else it.resume(Unit)
                }
            }

            val task = commLock.use { commQueue.removeFirstOrNull() }

            if (task != null) return task
        }

        return null
    }

    suspend fun commMain() {
        while (isActive) {
            dequeueCommTask()?.execute()
        }
    }

}