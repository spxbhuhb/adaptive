package `fun`.adaptive.ui.fragment.layout.cellbox


/**
 * Represents the final arrangement of cells.
 */
data class CellBoxArrangement(
    val groups: List<CellBoxGroup>,
    val isVertical: Boolean,
    val totalWidth: Double
)
