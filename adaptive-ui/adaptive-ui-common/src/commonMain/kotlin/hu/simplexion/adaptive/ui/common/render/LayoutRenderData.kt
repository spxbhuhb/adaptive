/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.render

import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter
import hu.simplexion.adaptive.ui.common.instruction.Alignment
import hu.simplexion.adaptive.ui.common.support.layout.RawSurrounding

class LayoutRenderData(
    val adapter : AbstractCommonAdapter<*, *>
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
}