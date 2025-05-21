/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.fragment.layout

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.render.model.GridRenderData

abstract class AbstractGrid<RT, CRT : RT>(
    adapter: AbstractAuiAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int
) : AbstractContainer<RT, CRT>(
    adapter, parent, declarationIndex, 2
) {

    companion object {
        /**
         * When `colTemplate` or `rowTemplate` is missing we use `singleTrack`.
         */
        val singleTrack = listOf(
            RawTrack(isFix = false, isFraction = true, rawValue = 1.0)
        )

        val defaultExtend = RawTrack(isFix = false, isFraction = true, rawValue = 1.0)
    }

    override fun computeLayout(
        proposal: SizingProposal
    ) {
        val data = renderData
        val layout = data.layout
        val container = data.container

        val colTracks = (container?.colTracks ?: singleTrack).toMutableList()
        val rowTracks = (container?.rowTracks ?: singleTrack).toMutableList()

        val colGap = container?.gapWidth ?: 0.0 // gap between columns
        val rowGap = container?.gapHeight ?: 0.0 // gap between rows

        // ----  assign the items to cells  -------------------------------------------

        val colExtend = container?.colExtension
        val rowExtend = container?.rowExtension ?: if (colExtend != null) null else defaultExtend

        layoutItems.forEach { it.renderData.grid ?: GridRenderData(uiAdapter).apply { it.renderData.grid = this } }
        placeItemsInCells(colTracks, colExtend, rowTracks, rowExtend)

        // ----  calculate and set inner and final sizes  -----------------------------

        val widthSum = colTracks.sumOf { if (it.isFix) it.rawValue else Double.POSITIVE_INFINITY }
        val heightSum = rowTracks.sumOf { if (it.isFix) it.rawValue else Double.POSITIVE_INFINITY }

        val instructedWidth = layout?.instructedWidth

        val finalWidth = when {
            instructedWidth != null -> instructedWidth
            widthSum == Double.POSITIVE_INFINITY -> proposal.containerWidth
            else -> widthSum + data.surroundingHorizontal + (colGap * (colTracks.size - 1))
        }

        val instructedHeight = layout?.instructedHeight

        val finalHeight = when {
            instructedHeight != null -> instructedHeight
            heightSum == Double.POSITIVE_INFINITY -> proposal.containerWidth
            else -> heightSum + data.surroundingVertical + (rowGap * (rowTracks.size - 1))
        }

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

        val horizontalAlignment = container?.horizontalAlignment
        val verticalAlignment = container?.verticalAlignment

        val lastRow = rowTracks.size - 1
        val lastCol = colTracks.size - 1

        for (item in layoutItems) {
            val g = item.renderData.grid !!

            item.computeLayout(
                proposeItemSize(colOffsets, colGap, g.colIndex, g.colSpan),
                proposeItemSize(rowOffsets, rowGap, g.rowIndex, g.rowSpan),
            )

            val row = g.rowIndex
            val col = g.colIndex

            val rowOffset = rowOffsets[row]
            val spanHeight = rowOffsets[row + g.rowSpan] - rowOffset - (if (row < lastRow) rowGap else 0.0)
            val topAlign = alignOnAxis(item.verticalAlignment, verticalAlignment, spanHeight, item.renderData.finalHeight)

            val colOffset = colOffsets[col]
            val spanWidth = colOffsets[col + g.colSpan] - colOffset - (if (col < lastCol) colGap else 0.0)
            val leftAlign = alignOnAxis(item.horizontalAlignment, horizontalAlignment, spanWidth, item.renderData.finalWidth)

            item.placeLayout(
                rowOffset + topAlign + data.surroundingTop,
                colOffset + leftAlign + data.surroundingStart
            )
        }

        placeStructural()
        data.sizingProposal = proposal
    }

    fun placeItemsInCells(
        cols: MutableList<RawTrack>,
        colExtension: RawTrack?,
        rows: MutableList<RawTrack>,
        rowExtension: RawTrack?
    ): List<List<AbstractAuiFragment<RT>?>> {

        val grid = MutableList(rows.size) { MutableList<AbstractAuiFragment<RT>?>(cols.size) { null } }

        fun findNextEmptyCell(): Pair<Int, Int>? {
            for (r in 0 until rows.size) {
                for (c in 0 until cols.size) {
                    if (grid[r][c] == null) return r to c
                }
            }

            if (rowExtension != null) {
                grid.add(MutableList(cols.size) { null })
                rows.add(rowExtension)
                return rows.lastIndex to 0
            }

            if (colExtension != null) {
                grid.forEach { it.add(null) }
                cols.add(colExtension)
                return 0 to cols.lastIndex
            }

            return null
        }

        fun canPlaceFragment(r: Int, c: Int, rowSpan: Int, colSpan: Int): Boolean {
            if (r + rowSpan > rows.size || c + colSpan > cols.size) return false
            for (i in r until r + rowSpan) {
                for (j in c until c + colSpan) {
                    if (grid[i][j] != null) return false
                }
            }
            return true
        }

        fun placeFragment(fragment: AbstractAuiFragment<RT>, cell: GridRenderData, rowSpan: Int, colSpan: Int, r: Int, c: Int) {
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
                for (c in 0 until cols.size) {
                    if (canPlaceFragment(row, c, rowSpan, colSpan)) {
                        placeFragment(item, cell, rowSpan, colSpan, row, c)
                        break
                    }
                }
            } else if (col != null) {
                for (r in 0 until rows.size) {
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

        val piece = if (fractionSum == 0.0) 0.0 else (availableSpace - usedSpace) / fractionSum

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
}