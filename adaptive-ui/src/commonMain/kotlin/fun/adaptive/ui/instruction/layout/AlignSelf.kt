/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.layout

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.render.layout

@Adat
class AlignSelf(
    val vertical: Alignment?,
    val horizontal: Alignment?,
) : AdaptiveInstruction {

    override fun apply(subject: Any) {
        layout(subject) {
            if (vertical != null) it.verticalAlignment = vertical
            if (horizontal != null) it.horizontalAlignment = horizontal
        }
    }

    companion object {

        val center = AlignSelf(vertical = Alignment.Center, horizontal = Alignment.Center)

        val top = AlignSelf(vertical = Alignment.Start, horizontal = null)

        val topStart = AlignSelf(vertical = Alignment.Start, horizontal = Alignment.Start)
        val topCenter = AlignSelf(vertical = Alignment.Start, horizontal = Alignment.Center)
        val topEnd = AlignSelf(vertical = Alignment.Start, horizontal = Alignment.End)

        val start = AlignSelf(vertical = null, horizontal = Alignment.Start)

        val startTop = AlignSelf(vertical = Alignment.Start, horizontal = Alignment.Start)
        val startCenter = AlignSelf(vertical = Alignment.Center, horizontal = Alignment.Start)
        val startBottom = AlignSelf(vertical = Alignment.End, horizontal = Alignment.Start)

        val end = AlignSelf(vertical = null, horizontal = Alignment.End)

        val endTop = AlignSelf(vertical = Alignment.Start, horizontal = Alignment.End)
        val endCenter = AlignSelf(vertical = Alignment.Center, horizontal = Alignment.End)
        val endBottom = AlignSelf(vertical = Alignment.End, horizontal = Alignment.End)

        val bottom = AlignSelf(vertical = Alignment.End, horizontal = null)

        val bottomStart = AlignSelf(vertical = Alignment.End, horizontal = Alignment.Start)
        val bottomCenter = AlignSelf(vertical = Alignment.End, horizontal = Alignment.Center)
        val bottomEnd = AlignSelf(vertical = Alignment.End, horizontal = Alignment.End)
    }
}