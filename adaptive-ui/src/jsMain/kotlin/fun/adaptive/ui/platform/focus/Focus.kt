package `fun`.adaptive.ui.platform.focus

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.AdaptiveProducer
import `fun`.adaptive.ui.AbstractAuiFragment
import org.w3c.dom.HTMLElement

class Focus(
    override val binding: AdaptiveStateVariableBinding<Boolean>
) : AdaptiveProducer<Boolean> {

    override var latestValue: Boolean? = false

    override val actual: Boolean
        get() = true

    override fun start() {

    }

    override fun stop() {
        // FIXME check the effect of remove in batch on focus
    }

    val focusHandler = { _: Any ->
        latestValue = true
        setDirtyBatch()
    }

    val blurHandler = { _: Any ->
        latestValue = false
        setDirtyBatch()
    }

    fun AdaptiveFragment.receiver(): HTMLElement? =
        if (this is AbstractAuiFragment<*> && this.receiver is HTMLElement) this.receiver as HTMLElement? else null

    override fun addActual(fragment: AdaptiveFragment) {
        val receiver = fragment.receiver() ?: return
        receiver.addEventListener("focus", focusHandler)
        receiver.addEventListener("blur", blurHandler)
    }

    override fun removeActual(fragment: AdaptiveFragment) {
        val receiver = fragment.receiver() ?: return
        receiver.removeEventListener("focus", focusHandler)
        receiver.removeEventListener("blur", blurHandler)
    }
}