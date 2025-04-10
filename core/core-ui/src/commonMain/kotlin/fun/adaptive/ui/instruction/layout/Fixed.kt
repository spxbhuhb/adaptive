/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.layout

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.render.layout
import `fun`.adaptive.ui.render.model.LayoutRenderData

@Adat
class Fixed : AdaptiveInstruction {
    override fun applyTo(subject: Any) {
        layout(subject) {
            it.fixed = true
        }
    }
}
