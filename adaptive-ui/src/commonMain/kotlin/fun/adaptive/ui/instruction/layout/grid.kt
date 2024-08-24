/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.layout

/**
 * Expand track list, replaces "repeat" with N copy of the track for example.
 */
internal fun expand(tracks: Array<out GridTrack>): Array<GridTrack> {
    val out = mutableListOf<GridTrack>()
    for (track in tracks) {
        track.expand(out)
    }
    return out.toTypedArray()
}