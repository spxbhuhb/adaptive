package `fun`.adaptive.utility

import `fun`.adaptive.general.ObservableListener
import `fun`.adaptive.general.SelfObservable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Clock.System.now
import kotlin.time.Instant

class ObservableInstant(
    val scope : CoroutineScope,
    var now : Instant = now(),
    val intervalMicros: Long = 1_000_000L // default 1s
) : SelfObservable<ObservableInstant>() {

    var job : Job? = null

    override fun addListener(listener : ObservableListener<ObservableInstant>) {
        super.addListener(listener)
        if (job == null) {
            job = scope.launch { run() }
        }
    }

    override fun removeListener(listener : ObservableListener<ObservableInstant>) {
        super.removeListener(listener)
        if (listeners.isEmpty()) {
            job?.cancel()
            job = null
        }
    }

    suspend fun run() {
        var next = vmNowMicro() + intervalMicros

        while (currentCoroutineContext().isActive) {
            now = now()
            notifyListeners()

            val t = vmNowMicro()

            val remaining = next - t

            if (remaining > 0) {
                // Round up to ensure we don’t systematically undershoot
                val ms = (remaining + 999L) / 1000L
                delay(ms)
            } else {
                // we overran; skip missed intervals to keep phase steady
                val missed = (-remaining) / intervalMicros + 1
                next += missed * intervalMicros
                continue
            }

            // advance exactly one interval per loop (steady cadence)
            next += intervalMicros
        }
    }
}