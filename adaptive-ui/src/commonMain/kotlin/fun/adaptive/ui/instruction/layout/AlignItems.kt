/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.layout

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.render.container
import `fun`.adaptive.ui.render.model.ContainerRenderData

@Adat
class AlignItems(
    val vertical: Alignment?,
    val horizontal: Alignment?,
) : AdaptiveInstruction {

    override fun apply(subject: Any) {
        container(subject) {
            if (vertical != null) it.verticalAlignment = vertical
            if (horizontal != null) it.horizontalAlignment = horizontal
        }
    }

    companion object {
        val center = AlignItems(vertical = Alignment.Center, horizontal = Alignment.Center)

        val top = AlignItems(vertical = Alignment.Start, horizontal = null)

        val topStart = AlignItems(vertical = Alignment.Start, horizontal = Alignment.Start)
        val topCenter = AlignItems(vertical = Alignment.Start, horizontal = Alignment.Center)
        val topEnd = AlignItems(vertical = Alignment.Start, horizontal = Alignment.End)

        val start = AlignItems(vertical = null, horizontal = Alignment.Start)

        val startTop = AlignItems(vertical = Alignment.Start, horizontal = Alignment.Start)
        val startCenter = AlignItems(vertical = Alignment.Center, horizontal = Alignment.Start)
        val startBottom = AlignItems(vertical = Alignment.End, horizontal = Alignment.Start)

        val end = AlignItems(vertical = null, horizontal = Alignment.End)

        val endTop = AlignItems(vertical = Alignment.Start, horizontal = Alignment.End)
        val endCenter = AlignItems(vertical = Alignment.Center, horizontal = Alignment.End)
        val endBottom = AlignItems(vertical = Alignment.End, horizontal = Alignment.End)

        val bottom = AlignItems(vertical = Alignment.End, horizontal = null)

        val bottomStart = AlignItems(vertical = Alignment.End, horizontal = Alignment.Start)
        val bottomCenter = AlignItems(vertical = Alignment.End, horizontal = Alignment.Center)
        val bottomEnd = AlignItems(vertical = Alignment.End, horizontal = Alignment.End)
    }
}