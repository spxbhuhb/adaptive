/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.layout

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.render.layout

@Adat
open class Fill(
    val fillStrategy: FillStrategy?
) : AdaptiveInstruction {

    override fun applyTo(subject: Any) {
        layout(subject) {
            it.fill = fillStrategy
        }
    }
    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other == null) return false
        return (
            other is Fill
                && other.fillStrategy == fillStrategy
            )
    }

    override fun hashCode(): Int =
        fillStrategy.hashCode()
}