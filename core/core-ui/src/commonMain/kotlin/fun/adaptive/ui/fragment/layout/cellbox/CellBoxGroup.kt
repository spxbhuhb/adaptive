package `fun`.adaptive.ui.fragment.layout.cellbox

import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.layout.GridTrack


/**
 * Represents a group of cells in the layout.
 */
data class CellBoxGroup(
    val cells: MutableList<CellDef>,
    val minSize: DPixel,
    val maxSize: GridTrack,
    val definition: CellGroupDef? = null,
    var calculatedWidth: Double = 0.0,
)