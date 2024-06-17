/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.render

import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter
import hu.simplexion.adaptive.ui.common.instruction.AlignSelf
import hu.simplexion.adaptive.ui.common.instruction.JustifySelf
import hu.simplexion.adaptive.ui.common.support.RawSurrounding

class LayoutRenderData(
    val adapter : AbstractCommonAdapter<*, *>
) {
    var top: Double? = null
    var left : Double? = null
    var width: Double? = null
    var height: Double? = null

    var padding: RawSurrounding? = null
    var margin: RawSurrounding? = null
    var border: RawSurrounding? = null

    var alignSelf: AlignSelf? = null
    var justifySelf: JustifySelf? = null
}