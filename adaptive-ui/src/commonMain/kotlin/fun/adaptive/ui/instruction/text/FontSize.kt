/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.text

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.instruction.SPixel
import `fun`.adaptive.ui.render.text

@Adat
class FontSize(
    val fontSize: SPixel
) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        text(subject) { it.fontSize = fontSize }
    }
}