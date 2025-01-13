/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.layout

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.render.grid
import `fun`.adaptive.ui.render.model.GridRenderData

@Adat
class GridColSpan(
    val span: Int
) : AdaptiveInstruction {
    override fun applyTo(subject: Any) {
        grid(subject) { it.colSpan = span }
    }
}