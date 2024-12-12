/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.render.model

import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.fragment.layout.RawTrack
import `fun`.adaptive.ui.instruction.layout.Alignment
import `fun`.adaptive.ui.instruction.layout.SpaceDistribution

@Suppress("EqualsOrHashCode")
class ContainerRenderData(
    val adapter : AbstractAuiAdapter<*,*>
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

    var colExtension: RawTrack? = null
    var rowExtension: RawTrack? = null

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other == null) return false
        if (other !is ContainerRenderData) return false

        if (this.colTracks != other.colTracks) return false
        if (this.rowTracks != other.rowTracks) return false
        if (this.colExtension != other.colExtension) return false
        if (this.gapHeight != other.gapHeight) return false
        if (this.gapWidth != other.gapWidth) return false
        if (this.verticalAlignment != other.verticalAlignment) return false
        if (this.horizontalAlignment != other.horizontalAlignment) return false
        if (this.spaceDistribution != other.spaceDistribution) return false
        if (this.verticalScroll != other.verticalScroll) return false
        if (this.horizontalScroll != other.horizontalScroll) return false

        return true
    }
}