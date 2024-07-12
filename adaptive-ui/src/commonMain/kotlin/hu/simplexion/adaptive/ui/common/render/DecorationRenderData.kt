/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.render

import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter
import hu.simplexion.adaptive.ui.common.fragment.layout.RawBorder
import hu.simplexion.adaptive.ui.common.fragment.layout.RawCornerRadius
import hu.simplexion.adaptive.ui.common.fragment.layout.RawDropShadow
import hu.simplexion.adaptive.ui.common.instruction.BackgroundGradient
import hu.simplexion.adaptive.ui.common.instruction.Color

class DecorationRenderData(
    val adapter : AbstractCommonAdapter<*, *>
) {
    var border: RawBorder? = null

    var cornerRadius: RawCornerRadius? = null

    var backgroundColor: Color? = null
    var backgroundGradient : BackgroundGradient? = null

    var dropShadow: RawDropShadow? = null
}