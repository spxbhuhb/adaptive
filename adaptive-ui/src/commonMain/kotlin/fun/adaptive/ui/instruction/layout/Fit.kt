/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.layout

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.render.layout

@Adat
open class Fit(
    val verticalStrategy: FitStrategy?,
    val horizontalStrategy: FitStrategy?
) : AdaptiveInstruction {

    override fun applyTo(subject: Any) {
        layout(subject) {
            it.fit = merge(it.fit)
        }
    }

    fun merge(existing: Fit?): Fit =
        when {
            existing == null -> this
            verticalStrategy == null -> Fit(existing.verticalStrategy, horizontalStrategy ?: existing.horizontalStrategy)
            horizontalStrategy == null -> Fit(verticalStrategy, existing.horizontalStrategy)
            else -> this
        }

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other == null) return false
        return (
            other is Fit
                && other.verticalStrategy == verticalStrategy
                && other.horizontalStrategy == horizontalStrategy
            )
    }

    override fun hashCode(): Int =
        verticalStrategy.hashCode() + 31 * horizontalStrategy.hashCode()
}