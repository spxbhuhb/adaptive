/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.render

import `fun`.adaptive.ui.fragment.layout.RawBorder
import `fun`.adaptive.ui.fragment.layout.RawCornerRadius
import `fun`.adaptive.ui.fragment.layout.RawDropShadow
import `fun`.adaptive.ui.instruction.decoration.BackgroundGradient
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.decoration.CursorType
import org.w3c.dom.HTMLElement

object BrowserDecorationApplier : DecorationRenderApplier<HTMLElement>() {

    override fun applyBorder(receiver: HTMLElement, border: RawBorder?) {
        if (border == null) {
            receiver.style.border = "none"
            return
        }

        with(receiver.style) {
            borderStyle = "solid"
            borderColor = border.color.hex

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
        receiver.style.backgroundColor = color.hex
    }

    override fun applyGradient(receiver: HTMLElement, gradient: BackgroundGradient, cornerRadius: RawCornerRadius?) {
        receiver.style.backgroundImage = "linear-gradient(${gradient.degree}deg, ${gradient.start.hex}, ${gradient.end.hex})"
    }

    override fun clearBackground(receiver: HTMLElement) {
        receiver.style.background = "transparent"
    }

    override fun applyDropShadow(receiver: HTMLElement, dropShadow: RawDropShadow?) {

        if (dropShadow == null) {
            receiver.style.boxShadow = "none"
            return
        }

        receiver.style.boxShadow = "${dropShadow.offsetX.pxs()} ${dropShadow.offsetY.pxs()} ${dropShadow.standardDeviation.pxs()} ${dropShadow.color.rgba}"
    }

    override fun applyCursor(receiver: HTMLElement, cursor: CursorType?) {
        receiver.style.cursor = when (cursor) {
            CursorType.Pointer -> "pointer"
            CursorType.ColResize -> "col-resize"
            CursorType.RowResize -> "row-resize"
            else -> "auto"
        }
    }
}