/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.common.render

import `fun`.adaptive.ui.common.AbstractCommonAdapter
import `fun`.adaptive.ui.common.instruction.Color
import `fun`.adaptive.ui.common.instruction.SPixel

class TextRenderData {

    var fontName: String? = null
    var fontSize: SPixel? = null
    var fontWeight: Int? = null
    var lineHeight: Double? = null
    var letterSpacing: Double? = null
    var italic: Boolean = false
    var underline: Boolean = false
    var smallCaps: Boolean = false
    var noSelect : Boolean? = null
    var color : Color? = null
    var wrap: Boolean = false

    fun toCssString(adapter: AbstractCommonAdapter<*, *>): String {
        val s = mutableListOf<String>()
        if (underline) s += "underline"
        if (smallCaps) s += "small-caps"
        fontWeight?.let { s += it.toString() }
        // FIXME font size in toCSSString does not care about scaling
        (fontSize ?: adapter.defaultTextRenderData.fontSize)?.let { s += it.value.toString() + "px" }
        (fontName ?: adapter.defaultTextRenderData.fontName)?.let { s += "'$it'" }
        return s.joinToString(" ")
    }

}