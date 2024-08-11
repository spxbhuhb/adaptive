/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.common.render

import `fun`.adaptive.ui.common.AbstractCommonAdapter
import `fun`.adaptive.ui.common.fragment.layout.RawBorder
import `fun`.adaptive.ui.common.fragment.layout.RawCornerRadius
import `fun`.adaptive.ui.common.fragment.layout.RawDropShadow
import `fun`.adaptive.ui.common.instruction.BackgroundGradient
import `fun`.adaptive.ui.common.instruction.Color

class DecorationRenderData(
    val adapter : AbstractCommonAdapter<*, *>
) {
    var border: RawBorder? = null

    var cornerRadius: RawCornerRadius? = null

    var backgroundColor: Color? = null
    var backgroundGradient : BackgroundGradient? = null

    var dropShadow: RawDropShadow? = null
}