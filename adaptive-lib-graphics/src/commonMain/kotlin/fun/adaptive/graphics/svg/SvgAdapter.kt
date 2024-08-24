/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.graphics.svg

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.graphics.canvas.platform.ActualCanvas
import `fun`.adaptive.utility.vmNowMicro
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class SvgAdapter(
    val parentAdapter : AdaptiveAdapter,
    override val rootContainer : ActualCanvas
) : AdaptiveAdapter {

    override val fragmentFactory = parentAdapter.fragmentFactory

    override lateinit var rootFragment: AdaptiveFragment

    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Default

    override var trace = parentAdapter.trace

    override val startedAt = vmNowMicro()

    override fun newId() = parentAdapter.newId()

    fun draw() {
        rootContainer.startDraw()
        (rootFragment as SvgFragment<*>).draw()
        rootContainer.endDraw()
    }
}