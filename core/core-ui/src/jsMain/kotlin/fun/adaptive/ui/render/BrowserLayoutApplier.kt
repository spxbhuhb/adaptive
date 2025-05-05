/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.render

import `fun`.adaptive.ui.fragment.layout.RawSurrounding
import `fun`.adaptive.ui.instruction.layout.OverflowBehavior
import org.w3c.dom.HTMLElement

object BrowserLayoutApplier : LayoutRenderApplier<HTMLElement>() {

    override fun applyPadding(receiver: HTMLElement, padding: RawSurrounding?) {
        if (padding == null) {
            receiver.style.padding = "0"
            return
        }

        with(receiver.style) {
            paddingTop = padding.top.pxs()
            paddingLeft = padding.start.pxs()
            paddingBottom = padding.bottom.pxs()
            paddingRight = padding.end.pxs()
        }
    }

    override fun applyMargin(receiver: HTMLElement, margin: RawSurrounding?) {
        if (margin == null) {
            receiver.style.margin = "0"
            return
        }

        with(receiver.style) {
            marginTop = margin.top.pxs()
            marginLeft = margin.start.pxs()
            marginBottom = margin.bottom.pxs()
            marginRight = margin.end.pxs()
        }
    }

    override fun applyZIndex(receiver: HTMLElement, zIndex: Int?) {
        if (zIndex == null) {
            receiver.style.zIndex = "auto"
        } else {
            receiver.style.zIndex = "$zIndex"
        }
    }

    override fun applyFixed(receiver: HTMLElement, fixed: Boolean?) {
        if (fixed == null) {
            receiver.style.position = "absolute"
        } else {
            receiver.style.position = if (fixed) "fixed" else "absolute"
        }
    }

    override fun applyOverflow(receiver: HTMLElement, overflow: OverflowBehavior?) {
        if (overflow == null) {
            receiver.style.overflowX = "hidden"
        } else {
            receiver.style.position = overflow.name
        }
    }

}