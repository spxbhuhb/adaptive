/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.render

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.testing.Traceable
import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter

/**
 * A pre-processed version of fragment instructions to make access from layout easier.
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

    var boxWidth = Double.NaN
    var boxHeight = Double.NaN

    var layout : LayoutRenderData? = null
    var decoration : DecorationRenderData? = null
    var container : ContainerRenderData? = null
    var text : TextRenderData? = null
    var grid : GridRenderData? = null
    var event : EventRenderData? = null

}