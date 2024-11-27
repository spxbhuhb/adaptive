/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.graphics.canvas

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.graphics.canvas.fragment.CanvasLoop
import `fun`.adaptive.graphics.canvas.fragment.CanvasSelect
import `fun`.adaptive.graphics.canvas.platform.ActualCanvas
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.utility.alsoIfInstance
import `fun`.adaptive.utility.vmNowMicro
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

class CanvasAdapter(
    val parentAdapter : AdaptiveAdapter,
    override val rootContainer: ActualCanvas,
    override var rootFragment : AdaptiveFragment,
) : AdaptiveAdapter {

    override val fragmentFactory = parentAdapter.fragmentFactory

    override var trace = parentAdapter.trace

    override val startedAt = vmNowMicro()

    override val transport: ServiceCallTransport
        get() = parentAdapter.transport

    override val dispatcher: CoroutineDispatcher
        get() = parentAdapter.dispatcher

    override val scope: CoroutineScope
        get() = parentAdapter.scope

    override fun newId() = parentAdapter.newId()

    override fun newSelect(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
        CanvasSelect(this, parent, index)

    override fun newLoop(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
        CanvasLoop(this, parent, index)

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
        rootContainer.clear()
        rootContainer.draw {
            drawItems.forEach { it.draw() }
        }
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