/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.render

import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter
import hu.simplexion.adaptive.ui.common.instruction.*
import hu.simplexion.adaptive.ui.common.support.RawSurrounding

class DecorationRenderData(
    val adapter : AbstractCommonAdapter<*, *>
) {
    var borderColor: Color? = null
    var borderRadius: BorderRadius? = null

    var backgroundColor: Color? = null
    var backgroundGradient : BackgroundGradient? = null

    var dropShadow : DropShadow? = null
}