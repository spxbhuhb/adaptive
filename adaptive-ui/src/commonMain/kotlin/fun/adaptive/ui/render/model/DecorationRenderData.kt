/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.render.model

import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.fragment.layout.RawBorder
import `fun`.adaptive.ui.fragment.layout.RawCornerRadius
import `fun`.adaptive.ui.fragment.layout.RawDropShadow
import `fun`.adaptive.ui.instruction.decoration.BackgroundGradient
import `fun`.adaptive.ui.instruction.decoration.Color

class DecorationRenderData(
    val adapter : AbstractAuiAdapter<*, *>
) {
    var border: RawBorder? = null

    var cornerRadius: RawCornerRadius? = null

    var backgroundColor: Color? = null
    var backgroundGradient : BackgroundGradient? = null

    var dropShadow: RawDropShadow? = null
}