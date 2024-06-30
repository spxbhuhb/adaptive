/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter
import hu.simplexion.adaptive.ui.common.fragment.layout.RawTrack
import hu.simplexion.adaptive.ui.common.render.container
import hu.simplexion.adaptive.ui.common.render.grid

fun colTemplate(vararg tracks: Track) = ColTemplate(tracks)
fun rowTemplate(vararg tracks: Track) = RowTemplate(tracks)

infix fun Track.repeat(count: Int): Repeat = Repeat(count, this)

fun colSpan(span : Int) = ColSpan(span)
fun rowSpan(span : Int) = RowSpan(span)

fun gridCol(col: Int, span : Int = 1) = GridCol(col, span)
fun gridRow(row : Int, span : Int = 1) = GridRow(row, span)

fun gridPos(row : Int, col : Int, rowSpan : Int = 1, colSpan : Int = 1) = GridPos(row, col, rowSpan, colSpan)

val Number.gridCol
    inline get() = GridCol(this.toInt(), 1)

val Number.gridRow
    inline get() = GridRow(this.toInt(), 1)

val Number.rowSpan
    inline get() = RowSpan(this.toInt())

val Number.colSpan
    inline get() = ColSpan(this.toInt())

data class RowSpan(val span: Int) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        grid(subject) { it.rowSpan = span }
    }
}

data class ColSpan(val span: Int) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        grid(subject) { it.colSpan = span }
    }
}

data class GridPos(val row: Int, val col: Int, val rowSpan: Int, val colSpan: Int) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        grid(subject) {
            it.gridRow = row
            it.gridCol = col
            it.rowSpan = rowSpan
            it.colSpan = colSpan
        }
    }
}

data class GridRow(val row: Int, val span: Int) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        grid(subject) {
            it.gridRow = row
            it.rowSpan = span
        }
    }
}

data class GridCol(val col: Int, val span: Int) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        grid(subject) {
            it.gridCol = col
            it.colSpan = span
        }
    }
}

class ColTemplate(val tracks: Array<out Track>) : AdaptiveInstruction {

    override fun apply(subject: Any) {
        container(subject) { container ->
            container.colTracks = expand(tracks).map { RawTrack(it.isFix, it.isFraction, it.toRawValue(container.adapter)) }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is ColTemplate) return false
        return tracks.contentEquals(other.tracks)
    }

    override fun hashCode(): Int {
        return tracks.contentHashCode()
    }

    override fun toString(): String {
        return "ColTemplate(tracks=${tracks.contentToString()})"
    }
}

class RowTemplate(val tracks: Array<out Track>) : AdaptiveInstruction {

    override fun apply(subject: Any) {
        container(subject) { container ->
            container.rowTracks = expand(tracks).map { RawTrack(it.isFix, it.isFraction, it.toRawValue(container.adapter)) }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is RowTemplate) return false
        return tracks.contentEquals(other.tracks)
    }

    override fun hashCode(): Int {
        return tracks.contentHashCode()
    }

    override fun toString(): String {
        return "RowTemplate(tracks=${tracks.contentToString()})"
    }
}

// ---- Track --------------------------------------------------------

/**
 * IMPORTANT Tracks must be are immutable (or [Repeat] won't work).
 */
interface Track {

    val isIntrinsic: Boolean
        get() = true

    val isFix: Boolean
        get() = false

    val isFraction: Boolean
        get() = false

    val isMinContent: Boolean
        get() = false

    val value: Double

    fun expand(out: MutableList<Track>) {
        out.add(this)
    }

    fun toRawValue(adapter: AbstractCommonAdapter<*, *>): Double

}

/**
 * Expand track list, replaces "repeat" with N copy of the track for example.
 */
private fun expand(tracks: Array<out Track>): Array<Track> {
    val out = mutableListOf<Track>()
    for (track in tracks) {
        track.expand(out)
    }
    return out.toTypedArray()
}

/**
 * Repeat [track] [count] times.
 */
data class Repeat(val count: Int, val track: Track) : Track {

    override val isIntrinsic: Boolean
        get() = false

    override val value: Double
        get() = throw UnsupportedOperationException()

    override fun expand(out: MutableList<Track>) {
        for (i in 0 until count) {
            if (track.isIntrinsic) {
                out.add(track)
            } else {
                track.expand(out)
            }
        }
    }

    override fun toRawValue(adapter: AbstractCommonAdapter<*, *>): Double {
        throw UnsupportedOperationException()
    }

}