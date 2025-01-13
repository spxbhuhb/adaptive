/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.layout

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.fragment.layout.RawSurrounding
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.Surrounding
import `fun`.adaptive.ui.render.layout

@Adat
class Margin(
    override val top: DPixel?,
    override val right: DPixel?,
    override val bottom: DPixel?,
    override val left: DPixel?
) : AdaptiveInstruction, Surrounding {

    constructor(all: DPixel) : this(all, all, all, all)

    override fun applyTo(subject: Any) {
        layout(subject) {
            it.margin = RawSurrounding(this, it.margin ?: RawSurrounding.ZERO, it.adapter)
        }
    }

    companion object {
        val NONE = Margin(null, null, null, null)
    }
}