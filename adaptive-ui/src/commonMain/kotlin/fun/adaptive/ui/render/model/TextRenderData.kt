/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.render.model

import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.instruction.SPixel
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.sp

class TextRenderData {

    var fontName: String? = null
    var fontSize: SPixel? = null
    var fontWeight: Int? = null
    var lineHeight: Double? = null
    var letterSpacing: Double? = null
    var italic: Boolean = false
    var underline: Boolean = false
    var smallCaps: Boolean = false
    var noSelect: Boolean? = null
    var color: Color? = null
    var wrap: Boolean = true

    fun toCssString(adapter: AbstractAuiAdapter<*, *>): String {
        val s = mutableListOf<String>()
        if (underline) s += "underline"
        if (smallCaps) s += "small-caps"
        s += "normal"
        s += (fontWeight ?: adapter.defaultTextRenderData.fontWeight ?: "300").toString()
        s += (fontSize?.value ?: adapter.defaultTextRenderData.fontSize?.value ?: 16).toString() + "px"  // FIXME font size in toCSSString does not care about scaling
        s += "'${fontName ?: adapter.defaultTextRenderData.fontName ?: "Open Sans"}'"
        // FIXME missing letter spacing
        return s.joinToString(" ")
    }

}