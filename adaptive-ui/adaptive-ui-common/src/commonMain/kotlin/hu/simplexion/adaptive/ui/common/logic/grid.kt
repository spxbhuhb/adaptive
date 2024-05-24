/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.logic

import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.instruction.BoundingRect
import hu.simplexion.adaptive.ui.common.instruction.Track

interface GridCell {
    val fragment: AdaptiveUIFragment
    var row: Int
    var col: Int
}

/**
 * Expand track list, replaces "repeat" with N copy of the track for example.
 */
fun expand(tracks : Array<out Track>) : Array<Track> {
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
 *         the offset of the next track or available space if it is the
 *         last track.
 */
fun distribute(availableSpace : Float, tracks: Array<Track>) : FloatArray {
    var remainingSpace = availableSpace
    var fractionSum = 0f
    val result = FloatArray(tracks.size)

    for (i in tracks.indices) {
        val track = tracks[i]
        if (track.isFix) {
            result[i] = track.value
            remainingSpace -= track.value
        } else {
            fractionSum += track.value
        }
    }

    val piece = remainingSpace / fractionSum
    val offset = 0f
    var previous = 0f // size of the previous track

    for (i in tracks.indices) {
        if (! tracks[i].isFix) {
            result[i] = offset + previous
            previous = tracks[i].value * piece
        }
    }

    check(availableSpace >= offset + previous) { "negative remaining space: available=$availableSpace remaining:$remainingSpace tracks: ${tracks.contentToString()}" }

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

    fun placeFragment(cell: GridCell, rowSpan : Int, colSpan: Int, r: Int, c: Int) {
        for (i in r until r + rowSpan) {
            for (j in c until c + colSpan) {
                grid[i][j] = cell
            }
        }
        cell.row = r
        cell.col = c
    }

    for (cell in cells) {
        val fragment = cell.fragment
        val row = fragment.uiInstructions.gridRow
        val col = fragment.uiInstructions.gridCol
        val rowSpan = fragment.uiInstructions.rowSpan
        val colSpan = fragment.uiInstructions.colSpan

        if (row != null && col != null) {
            if (canPlaceFragment(row, col, rowSpan, colSpan)) {
                placeFragment(cell, rowSpan, colSpan, row, col)
            } else {
                throw IllegalStateException("Cannot place fragment ${fragment.id} at ($row, $col)")
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
                throw IllegalStateException("Cannot place fragment ${fragment.id} at implicit position")
            }
        }
    }

    return cells
}

fun setFrame(cell : GridCell, colOffsets : FloatArray, rowOffsets : FloatArray) {

    val uiInstructions = cell.fragment.uiInstructions
    val rowSpan = cell.fragment.uiInstructions.rowSpan
    val colSpan = cell.fragment.uiInstructions.colSpan


 //   uiInstructions.frame = BoundingRect(0f, 0f, colSpanWidth, rowSpanWidth)

}