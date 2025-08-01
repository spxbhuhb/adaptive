package `fun`.adaptive.ui.fragment.layout.cell

import `fun`.adaptive.ui.DensityIndependentAdapter
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.Fraction
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import kotlin.math.max

/**
 * Calculates cell arrangements without any reference to the contained fragments.
 * This allows calculating the arrangement once and then using it to lay out multiple list items.
 */
class CellBoxArrangementCalculator(
    private val adapter: DensityIndependentAdapter
) {

    // FIXME clean up cell box arrangement dp versus px conversions

    /**
     * Finds the best arrangement of cells based on available width.
     * Returns a CellBoxArrangement that describes how to lay out the cells.
     */
    fun findBestArrangement(
        cells: List<CellDef>,
        availableWidth: Double,
        cellGap: Double
    ): CellBoxArrangement {

        if (cells.isEmpty()) {
            return CellBoxArrangement(emptyList(), false, 0.0)
        }

        var groups = cells.map { cell ->
            CellBoxGroup(
                cells = mutableListOf(cell),
                minSize = cell.minSize,
                maxSize = cell.maxSize,
                definition = cell.group
            )
        }

        // If the full layout fits into the available width, use the full layout

        val fullLayoutWidth = calculateCellWidths(groups, availableWidth, cellGap)

        if (fullLayoutWidth <= availableWidth) {
            return CellBoxArrangement(groups, false, fullLayoutWidth)
        }

        // Try to form groups until we fit within the available width

        for (priority in collectPriorities(cells)) {

            groups = collapsePriority(groups, priority)

            val layoutWidth = calculateCellWidths(groups, availableWidth, cellGap)

            if (layoutWidth <= availableWidth) {
                return CellBoxArrangement(groups, false, fullLayoutWidth)
            }

        }

        // return with a vertical list if no other options left
        val allVertical = CellBoxGroup(
            cells = cells.toMutableList(),
            minSize = cells.minOf { it.minSize.value }.dp,
            maxSize = if (cells.any { it.maxSize.isFraction }) 1.fr else cells.maxOf { it.maxSize.value }.dp,
            definition = null
        )

        return CellBoxArrangement(
            listOf(allVertical),
            isVertical = true,
            if (allVertical.maxSize.isFraction) availableWidth else allVertical.maxSize.toRawValue(adapter)
        )
    }

    fun collectPriorities(cells: List<CellDef>): List<Int> {
        val priorities = mutableSetOf<Int>()

        for (cell in cells) {
            if (cell.group != null) {
                priorities += cell.group.priority
            }
        }

        return priorities.sorted()
    }

    fun collapsePriority(groups: List<CellBoxGroup>, priority: Int): List<CellBoxGroup> {
        val collapsed = mutableListOf<CellBoxGroup>()

        for (group in groups) {
            val definition = group.definition

            if (definition == null || definition.priority != priority) {
                collapsed += group
                continue
            }

            val last = collapsed.firstOrNull { it.definition == definition }
            if (last == null) {
                collapsed += group
                continue
            }

            last.cells += group.cells
        }

        return collapsed.map { mergeCellSizes(it) }
    }

    private fun mergeCellSizes(cellBoxGroup: CellBoxGroup): CellBoxGroup {
        val cells = cellBoxGroup.cells
        if (cells.size == 1) return cellBoxGroup

        var minSize = 0.0
        var maxFix = 0.0
        var maxFraction = 0.0

        for (cell in cells) {
            minSize = max(minSize, cell.minSize.value)

            val maxSize = cell.maxSize
            when {
                maxSize.isFix -> maxFix = max(maxFix, cell.maxSize.value)
                maxSize.isFraction -> maxFraction = max(maxFraction, maxSize.value)
                else -> throw IllegalStateException("Invalid cell size: $maxSize")
            }
        }

        CellBoxGroup(
            cells,
            DPixel(minSize),
            if (maxFraction > 0.0) {
                Fraction(maxFraction)
            } else {
                DPixel(maxFix)
            },
            definition = cellBoxGroup.definition
        ).also {
            return it
        }
    }


    // TODO try merge calculateCellWidths with grid distribute
    /**
     * Calculates the widths for cells with fractional tracks.
     * Similar to the distribute function in AbstractGrid.
     */
    private fun calculateCellWidths(cells: List<CellBoxGroup>, availableWidth: Double, cellGap: Double): Double {
        // Calculate total space used by fixed tracks and gaps
        var usedSpace = (cells.size - 1) * cellGap
        var fractionSum = 0.0

        // First pass: calculate widths for fixed tracks and sum up fractions
        for (cell in cells) {
            val maxSize = cell.maxSize

            if (maxSize.isFix) {
                cell.calculatedWidth = maxSize.toRawValue(adapter)
                usedSpace += cell.calculatedWidth
            } else if (maxSize.isFraction) {
                fractionSum += maxSize.value
            }
        }

        // Calculate space available for fractional tracks
        val availableForFractions = (availableWidth - usedSpace).coerceAtLeast(0.0)

        // Calculate width per fraction unit
        val fractionUnit = if (fractionSum > 0) availableForFractions / fractionSum else 0.0

        // Second pass: calculate widths for fractional tracks
        for (cell in cells) {
            if (cell.maxSize.isFraction) {
                cell.calculatedWidth = max(cell.minSize.value, cell.maxSize.value * fractionUnit)
            }
        }

        return cells.sumOf { it.calculatedWidth } + (cells.size - 1) * cellGap
    }

}