/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.layout

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.fragment.layout.RawTrack
import `fun`.adaptive.ui.render.container

@Adat
class GridRowTemplate(
    val tracks: Array<out GridTrack>,
    val extend : GridTrack?
) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        container(subject) { c ->
            c.rowTracks = expand(tracks).map { RawTrack(it.isFix, it.isFraction, it.toRawValue(c.adapter)) }
            c.rowExtension = extend?.let { RawTrack(it.isFix, it.isFraction, it.toRawValue(c.adapter)) }
        }
    }
}