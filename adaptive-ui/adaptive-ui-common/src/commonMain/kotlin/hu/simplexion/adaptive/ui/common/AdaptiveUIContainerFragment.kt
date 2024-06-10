/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.internal.BoundFragmentFactory
import hu.simplexion.adaptive.foundation.fragment.AdaptiveAnonymous
import hu.simplexion.adaptive.ui.common.instruction.*
import hu.simplexion.adaptive.ui.common.layout.RawFrame
import hu.simplexion.adaptive.ui.common.layout.RawPadding
import hu.simplexion.adaptive.ui.common.layout.RawPoint
import hu.simplexion.adaptive.ui.common.layout.RawSize
import hu.simplexion.adaptive.utility.alsoIfInstance
import hu.simplexion.adaptive.utility.checkIfInstance

/**
 * Two uses: layouts and loop/select containers.
 *
 */
abstract class AdaptiveUIContainerFragment<RT, CRT : RT>(
    adapter: AdaptiveUIAdapter<RT, CRT>,
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

    val layoutItems = mutableListOf<AdaptiveUIFragment<RT>>() // Items to consider during layout.

    val directItems = mutableListOf<AdaptiveUIFragment<RT>>() // Items to update directly, see class docs.

    val structuralItems = mutableListOf<AdaptiveUIFragment<RT>>() // Items to update directly, see class docs.

    val content: BoundFragmentFactory
        get() = state[state.size - 1].checkIfInstance()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? {
        if (declarationIndex != 0) invalidIndex(declarationIndex)
        // FIXME I think this anonymous fragment is superfluous
        return AdaptiveAnonymous(adapter, this, declarationIndex, 0, content).apply { create() }
    }

    /**
     * An optimization to remove the whole subtree of fragments from the actual UI at once.
     * See [AdaptiveUIAdapter.actualBatch].
     */
    override fun unmount() {
        if (uiAdapter.actualBatch) {
            super.unmount()
            return
        }

        try {
            uiAdapter.actualBatch = true
            super.unmount()
        } finally {
            uiAdapter.actualBatch = false

            // Unmount calls this, but it is useless as actualBatch is true at
            // that time, therefore it is a no-op. So, we have to call it manually.
            // FIXME manual remove actual after actual batch
            // I think this is somewhat incorrect because it may call adapter.removeActualRoot twice
            parent?.removeActual(this, if (isStructural) null else true)
        }
    }

    override fun addActual(fragment: AdaptiveFragment, direct: Boolean?) {
        if (trace) trace("addActual", "fragment=$fragment, direct=$direct")

        fragment.alsoIfInstance<AdaptiveUIFragment<RT>> { itemFragment ->

            when (direct) {
                true -> {
                    layoutItems += itemFragment
                    directItems += itemFragment
                    uiAdapter.addActual(receiver, itemFragment.receiver)
                }
                false -> {
                    layoutItems += itemFragment
                }
                null -> {
                    structuralItems += itemFragment
                    uiAdapter.addActual(receiver, itemFragment.receiver)
                }
            }

            if (isMounted) {
                itemFragment.measure()
                layout(layoutFrame)
            }
        }
    }

    override fun removeActual(fragment: AdaptiveFragment, direct: Boolean?) {
        if (trace) trace("removeActual", "fragment=$fragment")

        // when in a batch, everything will be removed at once
        if (uiAdapter.actualBatch) return

        fragment.alsoIfInstance<AdaptiveUIFragment<RT>> { itemFragment ->

            when (direct) {
                true -> {
                    layoutItems.removeAt(layoutItems.indexOfFirst { it.id == fragment.id })
                    directItems.removeAt(directItems.indexOfFirst { it.id == fragment.id })
                    uiAdapter.removeActual(itemFragment.receiver)
                }
                false -> {
                    layoutItems.removeAt(layoutItems.indexOfFirst { it.id == fragment.id })
                }
                null -> {
                    structuralItems.removeAt(structuralItems.indexOfFirst { it.id == fragment.id })
                    uiAdapter.removeActual(itemFragment.receiver)
                }
            }

            if (isMounted) {
                layout(layoutFrame)
            }
        }
    }

    fun instructed() : RawSize {
        val instructedSize = RawSize(renderData.instructedSize, uiAdapter)

        for (item in layoutItems) {
            item.measure()
        }

        measuredSize = instructedSize
        traceMeasure()
        return instructedSize
    }

    fun measure(widthFun: (Float, RawPoint, RawSize) -> Float, heightFun: (Float, RawPoint, RawSize) -> Float): RawSize {
        val padding = RawPadding(renderData.padding, uiAdapter)

        var width = 0f
        var height = 0f

        for (item in layoutItems) {
            val size = checkNotNull(item.measure()) { "unable to measure, cannot get size of: $item" }

            val point = RawPoint(item.renderData.instructedPoint, uiAdapter)

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
        val instructedGap = renderData.gap.toPx(uiAdapter)
        val padding = RawPadding(renderData.padding, uiAdapter)

        if (layoutItems.isEmpty()) return (0f to instructedGap)

        val size = this.layoutFrame.size

        val available = if (horizontal) {
            size.width - padding.left - padding.right
        } else {
            size.height - padding.top - padding.bottom
        }

        val prefixPadding = if (horizontal) padding.left else padding.top

        val used = calcUsedSpace(horizontal)

        val gapCount = layoutItems.size - 1
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

        for (item in layoutItems) {
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
            for (item in layoutItems) {
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

        for (item in layoutItems) {
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

        for (item in structuralItems) {
            item.layout(RawFrame(RawPoint.ORIGIN, layoutFrame.size))
        }
    }
}