/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.adapter

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.structural.AdaptiveAnonymous
import hu.simplexion.adaptive.ui.common.instruction.*
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

    inline fun measure(widthFun: (Float, Point, Size) -> Float, heightFun: (Float, Point, Size) -> Float): Size {
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

        return  Size(width, height).also { measuredSize = it }
    }

    fun calcPrefixAndGap(horizontal: Boolean): Pair<Float, Float> {
        val instructedGap = renderData.gap ?: 0f

        if (items.isEmpty()) return (0f to instructedGap)

        val size = renderData.layoutFrame.size
        val available = if (horizontal) size.width else size.height
        val used = calcUsedSpace(horizontal)

        val gapCount = items.size - 1
        val usedByInstructedGap = gapCount * instructedGap
        val remaining = available - (used + usedByInstructedGap)

        if (remaining <= 0) return (0f to instructedGap)

        return when (renderData.justifyContent) {
            JustifyContent.Start -> (0f to instructedGap)
            JustifyContent.Center -> ((remaining / 2) to instructedGap)
            JustifyContent.End -> (remaining to instructedGap)
            null -> (0f to instructedGap)
        }
    }

    fun calcUsedSpace(horizontal: Boolean): Float {

        var usedSpace = 0f

        for (item in items) {
            val measuredSize = checkNotNull(item.measuredSize) { "measured size should not be null, container:$this item:$item)" }
            if (horizontal) {
                usedSpace += measuredSize.width
            } else {
                usedSpace += measuredSize.height
            }
        }

        return usedSpace
    }

    fun calcAlign(alignItems: AlignItems?, alignSelf: AlignSelf?, availableSpace: Float, usedSpace: Float) =
        if (alignSelf != null) {
            when (alignSelf) {
                AlignSelf.Center -> (availableSpace - usedSpace) / 2
                AlignSelf.Start -> 0f
                AlignSelf.End -> availableSpace - usedSpace
            }
        } else {
            when (alignItems) {
                AlignItems.Center -> (availableSpace - usedSpace) / 2
                AlignItems.Start -> 0f
                AlignItems.End -> availableSpace - usedSpace
                null -> 0f
            }
        }

    /**
     * Common layout func for row and column layouts.
     */
    fun layoutStack(horizontal: Boolean, autoSizing : Boolean) {

        if (autoSizing) {
            for (item in items) {
                item.layout(Frame.NaF)
            }
            return
        }

        val (prefix, gap) = calcPrefixAndGap(horizontal)

        var offset = prefix
        val stackSize = renderData.layoutFrame.size
        val spaceForAlign = if (horizontal) stackSize.height else stackSize.width
        val alignItems = renderData.alignItems

        for (item in items) {
            val renderData = item.renderData

            val measuredSize = checkNotNull(item.measuredSize) { "measured size should not be null, container:$this item:$item)" }
            val alignSelf = renderData.alignSelf

            val frame = if (horizontal) {
                val top = calcAlign(alignItems, alignSelf, spaceForAlign, measuredSize.height)
                Frame(Point(top, offset), measuredSize)
            } else {
                val left = calcAlign(alignItems, alignSelf, spaceForAlign, measuredSize.width)
                Frame(Point(left, offset), measuredSize)
            }

            item.layout(frame)

            offset += gap + (if (horizontal) measuredSize.width else measuredSize.height)
        }
    }
}