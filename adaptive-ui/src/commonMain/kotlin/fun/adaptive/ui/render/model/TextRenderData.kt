/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.render.model

import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.instruction.SPixel
import `fun`.adaptive.ui.instruction.decoration.Color

@Suppress("EqualsOrHashCode")
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

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other == null) return false
        if (other !is TextRenderData) return false

        if (fontName != other.fontName) return false
        if (fontSize != other.fontSize) return false
        if (fontWeight != other.fontWeight) return false
        if (lineHeight != other.lineHeight) return false
        if (letterSpacing != other.letterSpacing) return false
        if (italic != other.italic) return false
        if (underline != other.underline) return false
        if (smallCaps != other.smallCaps) return false
        if (noSelect != other.noSelect) return false
        if (color != other.color) return false
        if (wrap != other.wrap) return false

        return true
    }

    fun toCssString(default : TextRenderData?): String {
        val s = mutableListOf<String>()
        if (underline) s += "underline"
        if (smallCaps) s += "small-caps"
        s += "normal"
        s += (fontWeight ?: default?.fontWeight ?: "300").toString()
        s += (fontSize?.value ?: default?.fontSize?.value ?: 16).toString() + "px"  // FIXME font size in toCSSString does not care about scaling
        s += "'${fontName ?: default?.fontName ?: "Open Sans"}'"
        // FIXME missing letter spacing
        return s.joinToString(" ")
    }

}