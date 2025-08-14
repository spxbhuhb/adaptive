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
    var now : Instant = now()
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
        var next = vmNowMicro() + 1_000_000L // schedule next tick 1 s from now

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
                // we overran; skip missed seconds to keep phase steady
                val missed = (-remaining) / 1_000_000L + 1
                next += missed * 1_000_000L
                continue
            }

            // advance exactly one second per loop (steady cadence)
            next += 1_000_000L
        }
    }
}