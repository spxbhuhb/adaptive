/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.render

import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter
import hu.simplexion.adaptive.ui.common.support.layout.GridCell

/**
 * @property   gridRow   The instructed row if any.
 * @property   gridCol   The instructed column if any.
 * @property   rowSpan   The instructed row span or 1 if not instructed.
 * @property   colSpan   The instructed column span or 1 if not instructed.
 * @property   rowIndex  Zero based row index of the first cell this fragment occupies.
 * @property   colIndex  Zero based column index of the first cell this fragment occupies.
 */
class GridRenderData(
    val adapter : AbstractCommonAdapter<*, *>
) : GridCell {
    override var gridRow: Int? = null
    override var gridCol: Int? = null
    override var rowSpan: Int = 1
    override var colSpan: Int = 1
    override var rowIndex: Int = -1
    override var colIndex: Int = -1
}