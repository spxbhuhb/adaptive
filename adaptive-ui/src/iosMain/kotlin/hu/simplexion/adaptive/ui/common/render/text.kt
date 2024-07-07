/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.render

import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.fragment.CommonText
import hu.simplexion.adaptive.ui.common.platform.uiColor
import kotlinx.cinterop.BetaInteropApi
import platform.Foundation.NSAttributedString
import platform.Foundation.create
import platform.UIKit.NSKernAttributeName
import platform.UIKit.UIFont
import platform.UIKit.UILabel
import platform.UIKit.UIView

@OptIn(BetaInteropApi::class)
fun applyText(fragment: AbstractCommonFragment<UIView>) {
    val view = fragment.receiver
    if (view !is UILabel) return

    val previousData = fragment.previousRenderData
    val currentData = fragment.renderData

    val previous = previousData.text
    val current = currentData.text

    val adapter = fragment.uiAdapter
    val default = adapter.defaultTextRenderData

    // ----  color ----

    val color = current?.color
    if (color != previous?.color) {
        view.textColor = (color ?: default.color !!).uiColor
    }

    // ----  font ----

    // FIXME font handling in iOS

    val fontSize = current?.fontSize
    val fontWeight = current?.fontWeight

    if (fontSize != previous?.fontSize || fontWeight != previous?.fontWeight) {
        val scaledFontSize = adapter.toPx(fontSize ?: default.fontSize !!)
        view.font = when {
            fontWeight != 400 -> UIFont.boldSystemFontOfSize(scaledFontSize)
            else -> UIFont.systemFontOfSize(adapter.toPx(fontSize ?: default.fontSize !!))
        }
    }

    // ----  IMPORTANT content ----
    // ----  letter spacing ----

    val text = (fragment as CommonText).content

    val letterSpacing = current?.letterSpacing

    if (letterSpacing != previous?.letterSpacing) {

        val attributedString = NSAttributedString.create(
            text,
            mapOf(NSKernAttributeName to letterSpacing)
        )

        view.attributedText = attributedString
    } else {
        view.text = text
    }

}