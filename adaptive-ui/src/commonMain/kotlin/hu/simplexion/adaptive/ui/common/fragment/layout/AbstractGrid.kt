/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.fragment.layout

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
            RawTrack(isFix = false, isFraction = true, rawValue = 1.0)
        )
    }

    override fun computeLayout(proposedWidth: Double, proposedHeight: Double) {
        val data = renderData
        val layout = data.layout
        val container = data.container

        val colTracks = container?.colTracks ?: singleTrack
        val rowTracks = container?.rowTracks ?: singleTrack

        val colGap = container?.gapWidth ?: 0.0 // gap between columns
        val rowGap = container?.gapHeight ?: 0.0 // gap between rows

        // ----  assign the items to cells  -------------------------------------------

        layoutItems.forEach { it.renderData.grid ?: GridRenderData(uiAdapter).apply { it.renderData.grid = this } }
        placeItemsInCells(colTracks.size, rowTracks.size)

        // ----  calculate and set inner and final sizes  -----------------------------

        val finalWidth = layout?.instructedWidth ?: proposedWidth
        val finalHeight = layout?.instructedHeight ?: proposedHeight

        data.finalWidth = finalWidth
        data.finalHeight = finalHeight

        val innerWidth = finalWidth - data.surroundingHorizontal
        val innerHeight = finalHeight - data.surroundingVertical

        data.innerWidth = innerWidth
        data.innerHeight = innerHeight

        // ----  calculate size of tracks, cannot be unbound  -------------------------

        val colOffsets = distribute(innerWidth, colGap, colTracks)
        val rowOffsets = distribute(innerHeight, rowGap, rowTracks)

        // ----  layout and place items -----------------------------------------------

        for (item in layoutItems) {
            val g = item.renderData.grid !!

            item.computeLayout(
                proposeItemSize(colOffsets, colGap, g.colIndex, g.colSpan),
                proposeItemSize(rowOffsets, rowGap, g.rowIndex, g.rowSpan)
            )

            val row = g.rowIndex
            val col = g.colIndex

            item.placeLayout(
                rowOffsets[row] + data.surroundingTop,
                colOffsets[col] + data.surroundingStart
            )
        }

        placeStructural()
    }

    fun proposeItemSize(offsets: DoubleArray, gap: Double, start: Int, span: Int): Double =
        if (start + span < offsets.size - 1) {
            offsets[start + span] - offsets[start] - gap
        } else {
            offsets[start + span] - offsets[start]
        }

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

        check(availableSpace.isFinite()) { "grid cannot be used without size information" }

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

        return result
    }

    fun placeItemsInCells(cols: Int, rows: Int): Array<Array<AbstractCommonFragment<RT>?>> {

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

        fun placeFragment(fragment: AbstractCommonFragment<RT>, cell: GridRenderData, rowSpan: Int, colSpan: Int, r: Int, c: Int) {
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

    override fun layoutChange(fragment: AbstractCommonFragment<*>) {
        // FIXME layout change for grid
    }
}