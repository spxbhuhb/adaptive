/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.logic

import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.instruction.ColTemplate
import hu.simplexion.adaptive.ui.common.instruction.Frame
import hu.simplexion.adaptive.ui.common.instruction.RowTemplate
import hu.simplexion.adaptive.ui.common.instruction.Track
import hu.simplexion.adaptive.utility.firstOrNullIfInstance

interface GridCell {
    var rowIndex: Int
    var colIndex: Int

    val gridRow : Int?
    val gridCol : Int?

    val rowSpan : Int
    val colSpan : Int
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
fun distribute(availableSpace: Float, tracks: Array<out Track>): FloatArray {

    var usedSpace = 0f
    var fractionSum = 0f

    for (i in tracks.indices) {
        val track = tracks[i]
        if (track.isFix) {
            usedSpace += track.value
        } else {
            fractionSum += track.value
        }
    }

    val piece = (availableSpace - usedSpace) / fractionSum

    var offset = 0f
    var previous = 0f // size of the previous track

    val result = FloatArray(tracks.size + 1)

    for (i in tracks.indices) {
        offset += previous
        result[i] = offset

        if (tracks[i].isFix) {
            previous = tracks[i].value
        } else {
            previous = tracks[i].value * piece
        }
    }

    result[tracks.size] = offset + previous

    check(availableSpace >= result.last()) { "grid track overflow: available=$availableSpace result:${result.contentToString()} tracks: ${tracks.contentToString()}" }

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
                throw IllegalStateException("Cannot place fragment $cell at ($row, $col)")
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
            val (r, c) = findNextEmptyCell() ?: throw IllegalStateException("Grid is full")
            if (canPlaceFragment(r, c, rowSpan, colSpan)) {
                placeFragment(cell, rowSpan, colSpan, r, c)
            } else {
                throw IllegalStateException("Cannot place fragment $cell at implicit position")
            }
        }
    }

    return cells
}

fun AdaptiveUIFragment.layoutGrid(items : List<AdaptiveUIFragment>) {

    val colTemp = checkNotNull(instructions.firstOrNullIfInstance<ColTemplate>()) { "missing column template in $this" }
    val rowTemp = checkNotNull(instructions.firstOrNullIfInstance<RowTemplate>()) { "missing row template in $this" }

    val size = renderInstructions.layoutFrame.size
    val colOffsets = distribute(size.width, expand(colTemp.tracks))
    val rowOffsets = distribute(size.height, expand(rowTemp.tracks))

    if (trace) {
        trace("measure-layoutFrame", renderInstructions.layoutFrame)
        trace("measure-colOffsets", colOffsets.contentToString())
        trace("measure-rowOffsets", rowOffsets.contentToString())
    }

    placeFragments(items.map { it.renderInstructions }, rowOffsets.size - 1, colOffsets.size - 1)

    for (item in items) {
        item.layout(item.toFrame(colOffsets, rowOffsets))
    }
}