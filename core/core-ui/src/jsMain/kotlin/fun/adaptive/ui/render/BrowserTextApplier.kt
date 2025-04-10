/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.render

import `fun`.adaptive.ui.instruction.SPixel
import `fun`.adaptive.ui.instruction.decoration.Color
import org.w3c.dom.HTMLElement

object BrowserTextApplier : TextRenderApplier<HTMLElement>() {

    override fun applyFontName(receiver: HTMLElement, fontName: String?) {
        if (fontName == null) {
            receiver.style.fontFamily = "inherit"
        } else {
            receiver.style.fontFamily = fontName
        }
    }

    override fun applyFontSize(receiver: HTMLElement, fontSize: SPixel?) {
        if (fontSize == null) {
            receiver.style.fontSize = "inherit"
        } else {
            receiver.style.fontSize = fontSize.value.pxs()
        }
    }

    override fun applyFontWeight(receiver: HTMLElement, fontWeight: Int?) {
        if (fontWeight == null) {
            receiver.style.fontWeight = "inherit"
        } else {
            receiver.style.fontWeight = fontWeight.toString()
        }
    }

    override fun applyLetterSpacing(receiver: HTMLElement, letterSpacing: Double?) {
        if (letterSpacing == null) {
            receiver.style.fontWeight = "inherit"
        } else {
            receiver.style.fontWeight = "${letterSpacing}em"
        }
    }

    override fun applyWrap(receiver: HTMLElement, wrap: Boolean?) {
        receiver.style.setProperty("text-wrap", if (wrap == true) "wrap" else "nowrap")
    }

    override fun applyColor(receiver: HTMLElement, color: Color?) {
        if (color == null) {
            receiver.style.color = "inherit"
        } else {
            receiver.style.color = color.hex
        }
    }

    override fun applySmallCaps(receiver: HTMLElement, smallCaps: Boolean?) {
        if (smallCaps != true) {
            receiver.style.fontVariant = ""
        } else {
            receiver.style.fontVariant = "small-caps"
        }
    }

    override fun applyNoSelect(receiver: HTMLElement, noSelect: Boolean?) {
        if (noSelect == true) {
            with(receiver.style) {
                setProperty("-webkit-user-select", "none")
                setProperty("user-select", "none")
                cursor = "default"
            }
        }
    }


}