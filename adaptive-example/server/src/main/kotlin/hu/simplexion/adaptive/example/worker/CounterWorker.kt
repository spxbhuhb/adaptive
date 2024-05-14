package hu.simplexion.adaptive.example.worker

import hu.simplexion.adaptive.server.builtin.WorkerImpl
import hu.simplexion.adaptive.server.setting.dsl.setting
import hu.simplexion.adaptive.utility.vmNowSecond
import kotlinx.coroutines.delay
import java.util.concurrent.atomic.AtomicInteger

class CounterWorker : WorkerImpl<CounterWorker> {

    val counter = AtomicInteger(0)

    val limit by setting<Int> { "COUNTER_LIMIT" }
    val idleInterval by setting<Int> { "COUNTER_IDLE_INTERVAL" }

    override suspend fun run() {

        var lastValue = 0
        var lastChange = vmNowSecond()
        var lastReport = vmNowSecond()

        while (lastValue < limit) {

            val currentValue = counter.get()

            val now = vmNowSecond()

            if (currentValue != lastValue) {
                logger.info("counter change: $lastValue -> $currentValue")
                lastChange = now
                lastValue = currentValue
            }

            if (now - idleInterval >= lastChange && now - idleInterval >= lastReport) {
                logger.info("no change in the last $idleInterval seconds")
                lastReport = now
            }

            delay(1000)
        }

        adapter?.wait = false
    }

}