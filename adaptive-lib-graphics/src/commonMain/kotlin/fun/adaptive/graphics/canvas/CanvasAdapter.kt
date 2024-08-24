/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.graphics.canvas

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.graphics.canvas.platform.ActualCanvas
import `fun`.adaptive.utility.alsoIfInstance
import `fun`.adaptive.utility.vmNowMicro
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class CanvasAdapter(
    val parentAdapter : AdaptiveAdapter,
    override val rootContainer: ActualCanvas,
    override var rootFragment : AdaptiveFragment
) : AdaptiveAdapter {

    override val fragmentFactory = parentAdapter.fragmentFactory

    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Default

    override var trace = parentAdapter.trace

    override val startedAt = vmNowMicro()

    override fun newId() = parentAdapter.newId()

    val drawItems = mutableListOf<CanvasFragment>()

    override fun addActualRoot(fragment: AdaptiveFragment) {
        traceAddActual(fragment)
        fragment.alsoIfInstance<CanvasFragment> {
            drawItems += it
        }
    }

    override fun removeActualRoot(fragment: AdaptiveFragment) {
        traceRemoveActual(fragment)
        fragment.alsoIfInstance<CanvasFragment> {
            drawItems.removeAt(drawItems.indexOfFirst { it.id == fragment.id })
        }
    }

    fun draw() {
        trace("draw", "drawItems.size = ${drawItems.size}")
        rootContainer.startDraw()
        drawItems.forEach { it.draw() }
        rootContainer.endDraw()
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