package `fun`.adaptive.ui.table

import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.ui.input.text.textInputBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Gap
import kotlin.properties.Delegates.observable
import kotlin.reflect.KProperty

/**
 * @property    allItems         All the items this table has, including invisible (filtered out).
 * @property    filteredItems    The items that match the current state of the filter.
 * @property    viewportItems    Items that are shown in the current viewport.
 */
class TableViewBackend<ITEM> : SelfObservable<TableViewBackend<ITEM>>() {

    var cells : List<TableCellDef<ITEM, *>> = emptyList()

    var tableTheme = TableTheme.default

    internal var allItems : MutableList<TableItem<ITEM>>? = null

    var filteredItems : List<TableItem<ITEM>>? = null

    var viewportItems : List<TableItem<ITEM>>? = null

    var showHeaders = true

    var gap : Gap = Gap(0.dp, 0.dp)

    /**
     * Current filter text used to filter table items.
     * When this value changes, the table items are filtered automatically.
     * Kept for backward compatibility and as a default text-based filtering input.
     */
    var filterText : String by observable("", ::updateAndNotify)

    /**
     * Optional custom filtering data provided by the user.
     * This can be any type and is passed to [filterFun] when filtering.
     */
    var filterData : Any? by observable(null, ::updateAndNotify)

    /**
     * Optional custom filtering function. When set, this function will be used
     * to decide if an ITEM should be included.
     *
     * Parameters:
     *  - item: the raw data item
     *  - cells: the table cells (can be used to access getters or metadata)
     *  - filterData: the arbitrary filtering data set in [filterData]
     *
     * Return true to keep the item, false to filter it out.
     */
    var filterFun : ((item : ITEM, cells : List<TableCellDef<ITEM, *>>, filterData : Any?) -> Boolean)? = null

    /**
     * Incremented each time the table is sorted. Used to determine the sort order of each cell.
     */
    var sortOrder = 0

    /**
     * Backend to use for text-based filtering.
     */
    val filterTextBackend = textInputBackend("") {
        onChange = { filterText = it ?: "" }
    }

    var onDoubleClick : ((ITEM) -> Unit)? = null

    fun setAllItems(items : List<ITEM>) {
        allItems = items.map { TableItem(it) }.toMutableList()
        sortAllItemsByActiveSorts()
        updateAndNotify()
    }
    /**
     * Updates the filtered and viewport items after the table items have changed (set, sort, filter, etc.)
     */
    fun updateAndNotify(property : KProperty<*>, oldValue : Any?, newValue : Any?) {
        updateAndNotify()
    }

    /**
     * Updates the filtered and viewport items after the table items have changed (set, sort, filter, etc.)
     */
    fun updateAndNotify() {
        updateItems()
        notifyListeners()
    }

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
    fun sort(cell : TableCellDef<ITEM, *>) {
        if (! cell.sortable) return

        val newCell = cell.copy(
            sorting = when (cell.sorting) {
                Sorting.None -> Sorting.Descending
                Sorting.Descending -> Sorting.Ascending
                Sorting.Ascending -> Sorting.Descending
            },
            sortOrder = sortOrder ++
        )

        cells = cells.map { if (it == cell) newCell else it }

        // Perform sorting of all items based on current active sorts
        sortAllItemsByActiveSorts()
    }

    /**
     * Sorts allItems using all cells that currently have sorting enabled (sorting != None),
     * honoring their sortOrder priority (most recent first) and direction.
     * Does not modify cells; only sorts data and refreshes filtered/viewport lists.
     */
    fun sortAllItemsByActiveSorts() {
        val items = allItems ?: return

        // Collect active sorted cells in priority order (highest sortOrder first)
        val sortedCells = cells.filter { it.sorting != Sorting.None }.sortedByDescending { it.sortOrder }
        if (sortedCells.isEmpty()) {
            updateAndNotify()
            return
        }

        items.sortWith { item1, item2 ->
            var result = 0
            for (sortedCell in sortedCells) {
                result = sortedCell.compareFunction(item1.data, item2.data)
                if (result != 0) break
            }
            result
        }

        updateAndNotify()
    }

    /**
     * Updates the filtered and viewport items after sorting or filtering.
     */
    private fun updateItems() {
        val items = allItems ?: return
        
        // Determine how to filter: combine custom filter and filterText with logical AND
        val customFilter = filterFun
        filteredItems = items.filter { item ->
            // Apply custom predicate if available
            val customPass = customFilter?.invoke(item.data, cells, filterData) ?: true

            // Apply text-based predicate if filterText is not empty
            val textPass = if (filterText.isNotEmpty()) {
                cells.any { cell ->
                    if (! cell.supportsTextFilter) return@any false
                    cell.matches(item.data, filterText)
                }
            } else true

            customPass && textPass
        }

        // For now, just set viewportItems to filteredItems
        // In a real implementation, this would apply pagination
        viewportItems = filteredItems
    }

    override fun toString() : String {
        return "TableViewBackend(viewportItems=$viewportItems)"
    }

    fun newRevision(key : String) {
        cells = cells.map { if (it.key == key) it.copy(revision = it.revision + 1) else it }
    }

}