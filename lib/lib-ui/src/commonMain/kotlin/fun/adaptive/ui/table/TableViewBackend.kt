package `fun`.adaptive.ui.table

import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Gap

/**
 * @property    allItems         All the items this table has, including invisible (filtered out).
 * @property    filteredItems    The items that match the current state of the filter.
 * @property    viewportItems    Items that are shown in the current viewport.
 */
class TableViewBackend<ITEM> : SelfObservable<TableViewBackend<ITEM>>() {

    val cells: MutableList<TableCellDef<ITEM, *>> = mutableListOf()

    val tableTheme = TableTheme.default

    var allItems: MutableList<TableItem<ITEM>>? = null

    var filteredItems: List<TableItem<ITEM>>? = null

    var viewportItems: List<TableItem<ITEM>>? = null

    var gap : Gap = Gap(0.dp, 0.dp)

    /**
     * Incremented each time the table is sorted. Used to determine the sort order of each cell.
     */
    var sortOrder = 0

    /**
     * Sorts the table items based on the specified cell.
     * 
     * Behavior:
     * 1. When a cell is sorted for the first time, it sorts in descending order.
     * 2. When a cell that's already sorted is clicked again, it reverses the sort order.
     * 3. When multiple cells are sorted, the results of previous sorts are preserved.
     * 
     * @param cell The cell definition to sort by
     */
    fun sort(cell: TableCellDef<ITEM, *>) {
        if (!cell.sortable) return
        
        // Toggle sorting state for the cell
        cell.sorting = when (cell.sorting) {
            Sorting.None -> Sorting.Descending
            Sorting.Descending -> Sorting.Ascending
            Sorting.Ascending -> Sorting.Descending
        }

        cell.sortOrder = sortOrder++
        
        // Get the items to sort
        val items = allItems ?: return
        
        // Sort the items based on all sorted cells
        val sortedCells = cells.filter { it.sorting != Sorting.None }.sortedByDescending { it.sortOrder }

        if (sortedCells.isNotEmpty()) {
            items.sortWith { item1, item2 ->
                var result = 0
                
                for (sortedCell in sortedCells) {
                    result = sortedCell.compareFunction(item1.data, item2.data)
                    if (result != 0) break
                }

                result
            }
        }
        
        // Update the filtered and viewport items
        updateItems()
        
        // Notify observers that the table has changed
        notifyListeners()
    }
    
    /**
     * Updates the filtered and viewport items after sorting or filtering.
     */
    private fun updateItems() {
        // For now, just set filteredItems and viewportItems to allItems
        // In a real implementation, this would apply filters and pagination
        filteredItems = allItems
        viewportItems = allItems
    }
}