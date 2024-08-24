/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.text

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.render.text

@Adat
class TextColor(
    val color: Color
) : AdaptiveInstruction {

    override fun apply(subject: Any) {
        text(subject) { it.color = color }
    }
}