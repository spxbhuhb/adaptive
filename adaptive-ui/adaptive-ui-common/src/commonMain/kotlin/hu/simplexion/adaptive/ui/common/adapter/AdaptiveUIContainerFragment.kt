/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.adapter

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.structural.AdaptiveAnonymous
import hu.simplexion.adaptive.ui.common.instruction.Point
import hu.simplexion.adaptive.ui.common.instruction.Size
import hu.simplexion.adaptive.utility.alsoIfInstance

abstract class AdaptiveUIContainerFragment<CRT : RT, RT>(
    adapter: AdaptiveUIAdapter<CRT, RT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    instructionsIndex: Int,
    stateSize: Int
) : AdaptiveUIFragment<RT>(
    adapter, parent, declarationIndex, instructionsIndex, stateSize
) {

    @Suppress("LeakingThis") // instance construction should not perform any actions
    override val receiver: CRT = adapter.makeContainerReceiver(this)

    override val uiAdapter = adapter

    val items = mutableListOf<AdaptiveUIFragment<RT>>()

    val anchors = mutableMapOf<Long, CRT>()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment {
        return AdaptiveAnonymous(adapter, this, declarationIndex, 0, fragmentFactory(state.size - 1)).apply { create() }
    }

    override fun addActual(fragment: AdaptiveFragment, anchor: AdaptiveFragment?) {
        if (trace) trace("addActual", "fragment=$fragment, anchor=$anchor")

        fragment.alsoIfInstance<AdaptiveUIFragment<RT>> { itemFragment ->

            items += itemFragment

            if (anchor == null) {
                uiAdapter.addActual(receiver, itemFragment.receiver)
            } else {
                uiAdapter.addActual(
                    checkNotNull(anchors[anchor.id]) { "missing anchor: $anchor" },
                    itemFragment.receiver
                )
            }

            if (isMounted) {
                TODO("update layout")
            }
        }
    }

    override fun removeActual(fragment: AdaptiveFragment) {
        if (trace) trace("removeActual", "fragment=$fragment")

        fragment.alsoIfInstance<AdaptiveUIFragment<RT>> { itemFragment ->

            items.removeAt(items.indexOfFirst { it.id == fragment.id })
            uiAdapter.removeActual(itemFragment.receiver)

            if (isMounted) {
                TODO("update layout")
            }
        }
    }

    inline fun measure(widthFun : (Float, Point, Size) -> Float, heightFun : (Float, Point, Size) -> Float) : Size {
        traceMeasure()

        val instructedSize = renderData.instructedSize

        if (instructedSize != null) {
            for (item in items) {
                item.measure()
            }
            return instructedSize
        }

        var width = 0f
        var height = 0f

        for (item in items) {
            val size = checkNotNull(item.measure()) { "unable to measure, cannot get size of: $item" }

            val point = item.renderData.instructedPoint ?: Point.ORIGIN

            width = widthFun(width, point, size)
            height = heightFun(height, point, size)
        }

        return Size(width, height)
    }
}