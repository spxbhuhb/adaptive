/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.layout

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.render.layout

@Adat
class Size(
    val width: DPixel,
    val height: DPixel
) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        layout(subject) {
            val adapter = it.adapter
            it.instructedWidth = adapter.toPx(width)
            it.instructedHeight = adapter.toPx(height)
        }
    }
}