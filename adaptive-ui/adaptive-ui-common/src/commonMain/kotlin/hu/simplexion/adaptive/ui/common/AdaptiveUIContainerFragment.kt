/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.internal.BoundFragmentFactory
import hu.simplexion.adaptive.foundation.structural.AdaptiveAnonymous
import hu.simplexion.adaptive.ui.common.instruction.*
import hu.simplexion.adaptive.ui.common.layout.RawFrame
import hu.simplexion.adaptive.ui.common.layout.RawPadding
import hu.simplexion.adaptive.ui.common.layout.RawPoint
import hu.simplexion.adaptive.ui.common.layout.RawSize
import hu.simplexion.adaptive.utility.alsoIfInstance
import hu.simplexion.adaptive.utility.checkIfInstance

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

    val content : BoundFragmentFactory
        get() = state[state.size - 1].checkIfInstance()

    /**
     * anchor fragment id : container receiver
     */
    val anchors = mutableMapOf<Long, CRT>()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment {
        if (declarationIndex != 0) invalidIndex(declarationIndex)
        return AdaptiveAnonymous(adapter, this, declarationIndex, 0, content).apply { create() }
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
                itemFragment.measure()
                layout(layoutFrame)
            }
        }
    }

    override fun removeActual(fragment: AdaptiveFragment) {
        if (trace) trace("removeActual", "fragment=$fragment")

        fragment.alsoIfInstance<AdaptiveUIFragment<RT>> { itemFragment ->

            items.removeAt(items.indexOfFirst { it.id == fragment.id })
            uiAdapter.removeActual(itemFragment.receiver)

            if (isMounted) {
                layout(layoutFrame)
            }
        }
    }

    override fun addAnchor(fragment: AdaptiveFragment, higherAnchor: AdaptiveFragment?) {
        if (trace) trace("addAnchor", "fragment=$fragment, higherAnchor=$higherAnchor")

        val anchorReceiver = uiAdapter.makeAnchorReceiver()
        anchors[fragment.id] = anchorReceiver

        if (higherAnchor == null) {
            uiAdapter.addActual(receiver, anchorReceiver)
        } else {
            uiAdapter.addActual(
                checkNotNull(anchors[higherAnchor.id]) { "missing higher anchor: $higherAnchor" },
                anchorReceiver
            )
        }
    }

    override fun removeAnchor(fragment: AdaptiveFragment) {
        if (trace) trace("removeAnchor", "fragment=$fragment")

        anchors.remove(fragment.id)?.let {
            uiAdapter.removeActual(it)
        }
    }

    fun measure(widthFun: (Float, RawPoint, RawSize) -> Float, heightFun: (Float, RawPoint, RawSize) -> Float): RawSize {
        val instructedSize = renderData.instructedSize?.let { RawSize(it, uiAdapter) }
        val padding = RawPadding(renderData.padding ?: Padding.ZERO, uiAdapter)

        if (instructedSize != null) {
            for (item in items) {
                item.measure()
            }
            traceMeasure()
            return instructedSize
        }

        var width = 0f
        var height = 0f

        for (item in items) {
            val size = checkNotNull(item.measure()) { "unable to measure, cannot get size of: $item" }

            val point = item.renderData.instructedPoint?.let { RawPoint(it, uiAdapter) } ?: RawPoint.ORIGIN

            width = widthFun(width, point, size)
            height = heightFun(height, point, size)
        }

        return RawSize(
            width + padding.left + padding.right,
            height + padding.top + padding.bottom
        ).also {
            measuredSize = it
            traceMeasure()
        }
    }

    fun calcPrefixAndGap(horizontal: Boolean): Pair<Float, Float> {
        val instructedGap = renderData.gap ?: 0f
        val padding = RawPadding(renderData.padding ?: Padding.ZERO, uiAdapter)

        if (items.isEmpty()) return (0f to instructedGap)

        val size = this.layoutFrame.size

        val available = if (horizontal) {
            size.width - padding.left - padding.right
        } else {
            size.height - padding.top - padding.bottom
        }

        val prefixPadding = if (horizontal) padding.left else padding.top

        val used = calcUsedSpace(horizontal)

        val gapCount = items.size - 1
        val usedByInstructedGap = gapCount * instructedGap
        val remaining = available - (used + usedByInstructedGap)

        if (remaining <= 0) return (0f to instructedGap)

        return when (renderData.justifyContent) {
            JustifyContent.Start -> (prefixPadding + 0f to instructedGap)
            JustifyContent.Center -> (prefixPadding + (remaining / 2) to instructedGap)
            JustifyContent.End -> (prefixPadding + remaining to instructedGap)
            null -> (prefixPadding + 0f to instructedGap)
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
     *
     * @param horizontal True the items should be next to each other (row), false if they should be below each other (column).
     */
    fun layoutStack(horizontal: Boolean, autoSizing: Boolean) {

        if (autoSizing) {
            for (item in items) {
                item.layout(RawFrame.NaF)
            }
            return
        }

        val padding = RawPadding(renderData.padding ?: Padding.ZERO, uiAdapter)

        val (prefix, gap) = calcPrefixAndGap(horizontal)

        var offset = prefix
        val stackSize = this.layoutFrame.size

        val spaceForAlign = if (horizontal) {
            stackSize.height - padding.top - padding.bottom
        } else {
            stackSize.width - padding.left - padding.right
        }

        val alignItems = renderData.alignItems

        for (item in items) {
            val renderData = item.renderData

            val measuredSize = checkNotNull(item.measuredSize) { "measured size should not be null, container:$this item:$item)" }
            val alignSelf = renderData.alignSelf

            val frame = if (horizontal) {
                val top = calcAlign(alignItems, alignSelf, spaceForAlign, measuredSize.height)
                RawFrame(RawPoint(padding.top + top, offset), measuredSize)
            } else {
                val left = calcAlign(alignItems, alignSelf, spaceForAlign, measuredSize.width)
                RawFrame(RawPoint(offset, padding.left + left), measuredSize)
            }

            item.layout(frame)

            offset += gap + (if (horizontal) measuredSize.width else measuredSize.height)
        }
    }
}