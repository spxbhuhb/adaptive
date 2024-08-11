/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.common.render

import `fun`.adaptive.ui.common.AbstractCommonAdapter
import `fun`.adaptive.ui.common.fragment.layout.RawTrack
import `fun`.adaptive.ui.common.instruction.Alignment
import `fun`.adaptive.ui.common.instruction.SpaceDistribution

class ContainerRenderData(
    val adapter : AbstractCommonAdapter<*,*>
) {
    var gapWidth = 0.0
    var gapHeight = 0.0

    var verticalAlignment: Alignment? = null
    var horizontalAlignment: Alignment? = null

    var spaceDistribution: SpaceDistribution? = null

    var verticalScroll: Boolean = false
    var horizontalScroll: Boolean = false

    var colTracks: List<RawTrack>? = null
    var rowTracks: List<RawTrack>? = null
}