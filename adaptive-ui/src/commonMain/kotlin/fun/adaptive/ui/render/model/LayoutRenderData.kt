/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.render.model

import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.fragment.layout.RawSurrounding
import `fun`.adaptive.ui.instruction.layout.Alignment

class LayoutRenderData(
    val adapter : AbstractAuiAdapter<*, *>
) {
    var instructedTop: Double? = null
    var instructedLeft: Double? = null
    var instructedWidth: Double? = null
    var instructedHeight: Double? = null

    var padding: RawSurrounding? = null
    var margin: RawSurrounding? = null
    var border: RawSurrounding? = null

    var verticalAlignment: Alignment? = null
    var horizontalAlignment: Alignment? = null

    var fillHorizontal: Boolean = false
    var fillVertical: Boolean = false

    var fixed: Boolean? = null
    var zIndex: Int? = null
}