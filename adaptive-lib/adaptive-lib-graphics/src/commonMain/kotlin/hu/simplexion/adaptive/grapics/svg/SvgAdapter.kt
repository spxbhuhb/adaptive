/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.grapics.svg

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.grapics.canvas.ActualCanvas
import hu.simplexion.adaptive.utility.alsoIfInstance
import hu.simplexion.adaptive.utility.vmNowMicro
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class SvgAdapter(
    val parentAdapter : AdaptiveAdapter?,
    override val rootContainer : ActualCanvas
) : AdaptiveAdapter {

    override val fragmentFactory = SvgFragmentFactory

    override lateinit var rootFragment: AdaptiveFragment

    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Default

    override var trace = parentAdapter?.trace ?: emptyArray()

    override val startedAt = vmNowMicro()

    var nextId = 1L

    override fun newId() = parentAdapter?.newId() ?: nextId++

    val drawItems = mutableListOf<SvgFragment>()

    override fun addActualRoot(fragment: AdaptiveFragment) {
        if (trace.isNotEmpty()) trace("addActual", "fragment: $fragment")

        fragment.alsoIfInstance<SvgFragment> {
            drawItems += it
        }
    }

    override fun removeActualRoot(fragment: AdaptiveFragment) {
        if (trace.isNotEmpty()) trace("removeActual", "fragment: $fragment")

        fragment.alsoIfInstance<SvgFragment> {
            drawItems.removeAt(drawItems.indexOfFirst { it.id == fragment.id })
        }
    }

    fun draw() {
        for (drawItem in drawItems) {
            drawItem.draw()
        }
    }
}