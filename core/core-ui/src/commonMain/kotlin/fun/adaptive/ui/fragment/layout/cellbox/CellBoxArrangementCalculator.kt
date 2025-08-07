package `fun`.adaptive.ui.fragment.layout.cellbox

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
                definition = cell.group,
                rawMinSize = cell.minSize.toRawValue(adapter)
            )
        }

        // using 0.001 to account for floating point errors
        val availableWidthComparable = availableWidth + 0.001

        // If the full layout fits into the available width, use the full layout
        val fullLayoutWidth = calculateCellWidths(groups, availableWidth, cellGap)

        if (fullLayoutWidth <= availableWidthComparable) {
            return CellBoxArrangement(groups, false, fullLayoutWidth)
        }

        // Try to form groups until we fit within the available width

        for (priority in collectPriorities(cells)) {

            groups = collapsePriority(groups, priority)

            val layoutWidth = calculateCellWidths(groups, availableWidth, cellGap)

            // using 0.001 to account for floating point errors

            if (layoutWidth <= availableWidthComparable) {
                return CellBoxArrangement(groups, false, layoutWidth)
            }
        }

        val allVerticalMinSize = cells.maxOf { it.minSize.value }.dp
        val allVerticalMaxSize = if (cells.any { it.maxSize.isFraction }) 1.fr else cells.maxOf { it.maxSize.value }.dp
        val allVerticalWidth = if (allVerticalMaxSize.isFraction) availableWidth else allVerticalMaxSize.toRawValue(adapter)
        val allVerticalRawMinSize = allVerticalMinSize.toRawValue(adapter)

        // return with a vertical list if no other options left
        val allVertical = CellBoxGroup(
            cells = cells.toMutableList(),
            minSize = allVerticalMinSize,
            maxSize = allVerticalMaxSize,
            definition = null,
            rawMinSize = allVerticalRawMinSize,
            calculatedWidth = max(allVerticalRawMinSize, allVerticalWidth)
        )

        return CellBoxArrangement(
            listOf(allVertical),
            isVertical = true,
            allVerticalWidth
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

    /**
     * Calculates the widths for cells with fractional tracks.
     * Similar to the distribute function in AbstractGrid.
     */
    private fun calculateCellWidths(cells: List<CellBoxGroup>, availableWidth: Double, cellGap: Double): Double {
        // Calculate total space used by fixed tracks and gaps
        var usedSpace = (cells.size - 1) * cellGap
        var fractionSum = 0.0

        val fractionCells = mutableListOf<CellBoxGroup>()

        // First pass: calculate widths for fixed tracks and sum up fractions
        for (cell in cells) {
            val maxSize = cell.maxSize

            if (maxSize.isFix) {
                cell.calculatedWidth = maxSize.toRawValue(adapter)
                usedSpace += cell.calculatedWidth
            } else if (maxSize.isFraction) {
                fractionSum += maxSize.value
                fractionCells += cell
            }
        }

        var availableForFractions = (availableWidth - usedSpace).coerceAtLeast(0.0)

        while (fractionCells.isNotEmpty()) {

            val fractionUnit = if (fractionSum > 0) availableForFractions / fractionSum else 0.0

            // For fractional cells where the minimum size is larger than the available space,
            // set the minimum size to the specified value. Remove these cells from consideration.

            val before = fractionCells.size

            // Using while to avoid a concurrent modification exception
            var index = 0
            var end = fractionCells.size

            while (index < end) {
                val cell = fractionCells[index]
                val size = cell.maxSize.value * fractionUnit

                if (size < cell.rawMinSize) {
                    cell.calculatedWidth = cell.rawMinSize
                    availableForFractions -= cell.rawMinSize
                    fractionSum -= cell.maxSize.value
                    fractionCells.removeAt(index)
                    end--
                } else {
                    index++
                }
            }

            // At this point we have fractional cells that were fine with the old fractionUnit.
            // However, as the available space for fractions may have decreased, we might need
            // to recalculate the fractionUnit to fit the remaining space.

            // When the size of fractional cells is reduced, we have to recalculate.
            if (before != fractionCells.size) continue

            // There were no cells that hit the minimum size requirement. We can safely use the
            // fractionUnit to fit the remaining space.

            for (cell in fractionCells) {
                cell.calculatedWidth = cell.maxSize.value * fractionUnit
            }

            break
        }

        return cells.sumOf { it.calculatedWidth } + (cells.size - 1) * cellGap
    }

}