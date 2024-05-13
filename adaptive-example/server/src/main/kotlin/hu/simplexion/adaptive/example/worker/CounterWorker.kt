package hu.simplexion.adaptive.example.worker

import hu.simplexion.adaptive.server.builtin.WorkerImpl
import kotlinx.coroutines.delay
import java.util.concurrent.atomic.AtomicInteger

class CounterWorker : WorkerImpl<CounterWorker> {

    val counter = AtomicInteger(0)

    override suspend fun run() {
        while (counter.get() < 3) {
            println("counter: ${counter.get()}")
            delay(1000)
        }
        adapter?.wait = false
    }

}