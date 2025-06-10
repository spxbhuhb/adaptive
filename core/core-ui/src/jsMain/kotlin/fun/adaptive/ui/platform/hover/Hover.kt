package `fun`.adaptive.ui.platform.hover

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.AdaptiveProducer
import `fun`.adaptive.ui.AbstractAuiFragment
import org.w3c.dom.HTMLElement

class Hover(
    override val binding: AdaptiveStateVariableBinding<Boolean>
) : AdaptiveProducer<Boolean> {

    override var latestValue: Boolean? = false

    override val actual: Boolean
        get() = true

    override fun start() {

    }

    override fun stop() {
        // FIXME check the effect of remove in batch on hover
    }

    val enterHandler = { _: Any ->
        if (latestValue != true) {
            latestValue = true
            setDirtyBatch()
        }
    }

    val leaveHandler = { _: Any ->
        if (latestValue != false) {
            latestValue = false
            setDirtyBatch()
        }
    }

    fun AdaptiveFragment.receiver(): HTMLElement? =
        if (this is AbstractAuiFragment<*> && this.receiver is HTMLElement) this.receiver as HTMLElement? else null

    override fun addActual(fragment: AdaptiveFragment) {
        val receiver = fragment.receiver() ?: return
        receiver.addEventListener("mouseenter", enterHandler)
        receiver.addEventListener("mouseleave", leaveHandler)
    }

    override fun removeActual(fragment: AdaptiveFragment) {
        val receiver = fragment.receiver() ?: return
        receiver.removeEventListener("mouseenter", enterHandler)
        receiver.removeEventListener("mouseleave", leaveHandler)
    }
}