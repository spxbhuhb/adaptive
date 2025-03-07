/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.render.model

import `fun`.adaptive.ui.DensityIndependentAdapter
import `fun`.adaptive.ui.fragment.layout.RawBorder
import `fun`.adaptive.ui.fragment.layout.RawCornerRadius
import `fun`.adaptive.ui.fragment.layout.RawDropShadow
import `fun`.adaptive.ui.instruction.decoration.BackgroundGradient
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.decoration.CursorType

@Suppress("EqualsOrHashCode")
class DecorationRenderData(
    val adapter : DensityIndependentAdapter
) {
    var border: RawBorder? = null

    var cornerRadius: RawCornerRadius? = null

    var backgroundColor: Color? = null
    var backgroundGradient : BackgroundGradient? = null

    var dropShadow: RawDropShadow? = null

    var cursor : CursorType? = null

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other == null) return false
        if (other !is DecorationRenderData) return false

        if (this.border != other.border) return false
        if (this.cornerRadius != other.cornerRadius) return false
        if (this.backgroundColor != other.backgroundColor) return false
        if (this.backgroundGradient != other.backgroundGradient) return false
        if (this.dropShadow != other.dropShadow) return false
        if (this.cursor != other.cursor) return false

        return true
    }
}