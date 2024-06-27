/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.support.layout

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter
import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.render.GridRenderData

abstract class AbstractGrid<RT, CRT : RT>(
    adapter: AbstractCommonAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int
) : AbstractContainer<RT, CRT>(
    adapter, parent, declarationIndex, 0, 2
) {

    companion object {
        /**
         * When `colTemplate` or `rowTemplate` is missing we use `singleTrack`.
         */
        val singleTrack = listOf(
            RawTrack(isFix = false, isFraction = false, isMinContent = true, rawValue = 1.0)
        )
    }

    var colOffsets = DoubleArray(0)
    var rowOffsets = DoubleArray(0)


    override fun computeLayout(proposedWidth: Double, proposedHeight: Double) {
        val data = renderData
        val layout = data.layout
        val container = data.container

        val colTracks = container?.colTracks ?: singleTrack
        val rowTracks = container?.rowTracks ?: singleTrack

        val colGap = container?.gapWidth ?: 0.0 // gap between columns
        val rowGap = container?.gapHeight ?: 0.0 // gap between rows

        // ----  place the items in cells  --------------------------------------------

        layoutItems.forEach { it.renderData.grid ?: GridRenderData(uiAdapter).apply { it.renderData.grid = this } }
        val placement = placeItemsInCells(colTracks.size, rowTracks.size)

        // ----  calculate available space, might be NaN

        val colAvailableSpace = (layout?.instructedWidth ?: proposedWidth) - data.surroundingHorizontal - colTracks.boundSpace(colGap)
        val rowAvailableSpace = (layout?.instructedHeight ?: proposedHeight) - data.surroundingVertical - rowTracks.boundSpace(rowGap)

        // ----  compute item layouts and column/row sizes

        val colSizes = DoubleArray(colTracks.size + 1)
        val rowSizes = DoubleArray(rowTracks.size + 1)

        for (item in layoutItems) {
            val g = item.renderData.grid !!

            val proposedItemWidth = proposedItemSize(colTracks, g.colIndex, g.colSpan)
            val proposedItemHeight = proposedItemSize(rowTracks, g.rowIndex, g.rowSpan)

            item.computeLayout(proposedItemWidth, proposedItemHeight)

            applyItemSizes(item, colSizes)
        }
    }

    fun List<RawTrack>.boundSpace(gap: Double): Double {
        var usedSpace = (size - 1) * gap

        for (i in indices) {
            val track = this[i]
            if (track.isFix) {
                usedSpace += track.rawValue
            }
        }

        return usedSpace
    }

    fun List<RawTrack>.fractionSum(): Double {
        var fractionSum = 0.0

        for (i in indices) {
            val track = this[i]
            if (track.isFraction) {
                fractionSum += track.rawValue
            }
        }

        return fractionSum
    }

    fun proposedItemSize(tracks: List<RawTrack>, start: Int, span: Int): Double {
        var index = start
        val end = index + span

        var width = 0.0

        while (index < end) {
            val track = tracks[index]
            if (track.isFix) {
                width += track.rawValue
            } else {
                width = unbound
            }
            index ++
        }

        return width
    }

    fun applyItemSizes(tracks: List<RawTrack>, sizes: DoubleArray, itemSize: Double) {


    }




//    override fun measure(): RawFrame {
//        for (item in layoutItems) {
//            item.measure()
//        }
//
//        renderData.innerWidth = size(renderData.layout?.instructedWidth, colTracksPrepared)
//        renderData.innerHeight = size(renderData.layout?.instructedHeight, rowTracksPrepared)
//
//        return super.measure()
//    }

    fun size(instructed: Double?, tracks: List<RawTrack>) =
        when {
            instructed != null -> instructed
            tracks.any { ! it.isFix } -> Double.POSITIVE_INFINITY
            else -> Double.NaN
        }

//    override fun layout(proposedFrame: RawFrame?) {
//        calcLayoutFrame(proposedFrame)
//
//        prepare()
//
//        val offsets = toFrameOffsets()
//
//        for (item in layoutItems) {
//            item.layout(toFrame(item, offsets))
//        }
//
//        for (item in structuralItems) {
//            item.layout(layoutFrame)
//        }
//    }

    /**
     * There is a difference between browser and other targets. Browser handles margin
     * and padding in its own way. `toFrameOffsets` lets the browser grid implementation
     * to adjust the positions of items as needed.
     */
    open fun toFrameOffsets(): RawPosition {
        val border = renderData.layout?.border ?: RawSurrounding.ZERO
        val padding = renderData.layout?.padding ?: RawSurrounding.ZERO

        return RawPosition(border.top + padding.top, border.start + padding.start)
    }

    fun toFrame(fragment: AbstractCommonFragment<RT>, offsets: RawPosition): RawFrame {
        val grid = checkNotNull(fragment.renderData.grid) { "missing grid data at $this" }

        val row = grid.rowIndex
        val col = grid.colIndex

        return RawFrame(
            rowOffsets[row] + offsets.top,
            colOffsets[col] + offsets.left,
            colOffsets[col + grid.colSpan] - colOffsets[col],
            rowOffsets[row + grid.rowSpan] - rowOffsets[row]
        )
    }

    /**
     * Prepares the grid for layout. Calculates all track sizes and places all items
     * in the grid. This method requires `layoutFrame` to be set.
     */
    fun prepare() {
        val data = renderData
        val container = data.container !!


        val colGap = container.gapHeight
        val rowGap = container.gapWidth

        val availableWidth = data.finalWidth - data.surroundingHorizontal
        val availableHeight = data.finalHeight - data.surroundingVertical

        colOffsets = distribute(availableWidth, colGap, container.colTracks !!)
        rowOffsets = distribute(availableHeight, rowGap, container.rowTracks !!)

        if (trace) {
            trace("measure-layoutFrame", "width: ${data.finalWidth}, height: ${data.finalHeight}")
            trace("measure-colOffsets", colOffsets.contentToString())
            trace("measure-rowOffsets", rowOffsets.contentToString())
        }


    }

    /**
     * Distribute the available space between tracks:
     *
     * - first allocate space for all fix tracks
     * - then distribute the remaining space between the fraction tracks
     *
     * @return Offsets of the tracks in the available space. Size of a track
     *         can be calculated by subtracting the offset of the track from
     *         the offset of the next track. The return array is one item
     *         longer than [tracks] as it contains the offset of the "end"
     *         as well.
     */
    fun distribute(availableSpace: Double, gap: Double, tracks: List<RawTrack>): DoubleArray {

        var usedSpace = (tracks.size - 1) * gap
        var fractionSum = 0.0

        for (i in tracks.indices) {
            val track = tracks[i]
            if (track.isFix) {
                usedSpace += track.rawValue
            } else {
                fractionSum += track.rawValue
            }
        }

        val piece = (availableSpace - usedSpace) / fractionSum

        var offset = 0.0
        var previous = 0.0 // size of the previous track

        val result = DoubleArray(tracks.size + 1)

        for (i in tracks.indices) {
            offset += previous
            result[i] = offset

            if (tracks[i].isFix) {
                previous = tracks[i].rawValue
            } else {
                previous = tracks[i].rawValue * piece
            }

            offset += gap
        }

        result[tracks.size] = offset + previous - gap // the last gap is not there

        // Doubleing position calculations may result in small fractional values, for example:
        // available=852.0 result:[0.0, 140.0, 190.0, 360.6667, 531.3334, 702.00006, 752.00006, 852.00006]
        if (availableSpace + 0.0001 < result.last()) {
            adapter.log(this, "distribute", "grid track overflow: available=$availableSpace result:${result.contentToString()} tracks: $tracks in $this")
        }

        return result
    }

    fun placeItemsInCells(rows: Int, cols: Int): Array<Array<AbstractCommonFragment<RT>?>> {

        val grid = Array(rows) { arrayOfNulls<AbstractCommonFragment<RT>?>(cols) }

        fun findNextEmptyCell(): Pair<Int, Int>? {
            for (r in 0 until rows) {
                for (c in 0 until cols) {
                    if (grid[r][c] == null) return r to c
                }
            }
            return null
        }

        fun canPlaceFragment(r: Int, c: Int, rowSpan: Int, colSpan: Int): Boolean {
            if (r + rowSpan > rows || c + colSpan > cols) return false
            for (i in r until r + rowSpan) {
                for (j in c until c + colSpan) {
                    if (grid[i][j] != null) return false
                }
            }
            return true
        }

        fun placeFragment(fragment: AbstractCommonFragment<RT>, cell: GridCell, rowSpan: Int, colSpan: Int, r: Int, c: Int) {
            for (i in r until r + rowSpan) {
                for (j in c until c + colSpan) {
                    grid[i][j] = fragment
                }
            }
            cell.rowIndex = r
            cell.colIndex = c
        }

        for (item in layoutItems) {
            val cell = item.renderData.grid !!
            val row = cell.gridRow?.let { it - 1 }
            val col = cell.gridCol?.let { it - 1 }
            val rowSpan = cell.rowSpan
            val colSpan = cell.colSpan

            if (row != null && col != null) {
                if (canPlaceFragment(row, col, rowSpan, colSpan)) {
                    placeFragment(item, cell, rowSpan, colSpan, row, col)
                } else {
                    throw IllegalStateException("Cannot place fragment $cell at ($row, $col) in $this")
                }
            } else if (row != null) {
                for (c in 0 until cols) {
                    if (canPlaceFragment(row, c, rowSpan, colSpan)) {
                        placeFragment(item, cell, rowSpan, colSpan, row, c)
                        break
                    }
                }
            } else if (col != null) {
                for (r in 0 until rows) {
                    if (canPlaceFragment(r, col, rowSpan, colSpan)) {
                        placeFragment(item, cell, rowSpan, colSpan, r, col)
                        break
                    }
                }
            } else {
                val (r, c) = findNextEmptyCell() ?: throw IllegalStateException("Grid is full in $this")
                if (canPlaceFragment(r, c, rowSpan, colSpan)) {
                    placeFragment(item, cell, rowSpan, colSpan, r, c)
                } else {
                    throw IllegalStateException("Cannot place fragment $cell at implicit position in $this")
                }
            }
        }

        return grid
    }

}