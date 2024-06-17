/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.render

import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter
import hu.simplexion.adaptive.ui.common.instruction.*

class TextRenderData(
    val adapter : AbstractCommonAdapter<*, *>
) {
    var fontName: String? = null
    var fontSize: SPixel? = null
    var fontWeight: Int? = null
    var letterSpacing: Double? = null
    var align : TextAlign? = null
    var wrap : TextWrap? = null
    var decoration : TextDecoration? = null
    var noSelect : Boolean? = null
    var color : Color? = null
}