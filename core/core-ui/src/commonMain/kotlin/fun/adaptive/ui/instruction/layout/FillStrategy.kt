/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.layout

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.render.layout

/**
 * Stack containers (row, column) distribute space / resize items, according to
 * the fill strategy, when instructed.
 *
 * @property   constrain  Main axis - propose only the remaining space for their content in
 *                        respect of the content already placed.
 *
 * @property   reverse    Main axis - reverse the order of children when calculating constraints.
 *
 * @property   resizeToMax  Cross-axis - resize children to fill the space available in the cross-axis.
 *                        This is **expensive** (double layout for most children), use it with care.
 */
@Adat
open class FillStrategy(
    val constrain: Boolean,
    val reverse: Boolean,
    val resizeToMax: Boolean
) : AdaptiveInstruction {

    override fun applyTo(subject: Any) {
        layout(subject) {
            it.fill = this
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other == null) return false
        return (
            other is FillStrategy
                && other.reverse == reverse
                && other.constrain == constrain
                && other.resizeToMax == resizeToMax
            )
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + constrain.hashCode()
        result = 31 * result + reverse.hashCode()
        result = 31 * result + resizeToMax.hashCode()
        return result
    }
}