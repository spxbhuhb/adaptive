package hu.simplexion.z2.adaptive.worker

import hu.simplexion.z2.adaptive.AdaptiveSupportFunction
import hu.simplexion.z2.adaptive.binding.AdaptiveStateVariableBinding
import kotlinx.coroutines.*
import kotlin.time.Duration

class AdaptivePoll<VT>(
    val binding: AdaptiveStateVariableBinding<VT>,
    val interval: Duration
) : AdaptiveWorker {

    val pollFunction = AdaptiveSupportFunction(
        binding.sourceFragment,
        binding.sourceFragment,
        binding.supportFunctionIndex
    )

    var scope: CoroutineScope? = null

    override fun replaces(other: AdaptiveWorker): Boolean =
        other is AdaptivePoll<*> && other.binding == this.binding

    override fun start() {
        scope = CoroutineScope(binding.sourceFragment.adapter.dispatcher).apply {
            launch {
                while (isActive) {

                    try {
                        binding.setValue(pollFunction.invokeSuspend(), false) // TODO check provider call in poll
                        delay(interval)

                    } catch (e: AdaptiveWorkerCancel) {
                        cancel()
                        break
                    }
                }
            }
        }
    }

    override fun stop() {
        checkNotNull(scope).cancel()
        scope = null
    }

    override fun toString(): String {
        return "AdaptivePoll($binding, $interval)"
    }

}