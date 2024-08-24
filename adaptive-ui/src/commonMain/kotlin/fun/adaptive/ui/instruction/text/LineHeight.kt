/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.text

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.render.textAndAdapter

@Adat
class LineHeight(val height: DPixel) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        textAndAdapter(subject) { t, a -> t.lineHeight = a.toPx(height) }
    }
}
