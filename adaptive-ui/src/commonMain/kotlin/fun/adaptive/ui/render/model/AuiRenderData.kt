/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.render.model

import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.testing.Traceable
import `fun`.adaptive.ui.DensityIndependentAdapter
import `fun`.adaptive.ui.fragment.layout.AbstractContainer
import `fun`.adaptive.ui.fragment.layout.RawFrame
import `fun`.adaptive.ui.fragment.layout.RawSurrounding
import `fun`.adaptive.ui.instruction.layout.Size

/**
 * A pre-processed version of fragment instructions to make access from layout easier.
 *
 * @property innerWidth the intrinsic width of the inner content of the fragment without padding, border width and margin
 * @property innerHeight the intrinsic height of the inner content of the fragment without padding, border width and margin
 */
open class AuiRenderData(
    val adapter: DensityIndependentAdapter
) : Traceable {

    constructor(
        adapter: DensityIndependentAdapter,
        previous: AuiRenderData?,
        vararg instructionSets: AdaptiveInstructionGroup,
    ) : this(adapter) {
        instructionSets.forEach { it.applyTo(this) }
        computeSurrounding()
        innerWidth = previous?.innerWidth
        innerHeight = previous?.innerHeight
        finalTop = previous?.finalTop ?: 0.0
        finalLeft = previous?.finalLeft ?: 0.0
        finalWidth = previous?.finalWidth ?: 0.0
        finalHeight = previous?.finalHeight ?: 0.0
        layoutFragment = previous?.layoutFragment
    }

    override var tracePatterns: Array<out Regex> = emptyArray()

    var layoutFragment: AbstractContainer<*, *>? = null

    var innerWidth: Double? = null
    var innerHeight: Double? = null

    var surroundingTop = 0.0
    var surroundingStart = 0.0
    var surroundingBottom = 0.0
    var surroundingEnd = 0.0

    var surroundingHorizontal = 0.0
    var surroundingVertical = 0.0

    var finalTop = 0.0
    var finalLeft = 0.0
    var finalWidth = 0.0
    var finalHeight = 0.0

    var layout: LayoutRenderData? = null
    var decoration: DecorationRenderData? = null
    var container: ContainerRenderData? = null
    var text: TextRenderData? = null
    var grid: GridRenderData? = null
    var event: EventRenderData? = null
    var input: InputRenderData? = null

    val rawFrame
        get() = RawFrame(finalTop, finalLeft, finalWidth, finalHeight)

    val size
        get() = Size(adapter.toDp(finalWidth), adapter.toDp(finalHeight))

    fun computeSurrounding() {
        val padding = layout?.padding ?: RawSurrounding.ZERO
        val border = layout?.border ?: RawSurrounding.ZERO
        val margin = layout?.margin ?: RawSurrounding.ZERO

        surroundingStart = padding.start + border.start + margin.start
        surroundingTop = padding.top + border.top + margin.top
        surroundingEnd = padding.end + border.end + margin.end
        surroundingBottom = padding.bottom + border.bottom + margin.bottom

        surroundingVertical = surroundingTop + surroundingBottom
        surroundingHorizontal = surroundingStart + surroundingEnd
    }

    fun layoutIndependentChanged(previous: AuiRenderData): Boolean {
        if (decoration != previous.decoration) return true
        if (event != previous.event) return true
        if (input != previous.input) return true
        if (text != previous.text) return true
        return false
    }

    fun gridChanged(previous: AuiRenderData): Boolean {
        return grid != previous.grid
    }

    fun layoutChanged(previous: AuiRenderData): Boolean {
        if (layout != previous.layout) return true
        if (container != previous.container) return true
        return false
    }

    fun innerDimensionsChanged(previous: AuiRenderData): Boolean {
        if (previous.innerWidth != innerWidth) return true
        if (previous.innerHeight != innerHeight) return true
        return false
    }

    fun contains(x: Double, y: Double) =
        when {
            y < finalTop -> false
            x < finalLeft -> false
            x >= finalLeft + finalWidth -> false
            y >= finalTop + finalHeight -> false
            else -> true
        }
}