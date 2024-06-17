/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.support

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter
import hu.simplexion.adaptive.ui.common.instruction.*
import hu.simplexion.adaptive.ui.common.render.GridRenderData
import hu.simplexion.adaptive.utility.firstOrNullIfInstance

abstract class AbstractGrid<RT, CRT : RT>(
    adapter: AbstractCommonAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int
) : AbstractContainerFragment<RT, CRT>(
    adapter, parent, declarationIndex, 0, 2
) {

    inner class PreparedTrack(
        track: Track
    ) {
        val isFix: Boolean = track.isFix
        val rawValue: Double = track.toRawValue(uiAdapter)
    }

    // TODO track preparation can be done only once
    var colTracksPrepared = emptyList<PreparedTrack>()
    var rowTracksPrepared = emptyList<PreparedTrack>()

    var colOffsets = DoubleArray(0)
    var rowOffsets = DoubleArray(0)

    override fun measure() {
        for (item in layoutItems) {
            item.measure()
        }

        val width = renderData.layout?.width.pxOrZero
        val height = renderData.layout?.height.pxOrZero

        return RawSize(width, height).also {
            measuredSize = it
            traceMeasure()
        }
    }

    override fun layout(proposedFrame: RawFrame?) {
        calcLayoutFrame(proposedFrame)

        prepare()

        for (item in layoutItems) {
            item.layout(item.toFrame(colOffsets, rowOffsets))
        }

        for (item in structuralItems) {
            item.layout(layoutFrame)
        }
    }

    /**
     * Prepares the grid for layout. Calculates all track sizes and places all items
     * in the grid. This method requires `layoutFrame` to be set.
     */
    fun prepare() {
        val colTemp = checkNotNull(instructions.firstOrNullIfInstance<ColTemplate>()) { "missing column template in $this" }
        val rowTemp = checkNotNull(instructions.firstOrNullIfInstance<RowTemplate>()) { "missing row template in $this" }

        val size = this.layoutFrame.size

        colTracksPrepared = expand(colTemp.tracks).map { PreparedTrack(it) }
        rowTracksPrepared = expand(rowTemp.tracks).map { PreparedTrack(it) }

        val colGap = renderData.container?.gapHeight ?: 0.0
        val rowGap = renderData.container?.gapWidth ?: 0.0

        val padding = renderData.layout.paddingOrZero
        val border = renderData.decoration.borderWidthOrZero
        val margin = renderData.layout.marginOrZero

        val availableWidth = size.width - padding.right - padding.left - border - margin.left - margin.right
        val availableHeight = size.height - padding.top - padding.bottom - border - margin.top - margin.bottom

        colOffsets = distribute(availableWidth, padding.left, colGap, colTracksPrepared)
        rowOffsets = distribute(availableHeight, padding.top, rowGap, rowTracksPrepared)

        if (trace) {
            trace("measure-layoutFrame", this.layoutFrame)
            trace("measure-colOffsets", colOffsets.contentToString())
            trace("measure-rowOffsets", rowOffsets.contentToString())
        }

        placeFragments(
            layoutItems.map { it.renderData.grid ?: GridRenderData().apply { it.renderData.grid = this } },
            rowOffsets.size - 1,
            colOffsets.size - 1
        )
    }

    /**
     * Expand track list, replaces "repeat" with N copy of the track for example.
     */
    fun expand(tracks: Array<out Track>): Array<Track> {
        val out = mutableListOf<Track>()
        for (track in tracks) {
            track.expand(out)
        }
        return out.toTypedArray()
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
    fun distribute(availableSpace: Double, startOffset: Double, gap: Double, tracks: List<PreparedTrack>): DoubleArray {

        var usedSpace = startOffset + (tracks.size - 1) * gap
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

        var offset = startOffset
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
        check(availableSpace + 0.0001 >= result.last()) { "grid track overflow: available=$availableSpace result:${result.contentToString()} tracks: $tracks in $this" }

        return result
    }

    fun placeFragments(cells: List<GridCell>, rows: Int, cols: Int): List<GridCell> {

        val grid = Array(rows) { arrayOfNulls<GridCell?>(cols) }

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

        fun placeFragment(cell: GridCell, rowSpan: Int, colSpan: Int, r: Int, c: Int) {
            for (i in r until r + rowSpan) {
                for (j in c until c + colSpan) {
                    grid[i][j] = cell
                }
            }
            cell.rowIndex = r
            cell.colIndex = c
        }

        for (cell in cells) {
            val row = cell.gridRow?.let { it - 1 }
            val col = cell.gridCol?.let { it - 1 }
            val rowSpan = cell.rowSpan
            val colSpan = cell.colSpan

            if (row != null && col != null) {
                if (canPlaceFragment(row, col, rowSpan, colSpan)) {
                    placeFragment(cell, rowSpan, colSpan, row, col)
                } else {
                    throw IllegalStateException("Cannot place fragment $cell at ($row, $col) in $this")
                }
            } else if (row != null) {
                for (c in 0 until cols) {
                    if (canPlaceFragment(row, c, rowSpan, colSpan)) {
                        placeFragment(cell, rowSpan, colSpan, row, c)
                        break
                    }
                }
            } else if (col != null) {
                for (r in 0 until rows) {
                    if (canPlaceFragment(r, col, rowSpan, colSpan)) {
                        placeFragment(cell, rowSpan, colSpan, r, col)
                        break
                    }
                }
            } else {
                val (r, c) = findNextEmptyCell() ?: throw IllegalStateException("Grid is full in $this")
                if (canPlaceFragment(r, c, rowSpan, colSpan)) {
                    placeFragment(cell, rowSpan, colSpan, r, c)
                } else {
                    throw IllegalStateException("Cannot place fragment $cell at implicit position in $this")
                }
            }
        }

        return cells
    }

}