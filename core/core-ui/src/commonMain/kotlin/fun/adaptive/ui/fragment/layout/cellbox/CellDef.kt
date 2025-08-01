/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.fragment.layout.cellbox

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.layout.GridTrack

/**
 * Cell definition for use with [AbstractCellBox].
 *
 * @property group Optional cell group for responsive layout
 * @property maxSize Defines width behavior of the cell
 * @property minSize Minimum size constraint for the cell
 */
@Adat
class CellDef(
    val group: CellGroupDef?,
    val minSize: DPixel,
    val maxSize: GridTrack
) {

    constructor(group: CellGroupDef?, fixSize: DPixel) : this(group, fixSize, fixSize)

}