/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.common.render

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.testing.Traceable
import `fun`.adaptive.ui.common.AbstractCommonAdapter
import `fun`.adaptive.ui.common.fragment.layout.AbstractContainer
import `fun`.adaptive.ui.common.fragment.layout.RawSurrounding

/**
 * A pre-processed version of fragment instructions to make access from layout easier.
 *
 * @property innerWidth the intrinsic width of the inner content of the fragment without padding, border width and margin
 * @property innerHeight the intrinsic height of the inner content of the fragment without padding, border width and margin
 */
class CommonRenderData(
    val adapter: AbstractCommonAdapter<*, *>
) : Traceable {

    constructor(
        adapter: AbstractCommonAdapter<*, *>,
        instructions: Array<out AdaptiveInstruction>,
        previous: CommonRenderData?
    ) : this(adapter) {
        instructions.forEach { it.apply(this) }
        computeSurrounding()
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
}