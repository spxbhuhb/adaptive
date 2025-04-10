/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.render.model

import `fun`.adaptive.ui.DensityIndependentAdapter

/**
 * @property   gridRow       The instructed row if any.
 * @property   gridCol       The instructed column if any.
 * @property   rowSpan       The instructed row span or 1 if not instructed.
 * @property   colSpan       The instructed column span or 1 if not instructed.
 * @property   rowIndex      Zero based row index of the first cell this fragment occupies.
 * @property   colIndex      Zero based column index of the first cell this fragment occupies.
 */
@Suppress("EqualsOrHashCode")
class GridRenderData(
    val adapter : DensityIndependentAdapter
) {
    var gridRow: Int? = null
    var gridCol: Int? = null
    var rowSpan: Int = 1
    var colSpan: Int = 1
    var rowIndex: Int = - 1
    var colIndex: Int = - 1

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other == null) return false
        if (other !is GridRenderData) return false

        if (gridRow != other.gridRow) return false
        if (gridCol != other.gridCol) return false
        if (rowSpan != other.rowSpan) return false
        if (colSpan != other.colSpan) return false

        // indices are not set by instructions but by AbstractGrid

        return true
    }
}