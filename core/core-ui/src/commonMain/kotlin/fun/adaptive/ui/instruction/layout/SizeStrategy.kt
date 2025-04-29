/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.layout

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.render.layout

@Adat
open class SizeStrategy(
    val verticalBase: SizeBase? = null,
    val horizontalBase: SizeBase? = null,
    val minWidth: DPixel? = null,
    val maxWidth: DPixel? = null,
    val minHeight: DPixel? = null,
    val maxHeight: DPixel? = null
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
            minWidth ?: existing?.minWidth,
            maxWidth ?: existing?.maxWidth,
            minHeight ?: existing?.minHeight,
            maxHeight ?: existing?.maxHeight
        )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SizeStrategy) return false

        if (verticalBase != other.verticalBase) return false
        if (horizontalBase != other.horizontalBase) return false
        if (minWidth != other.minWidth) return false
        if (maxWidth != other.maxWidth) return false
        if (minHeight != other.minHeight) return false
        if (maxHeight != other.maxHeight) return false

        return true
    }

    override fun hashCode(): Int {
        var result = verticalBase?.hashCode() ?: 0
        result = 31 * result + (horizontalBase?.hashCode() ?: 0)
        result = 31 * result + (minWidth?.hashCode() ?: 0)
        result = 31 * result + (maxWidth?.hashCode() ?: 0)
        result = 31 * result + (minHeight?.hashCode() ?: 0)
        result = 31 * result + (maxHeight?.hashCode() ?: 0)
        return result
    }

}