/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.fragment

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.AdaptiveExpect
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.manualImplementation
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.instruction.Repeat
import hu.simplexion.adaptive.ui.common.instruction.Track

@AdaptiveExpect(commonUI)
fun stack(vararg instructions : AdaptiveInstruction, @Adaptive content : () -> Unit) {
    manualImplementation(instructions, content)
}

@AdaptiveExpect(commonUI)
fun grid(vararg instructions : AdaptiveInstruction, @Adaptive content : () -> Unit) {
    manualImplementation(instructions, content)
}

@AdaptiveExpect(commonUI)
fun pixel(vararg instructions : AdaptiveInstruction, @Adaptive content : () -> Unit) {
    manualImplementation(instructions, content)
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

    for (i in tracks.indices) {
        if (! tracks[i].isFix) {
            val space = tracks[i].value * piece
            result[i] = space
            remainingSpace -= space
        }
    }

    check(remainingSpace >= 0f) { "negative remainingSpace: available=$availableSpace remaining:$remainingSpace tracks: ${tracks.contentToString()}" }

    return result
}