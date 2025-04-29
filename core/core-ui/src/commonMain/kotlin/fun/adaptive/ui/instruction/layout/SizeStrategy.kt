/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.layout

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.render.layout

@Adat
open class SizeStrategy(
    val verticalBase: SizeBase?,
    val horizontalBase: SizeBase?,
    val widthMin: Double? = null,
    val widthMax: Double? = null,
    val heightMin: Double? = null,
    val heightMax: Double? = null
) : AdaptiveInstruction {

    override fun applyTo(subject: Any) {
        layout(subject) {
            it.sizeStrategy = merge(it.sizeStrategy)
        }
    }

    fun merge(existing: SizeStrategy?): SizeStrategy =
        SizeStrategy(
            verticalBase ?: existing?.verticalBase,
            horizontalBase ?: existing?.horizontalBase,
            widthMin ?: existing?.widthMin,
            widthMax ?: existing?.widthMax,
            heightMin ?: existing?.heightMin,
            heightMax ?: existing?.heightMax
        )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SizeStrategy) return false

        if (verticalBase != other.verticalBase) return false
        if (horizontalBase != other.horizontalBase) return false
        if (widthMin != other.widthMin) return false
        if (widthMax != other.widthMax) return false
        if (heightMin != other.heightMin) return false
        if (heightMax != other.heightMax) return false

        return true
    }

    override fun hashCode(): Int {
        var result = verticalBase?.hashCode() ?: 0
        result = 31 * result + (horizontalBase?.hashCode() ?: 0)
        result = 31 * result + (widthMin?.hashCode() ?: 0)
        result = 31 * result + (widthMax?.hashCode() ?: 0)
        result = 31 * result + (heightMin?.hashCode() ?: 0)
        result = 31 * result + (heightMax?.hashCode() ?: 0)
        return result
    }

}