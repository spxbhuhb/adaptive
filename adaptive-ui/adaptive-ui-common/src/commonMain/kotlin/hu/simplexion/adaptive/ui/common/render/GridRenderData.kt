/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.render

import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter
import hu.simplexion.adaptive.ui.common.instruction.SPixel
import hu.simplexion.adaptive.ui.common.instruction.TextAlign
import hu.simplexion.adaptive.ui.common.instruction.TextDecoration
import hu.simplexion.adaptive.ui.common.instruction.TextWrap
import hu.simplexion.adaptive.ui.common.support.GridCell

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