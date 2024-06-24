/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.render

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.testing.Traceable
import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter
import hu.simplexion.adaptive.ui.common.support.layout.RawFrame

/**
 * A pre-processed version of fragment instructions to make access from layout easier.
 *
 * @property measuredWidth the intrinsic width of the inner content of the fragment without padding, border width and margin
 * @property measuredHeight the intrinsic height of the inner content of the fragment without padding, border width and margin
 * @property box instructed top and left (or NaN) and the measured size + padding + border width + margin
 */
class CommonRenderData(
    val adapter : AbstractCommonAdapter<*,*>
): Traceable {

    constructor(
        adapter : AbstractCommonAdapter<*,*>,
        instructions : Array<out AdaptiveInstruction>
    ) : this(adapter) {
        instructions.forEach{ it.apply(this) }
    }

    override var tracePatterns : Array<out Regex> = emptyArray()

    var measuredWidth = Double.NaN
    var measuredHeight = Double.NaN

    var box: RawFrame = RawFrame.AUTO

    var layout : LayoutRenderData? = null
    var decoration : DecorationRenderData? = null
    var container : ContainerRenderData? = null
    var text : TextRenderData? = null
    var grid : GridRenderData? = null
    var event : EventRenderData? = null

}