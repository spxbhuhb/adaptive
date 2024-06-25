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
    var fontStyle: FontStyle? = null
    var fontVariant: FontVariant? = null
    var lineHeight: Double? = null
    var letterSpacing: Double? = null
    var align : TextAlign? = null
    var wrap : TextWrap? = null
    var decoration : TextDecoration? = null
    var noSelect : Boolean? = null
    var color : Color? = null

    fun toCssString(): String {
        val s = mutableListOf<String>()
        decoration?.let { s += it.value }
        fontVariant?.let { s += it.value }
        fontWeight?.let { s += it.toString() }
        fontSize?.let { s += it.value.toString() + "px" } // FIXME font size in toCSSString does not care about scaling
        fontName?.let { s += "'$it'" }
        return s.joinToString(" ")
    }

}