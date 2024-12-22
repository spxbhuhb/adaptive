/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.render.model

import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.fragment.layout.RawSurrounding
import `fun`.adaptive.ui.instruction.layout.Alignment
import `fun`.adaptive.ui.instruction.layout.Fit
import `fun`.adaptive.ui.instruction.layout.OverflowBehavior

@Suppress("EqualsOrHashCode")
class LayoutRenderData(
    val adapter : AbstractAuiAdapter<*, *>
) {
    var instructedTop: Double? = null
    var instructedLeft: Double? = null
    var instructedWidth: Double? = null
    var instructedHeight: Double? = null

    var padding: RawSurrounding? = null
    var margin: RawSurrounding? = null
    var border: RawSurrounding? = null

    var fit: Fit? = null

    var verticalAlignment: Alignment? = null
    var horizontalAlignment: Alignment? = null

    var fixed: Boolean? = null
    var overflow : OverflowBehavior? = null
    var zIndex: Int? = null

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other == null) return false
        if (other !is LayoutRenderData) return false

        if (instructedTop != other.instructedTop) return false
        if (instructedLeft != other.instructedLeft) return false
        if (instructedWidth != other.instructedWidth) return false
        if (instructedHeight != other.instructedHeight) return false
        if (padding != other.padding) return false
        if (margin != other.margin) return false
        if (border != other.border) return false
        if (verticalAlignment != other.verticalAlignment) return false
        if (horizontalAlignment != other.horizontalAlignment) return false
        if (fit != other.fit) return false
        if (fixed != other.fixed) return false
        if (overflow != other.overflow) return false
        if (zIndex != other.zIndex) return false

        return true
    }
}