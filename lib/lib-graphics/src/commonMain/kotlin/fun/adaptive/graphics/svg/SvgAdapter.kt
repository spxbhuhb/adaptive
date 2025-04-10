/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.graphics.svg

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.graphics.canvas.platform.ActualCanvas
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.ui.DensityIndependentAdapter
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.SPixel
import `fun`.adaptive.utility.vmNowMicro
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

class SvgAdapter(
    val parentAdapter: DensityIndependentAdapter,
    override val rootContainer: ActualCanvas,
) : DensityIndependentAdapter() {

    override val fragmentFactory = parentAdapter.fragmentFactory

    override val transport: ServiceCallTransport
        get() = parentAdapter.transport

    override lateinit var rootFragment: AdaptiveFragment

    override val dispatcher: CoroutineDispatcher
        get() = parentAdapter.dispatcher

    override val scope: CoroutineScope
        get() = parentAdapter.scope

    override var trace = parentAdapter.trace

    override val startedAt = vmNowMicro()

    override fun newId() = parentAdapter.newId()

    fun draw() {
        rootContainer.draw {
            rootContainer.save(0)
            (rootFragment as SvgFragment<*>).draw()
            rootContainer.restore(0)
        }
    }

    override fun toPx(dPixel: DPixel): Double =
        parentAdapter.toPx(dPixel)

    override fun toDp(value: Double): DPixel =
        parentAdapter.toDp(value)

    override fun toPx(sPixel: SPixel): Double =
        parentAdapter.toPx(sPixel)

}