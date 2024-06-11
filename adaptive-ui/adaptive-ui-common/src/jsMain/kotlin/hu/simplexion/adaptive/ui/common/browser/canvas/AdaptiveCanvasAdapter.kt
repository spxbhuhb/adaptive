/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.canvas

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.utility.alsoIfInstance
import hu.simplexion.adaptive.utility.vmNowMicro
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.w3c.dom.*

class AdaptiveCanvasAdapter(
    val parentAdapter : AdaptiveAdapter,
    override val rootContainer: HTMLCanvasElement,
    override var rootFragment : AdaptiveFragment
) : AdaptiveAdapter {

    override val fragmentFactory = CanvasFragmentFactory

    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Default

    override var trace = parentAdapter.trace

    override val startedAt = vmNowMicro()

    override fun newId() = parentAdapter.newId()

    val drawItems = mutableListOf<AdaptiveCanvasFragment>()

    override fun addActualRoot(fragment: AdaptiveFragment) {
        traceAddActual(fragment)
        fragment.alsoIfInstance<AdaptiveCanvasFragment> {
            drawItems += it
        }
    }

    override fun removeActualRoot(fragment: AdaptiveFragment) {
        traceRemoveActual(fragment)
        fragment.alsoIfInstance<AdaptiveCanvasFragment> {
            drawItems.removeAt(drawItems.indexOfFirst { it.id == fragment.id })
        }
    }

    fun draw() {
        trace("draw", "drawItems.size = ${drawItems.size}")
        val context = rootContainer.getContext("2d") as CanvasRenderingContext2D
        drawItems.forEach { it.draw(context) }
    }

    fun traceAddActual(fragment: AdaptiveFragment) {
        if (trace.isEmpty()) return
        trace("addActual", "fragment: $fragment")
    }

    fun traceRemoveActual(fragment: AdaptiveFragment) {
        if (trace.isEmpty()) return
        trace("removeActual", "fragment: $fragment")
    }
}