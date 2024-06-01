/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.ui.common.adapter.RenderData
import hu.simplexion.adaptive.utility.alsoIfInstance

data class Padding(
    val top: Float? = null,
    val right: Float? = null,
    val bottom: Float? = null,
    val left: Float? = null
) : AdaptiveInstruction {

    constructor(
        top : Int? = null,
        right : Int? = null,
        bottom : Int? = null,
        left: Int? = null
    ) : this(top?.toFloat(), right?.toFloat(), bottom?.toFloat(), left?.toFloat())

    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderData> { it.padding = this }
    }

}