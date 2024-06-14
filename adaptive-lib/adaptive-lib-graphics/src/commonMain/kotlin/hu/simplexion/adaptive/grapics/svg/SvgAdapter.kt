/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.grapics.svg

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.grapics.canvas.ActualCanvas
import hu.simplexion.adaptive.grapics.canvas.ActualPath
import hu.simplexion.adaptive.grapics.svg.fragment.SvgRoot
import hu.simplexion.adaptive.utility.alsoIfInstance
import hu.simplexion.adaptive.utility.vmNowMicro
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class SvgAdapter(
    val parentAdapter : AdaptiveAdapter?,
    override val rootContainer : ActualCanvas<*>
) : AdaptiveAdapter {

    override val fragmentFactory = SvgFragmentFactory

    override lateinit var rootFragment: AdaptiveFragment

    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Default

    override var trace = parentAdapter?.trace ?: emptyArray()

    override val startedAt = vmNowMicro()

    var nextId = 1L

    override fun newId() = parentAdapter?.newId() ?: nextId++

    fun draw() {
        rootContainer.startDraw()
        (rootFragment as SvgFragment<*>).draw()
        rootContainer.endDraw()
    }
}