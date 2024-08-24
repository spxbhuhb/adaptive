/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.layout

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.render.container

@Adat
class Scroll(
    val horizontal: Boolean? = null,
    val vertical: Boolean? = null
) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        container(subject) {
            if (vertical != null) it.verticalScroll = vertical
            if (horizontal != null) it.horizontalScroll = horizontal
        }
    }
}