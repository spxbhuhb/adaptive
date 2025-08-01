package `fun`.adaptive.ui.fragment.layout.cellbox

import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.testing.AuiTestAdapter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CellBoxArrangementCalculatorTest {

    @Test
    fun `empty cell box`() {
        val calculator = CellBoxArrangementCalculator(AuiTestAdapter())
        val arrangement = calculator.findBestArrangement(emptyList(), 100.0, 10.0)
        val expected = CellBoxArrangement(emptyList(), false, 0.0)
        assertEquals(expected, arrangement)
    }
    
    @Test
    fun `horizontal arrangement that fits within available width`() {
        val calculator = CellBoxArrangementCalculator(AuiTestAdapter())
        
        // Create cells with fixed widths that will fit horizontally
        val cell1 = CellDef(null, 0.dp, 30.dp)
        val cell2 = CellDef(null, 0.dp, 40.dp)
        
        // Total width: 30 + 40 + 10 (gap) = 80, which is less than available width of 100
        val arrangement = calculator.findBestArrangement(listOf(cell1, cell2), 100.0, 10.0)
        
        // Verify the arrangement
        assertFalse(arrangement.isVertical, "Arrangement should be horizontal")
        assertEquals(2, arrangement.groups.size, "Should have 2 groups")
        assertEquals(80.0, arrangement.totalWidth, "Total width should be 80.0")
        
        // Verify the groups
        assertEquals(30.0, arrangement.groups[0].calculatedWidth, "First group width should be 30.0")
        assertEquals(40.0, arrangement.groups[1].calculatedWidth, "Second group width should be 40.0")
    }
    
    @Test
    fun `arrangement with fractional widths`() {
        val calculator = CellBoxArrangementCalculator(AuiTestAdapter())
        
        // Create cells with mixed fixed and fractional widths
        val cell1 = CellDef(null, 0.dp, 30.dp)
        val cell2 = CellDef(null, 0.dp, 1.fr)
        val cell3 = CellDef(null, 0.dp, 2.fr)
        
        // Available width: 100
        // Fixed width: 30
        // Gaps: 2 * 10 = 20
        // Available for fractions: 100 - 30 - 20 = 50
        // 1fr = 50 / 3 = 16.67, 2fr = 33.33
        val arrangement = calculator.findBestArrangement(listOf(cell1, cell2, cell3), 100.0, 10.0)
        
        // Verify the arrangement
        assertFalse(arrangement.isVertical, "Arrangement should be horizontal")
        assertEquals(3, arrangement.groups.size, "Should have 3 groups")
        
        // Verify the groups
        assertEquals(30.0, arrangement.groups[0].calculatedWidth, "First group width should be 30.0")
        assertEquals(16.67, arrangement.groups[1].calculatedWidth, 0.01, "Second group width should be approximately 16.67")
        assertEquals(33.33, arrangement.groups[2].calculatedWidth, 0.01, "Third group width should be approximately 33.33")
    }
    
    @Test
    fun `arrangement that requires collapsing by priority`() {
        val calculator = CellBoxArrangementCalculator(AuiTestAdapter())
        
        // Create cell groups with different priorities
        val group1 = CellGroupDef("G1", 1)
        val group2 = CellGroupDef("G2", 2)
        
        // Create cells that won't fit horizontally without collapsing
        val cell1 = CellDef(group1, 0.dp, 40.dp)
        val cell2 = CellDef(group1, 0.dp, 40.dp)
        val cell3 = CellDef(group2, 0.dp, 30.dp)
        val cell4 = CellDef(group2, 0.dp, 30.dp)
        
        // Total width without collapsing: 40 + 40 + 30 + 30 + 3*10 (gaps) = 170
        // After collapsing priority 1: 40 (combined) + 30 + 30 + 2*10 (gaps) = 120
        // After collapsing priority 2: 40 (combined) + 30 (combined) + 10 (gap) = 80
        val arrangement = calculator.findBestArrangement(listOf(cell1, cell2, cell3, cell4), 100.0, 10.0)
        
        // Verify the arrangement
        assertFalse(arrangement.isVertical, "Arrangement should be horizontal")
        
        // The exact number of groups depends on the implementation details
        // But we can verify that collapsing occurred by checking the number of cells in each group
        var totalCells = 0
        for (group in arrangement.groups) {
            totalCells += group.cells.size
        }
        assertEquals(4, totalCells, "Total number of cells should be 4")
        
        // At least one group should have multiple cells
        assertTrue(arrangement.groups.any { it.cells.size > 1 }, "At least one group should have multiple cells")
    }
    
    @Test
    fun `arrangement that falls back to vertical layout`() {
        val calculator = CellBoxArrangementCalculator(AuiTestAdapter())
        
        // Create cells with fixed widths that won't fit horizontally even after collapsing
        val cell1 = CellDef(null, 0.dp, 60.dp)
        val cell2 = CellDef(null, 0.dp, 60.dp)
        
        // Total width: 60 + 60 + 10 (gap) = 130, which is more than available width of 100
        // Since there are no groups to collapse, it should fall back to vertical layout
        val arrangement = calculator.findBestArrangement(listOf(cell1, cell2), 100.0, 10.0)
        
        // Verify the arrangement
        assertTrue(arrangement.isVertical, "Arrangement should be vertical")
        assertEquals(1, arrangement.groups.size, "Should have 1 group")
        assertEquals(2, arrangement.groups.first().cells.size, "Should have 2 cells")
        assertEquals(60.0, arrangement.totalWidth, "Total width should be the max cell width (60.0)")
    }
    
    @Test
    fun `test collectPriorities method`() {
        val calculator = CellBoxArrangementCalculator(AuiTestAdapter())
        
        // Create cells with different group priorities
        val group1 = CellGroupDef("G1", 1)
        val group2 = CellGroupDef("G2", 3)
        val group3 = CellGroupDef("G3", 2)
        
        val cell1 = CellDef(group1, 0.dp, 10.dp)
        val cell2 = CellDef(group2, 0.dp, 10.dp)
        val cell3 = CellDef(group3, 0.dp, 10.dp)
        val cell4 = CellDef(null, 0.dp, 10.dp) // No group
        
        // Collect priorities
        val priorities = calculator.collectPriorities(listOf(cell1, cell2, cell3, cell4))
        
        // Verify the priorities are collected and sorted
        assertEquals(listOf(1, 2, 3), priorities, "Priorities should be collected and sorted")
    }
    
    @Test
    fun `test collapsePriority method`() {
        val calculator = CellBoxArrangementCalculator(AuiTestAdapter())
        
        // Create groups with different priorities
        val group1 = CellGroupDef("G1", 1)
        val group2 = CellGroupDef("G2", 2)
        
        // Create cells
        val cell1 = CellDef(group1, 0.dp, 10.dp)
        val cell2 = CellDef(group1, 0.dp, 20.dp)
        val cell3 = CellDef(group2, 0.dp, 30.dp)
        val cell4 = CellDef(null, 0.dp, 40.dp) // No group
        
        // Create initial groups
        val initialGroups = listOf(
            CellBoxGroup(mutableListOf(cell1), cell1.minSize, cell1.maxSize, cell1.group),
            CellBoxGroup(mutableListOf(cell2), cell2.minSize, cell2.maxSize, cell2.group),
            CellBoxGroup(mutableListOf(cell3), cell3.minSize, cell3.maxSize, cell3.group),
            CellBoxGroup(mutableListOf(cell4), cell4.minSize, cell4.maxSize, cell4.group)
        )
        
        // Collapse priority 1
        val collapsedGroups = calculator.collapsePriority(initialGroups, 1)
        
        // Verify the result
        assertEquals(3, collapsedGroups.size, "Should have 3 groups after collapsing")
        
        // First group should have 2 cells (collapsed)
        assertEquals(2, collapsedGroups[0].cells.size, "First group should have 2 cells")
        assertEquals(group1, collapsedGroups[0].definition, "First group should have priority 1")
        
        // Other groups should remain unchanged
        assertEquals(1, collapsedGroups[1].cells.size, "Second group should have 1 cell")
        assertEquals(group2, collapsedGroups[1].definition, "Second group should have priority 2")
        assertEquals(1, collapsedGroups[2].cells.size, "Third group should have 1 cell")
        assertEquals(null, collapsedGroups[2].definition, "Third group should have no priority")
    }
    
    @Test
    fun `test mixed cells with and without groups`() {
        val calculator = CellBoxArrangementCalculator(AuiTestAdapter())
        
        // Create groups with different priorities
        val group1 = CellGroupDef("G1", 1)
        val group2 = CellGroupDef("G2", 2)
        
        // Create cells with mixed pattern: group1, null, group1, null, group2
        val cell1 = CellDef(group1, 0.dp, 10.dp)
        val cell2 = CellDef(null, 0.dp, 15.dp) // No group
        val cell3 = CellDef(group1, 0.dp, 20.dp)
        val cell4 = CellDef(null, 0.dp, 25.dp) // No group
        val cell5 = CellDef(group2, 0.dp, 30.dp)
        
        // Create initial groups
        val initialGroups = listOf(
            CellBoxGroup(mutableListOf(cell1), cell1.minSize, cell1.maxSize, cell1.group),
            CellBoxGroup(mutableListOf(cell2), cell2.minSize, cell2.maxSize, cell2.group),
            CellBoxGroup(mutableListOf(cell3), cell3.minSize, cell3.maxSize, cell3.group),
            CellBoxGroup(mutableListOf(cell4), cell4.minSize, cell4.maxSize, cell4.group),
            CellBoxGroup(mutableListOf(cell5), cell5.minSize, cell5.maxSize, cell5.group)
        )
        
        // Collapse priority 1
        val collapsedGroups = calculator.collapsePriority(initialGroups, 1)
        
        // Verify the result
        assertEquals(4, collapsedGroups.size, "Should have 4 groups after collapsing")
        
        // Check the first group (should have cell1)
        assertEquals(2, collapsedGroups[0].cells.size, "First group should have 2 cells")
        assertEquals(group1, collapsedGroups[0].definition, "First group should have priority 1")
        assertTrue(collapsedGroups[0].cells.contains(cell1), "First group should contain cell1")
        
        // Check the second group (should be the first null group with cell2)
        assertEquals(1, collapsedGroups[1].cells.size, "Second group should have 1 cell")
        assertEquals(null, collapsedGroups[1].definition, "Second group should have no priority")
        assertTrue(collapsedGroups[1].cells.contains(cell2), "Second group should contain cell2")

        // Check the third group (should be the second null group with cell4)
        assertEquals(1, collapsedGroups[2].cells.size, "Third group should have 1 cell")
        assertEquals(null, collapsedGroups[2].definition, "Third group should have no priority")
        assertTrue(collapsedGroups[2].cells.contains(cell4), "Third group should contain cell4")
        
        // Check the fourth group (should be the group2 cell)
        assertEquals(1, collapsedGroups[3].cells.size, "Fourth group should have 1 cell")
        assertEquals(group2, collapsedGroups[3].definition, "Fourth group should have priority 2")
        assertTrue(collapsedGroups[3].cells.contains(cell5), "Fourth group should contain cell5")
        
        // Test with findBestArrangement to verify that cells are properly arranged
        val cells = listOf(cell1, cell2, cell3, cell4, cell5)
        
        // Total width: 10 + 15 + 20 + 25 + 30 + 4*10 (gaps) = 140
        // This should fit within the available width of 150
        val arrangement = calculator.findBestArrangement(cells, 150.0, 10.0)
        
        // Verify the arrangement
        assertFalse(arrangement.isVertical, "Arrangement should be horizontal")
        assertEquals(5, arrangement.groups.size, "Should have 5 groups")
        
        // Check that all cells are present
        var totalCells = 0
        for (group in arrangement.groups) {
            totalCells += group.cells.size
        }
        assertEquals(5, totalCells, "Total number of cells should be 5")
    }
    
    @Test
    fun `test cells with non-sequential group order`() {
        val calculator = CellBoxArrangementCalculator(AuiTestAdapter())
        
        // Create groups with different priorities
        val group1 = CellGroupDef("G1", 1)
        val group2 = CellGroupDef("G2", 2)
        val group3 = CellGroupDef("G3", 3)
        
        // Create cells with non-sequential group order: group1, group2, group1, group3, group2
        val cell1 = CellDef(group1, 0.dp, 10.dp)
        val cell2 = CellDef(group2, 0.dp, 20.dp)
        val cell3 = CellDef(group1, 0.dp, 30.dp)
        val cell4 = CellDef(group3, 0.dp, 40.dp)
        val cell5 = CellDef(group2, 0.dp, 50.dp)
        
        // Create initial groups
        val initialGroups = listOf(
            CellBoxGroup(mutableListOf(cell1), cell1.minSize, cell1.maxSize, cell1.group),
            CellBoxGroup(mutableListOf(cell2), cell2.minSize, cell2.maxSize, cell2.group),
            CellBoxGroup(mutableListOf(cell3), cell3.minSize, cell3.maxSize, cell3.group),
            CellBoxGroup(mutableListOf(cell4), cell4.minSize, cell4.maxSize, cell4.group),
            CellBoxGroup(mutableListOf(cell5), cell5.minSize, cell5.maxSize, cell5.group)
        )
        
        // Test collapsing priority 1
        val collapsedPriority1 = calculator.collapsePriority(initialGroups, 1)
        
        // Verify the result for priority 1
        assertTrue(collapsedPriority1.size < initialGroups.size, "Number of groups should decrease after collapsing priority 1")
        
        // Count total cells across all groups
        var totalCellsAfterPriority1 = 0
        for (group in collapsedPriority1) {
            totalCellsAfterPriority1 += group.cells.size
        }
        
        // Verify that all cells are present
        assertEquals(5, totalCellsAfterPriority1, "Total number of cells should be 5 after collapsing priority 1")
        
        // Verify that at least one group has multiple cells (indicating that collapsing occurred)
        assertTrue(collapsedPriority1.any { it.cells.size > 1 }, "At least one group should have multiple cells after collapsing priority 1")
        
        // Test collapsing priority 2
        val collapsedPriority2 = calculator.collapsePriority(collapsedPriority1, 2)
        
        // Verify the result for priority 2
        assertTrue(collapsedPriority2.size < collapsedPriority1.size, "Number of groups should decrease after collapsing priority 2")
        
        // Count total cells across all groups
        var totalCellsAfterPriority2 = 0
        for (group in collapsedPriority2) {
            totalCellsAfterPriority2 += group.cells.size
        }
        
        // Verify that all cells are present
        assertEquals(5, totalCellsAfterPriority2, "Total number of cells should be 5 after collapsing priority 2")
        
        // Verify that at least one group has multiple cells (indicating that collapsing occurred)
        assertTrue(collapsedPriority2.any { it.cells.size > 1 }, "At least one group should have multiple cells after collapsing priority 2")
        
        // Test full arrangement with cells that won't fit horizontally
        val cells = listOf(cell1, cell2, cell3, cell4, cell5)
        
        // Total width without collapsing: 10 + 20 + 30 + 40 + 50 + 4*10 (gaps) = 190
        // Total width with collapsing group 1: 30 + 20 + 40 + 50 + 3*10 (gaps) = 170
        // Total width with collapsing group 2: 30 + 40 + 50 + 2*10 (gaps) = 140
        var arrangement = calculator.findBestArrangement(cells, 150.0, 10.0)
        
        // Verify the arrangement
        assertFalse(arrangement.isVertical, "Arrangement should not be vertical")
        assertEquals(5, arrangement.groups.sumOf { it.cells.size }, "Total number of cells should be 5")
        assertEquals(3, arrangement.groups.size, "Should have 3 groups")

        arrangement = calculator.findBestArrangement(cells, 100.0, 10.0)

        // Verify the arrangement
        assertTrue(arrangement.isVertical, "Arrangement should be vertical")
        assertEquals(5, arrangement.groups.sumOf { it.cells.size }, "Total number of cells should be 5")
        assertEquals(1, arrangement.groups.size, "Should have 1 group")

        // as all cells are fixed size, the maximum size should be the total size of the arrangement
        assertEquals(50.0, arrangement.groups.first().calculatedWidth, "Group width should be 50.0")
        assertEquals(50.0, arrangement.totalWidth, "Total width should be 50.0")
    }
}