/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.render

import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.fragment.AuiText
import `fun`.adaptive.ui.platform.uiColor
import `fun`.adaptive.ui.render.model.TextRenderData
import kotlinx.cinterop.BetaInteropApi
import platform.Foundation.NSAttributedString
import platform.Foundation.create
import platform.UIKit.NSKernAttributeName
import platform.UIKit.UIFont
import platform.UIKit.UILabel
import platform.UIKit.UIView

@OptIn(BetaInteropApi::class)
fun applyText(fragment: AbstractAuiFragment<UIView>) {
    val view = fragment.receiver
    if (view !is UILabel) return

    val previousData = fragment.previousRenderData
    val currentData = fragment.renderData

    val previous = previousData.text
    val current = currentData.text

    val adapter = fragment.uiAdapter
    val default = adapter.defaultTextRenderData

    // ----  color ----

    val color = TextRenderData.color
    if (color != TextRenderData.color) {
        view.textColor = (color ?: default.color !!).uiColor
    }

    // ----  font ----

    // FIXME font handling in iOS

    val fontSize = TextRenderData.fontSize
    val fontWeight = TextRenderData.fontWeight

    if (fontSize != TextRenderData.fontSize || fontWeight != TextRenderData.fontWeight) {
        val scaledFontSize = adapter.toPx(fontSize ?: default.fontSize !!)
        view.font = when {
            fontWeight != 400 -> UIFont.boldSystemFontOfSize(scaledFontSize)
            else -> UIFont.systemFontOfSize(adapter.toPx(fontSize ?: default.fontSize !!))
        }
    }

    // ----  IMPORTANT content ----
    // ----  letter spacing ----

    val text = (fragment as AuiText).content

    val letterSpacing = TextRenderData.letterSpacing

    if (letterSpacing != TextRenderData.letterSpacing) {

        val attributedString = NSAttributedString.create(
            text,
            mapOf(NSKernAttributeName to letterSpacing)
        )

        view.attributedText = attributedString
    } else {
        view.text = text
    }

}