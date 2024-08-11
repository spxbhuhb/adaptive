/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.common.render

import `fun`.adaptive.ui.common.fragment.layout.RawBorder
import `fun`.adaptive.ui.common.fragment.layout.RawCornerRadius
import `fun`.adaptive.ui.common.fragment.layout.RawDropShadow
import `fun`.adaptive.ui.common.instruction.BackgroundGradient
import `fun`.adaptive.ui.common.instruction.Color
import org.w3c.dom.HTMLElement

object BrowserDecorationApplier : DecorationRenderApplier<HTMLElement>() {

    override fun applyBorder(receiver: HTMLElement, border: RawBorder?) {
        if (border == null) {
            receiver.style.border = "none"
            return
        }

        with(receiver.style) {
            borderStyle = "solid"
            borderColor = border.color.toHexColor()

            val top = border.top

            if (top == border.left && top == border.right && top == border.bottom) {
                borderWidth = border.top.pxs()
            } else {
                borderTopWidth = border.top.pxs()
                borderRightWidth = border.right.pxs()
                borderBottomWidth = border.bottom.pxs()
                borderLeftWidth = border.left.pxs()
            }
        }
    }

    override fun applyCornerRadius(receiver: HTMLElement, cornerRadius: RawCornerRadius?) {
        if (cornerRadius == null) {
            receiver.style.borderRadius = "0"
            return
        }

        with(receiver.style) {

            val topLeft = cornerRadius.topLeft

            if (topLeft == cornerRadius.topRight && topLeft == cornerRadius.bottomLeft && topLeft == cornerRadius.bottomRight) {
                borderRadius = topLeft.pxs()
            } else {
                borderTopLeftRadius = cornerRadius.topLeft.pxs()
                borderTopRightRadius = cornerRadius.topRight.pxs()
                borderBottomLeftRadius = cornerRadius.bottomLeft.pxs()
                borderBottomRightRadius = cornerRadius.bottomRight.pxs()
            }
        }
    }

    override fun applyColor(receiver: HTMLElement, color: Color) {
        receiver.style.backgroundColor = color.toHexColor()
    }

    override fun applyGradient(receiver: HTMLElement, gradient: BackgroundGradient, cornerRadius: RawCornerRadius?) {
        receiver.style.backgroundImage = "linear-gradient(${gradient.degree}deg, ${gradient.start.toHexColor()}, ${gradient.end.toHexColor()})"
    }

    override fun clearBackground(receiver: HTMLElement) {
        receiver.style.background = "transparent"
    }

    override fun applyDropShadow(receiver: HTMLElement, dropShadow: RawDropShadow?) {

        if (dropShadow == null) {
            receiver.style.filter = "none"
            return
        }

        receiver.style.filter =
            "drop-shadow(${dropShadow.color.toHexColor()} ${dropShadow.offsetX.pxs()} ${dropShadow.offsetY.pxs()} ${dropShadow.standardDeviation.pxs()})"
    }
}