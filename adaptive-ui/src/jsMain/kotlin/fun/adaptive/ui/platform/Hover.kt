package `fun`.adaptive.ui.platform

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
        // FIXME check the effect of remove batch on hover
    }

    val enterHandler = { _: Any ->
        latestValue = true
        setDirty()
    }

    val leaveHandler = { _: Any ->
        latestValue = false
        setDirty()
    }

    fun AdaptiveFragment.receiver(): HTMLElement? =
        if (this is AbstractAuiFragment<*> && this.receiver is HTMLElement) this.receiver as HTMLElement? else null

    override fun addActual(fragment: AdaptiveFragment) {
        println("addActual")
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