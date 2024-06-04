/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.ui.common.RenderData
import hu.simplexion.adaptive.utility.alsoIfInstance

data class Padding(
    val top: DPixel = DPixel.ZERO,
    val right: DPixel = DPixel.ZERO,
    val bottom: DPixel = DPixel.ZERO,
    val left: DPixel = DPixel.ZERO
) : AdaptiveInstruction {

    constructor(all : DPixel) : this(all, all, all, all)

    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderData> { it.padding = this }
    }

    companion object {
        val ZERO = Padding()
    }
}