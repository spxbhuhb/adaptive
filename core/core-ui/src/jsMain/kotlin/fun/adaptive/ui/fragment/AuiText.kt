/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.AuiAdapter
import `fun`.adaptive.ui.aui
import kotlinx.browser.document
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLSpanElement
import kotlin.math.ceil

@AdaptiveActual(aui)
open class AuiText(
    adapter: AuiAdapter,
    parent: AdaptiveFragment,
    index: Int,
) : AbstractText<HTMLElement>(adapter, parent, index) {

    override val receiver: HTMLSpanElement =
        document.createElement("span") as HTMLSpanElement

    override fun placeLayout(top: Double, left: Double) {
        super.placeLayout(top, left)
        receiver.style.whiteSpace = "pre"
    }

    override fun measureText(content: String) {

        if (content.isEmpty()) {
            renderData.innerWidth = 0.0
            renderData.innerHeight = 0.0
            return
        }

        // skip measurement when the text is instructed
        val layout = renderData.layout
        if (layout != null && layout.instructedWidth != null && layout.instructedHeight != null) {
            renderData.innerWidth = layout.instructedWidth
            renderData.innerHeight = layout.instructedHeight
            return
        }

        val text = renderData.text ?: uiAdapter.defaultTextRenderData
        measureContext.font = text.toCssString(uiAdapter.defaultTextRenderData)

        val metrics = measureContext.measureText(content)

        // without the 0.05 Firefox and Chrome displays a '...' as they think that there is not enough space
        // I don't really know why that happens, I guess it's some Double rounding issue

        renderData.innerWidth = ceil(metrics.width) + 0.05
        renderData.innerHeight = ceil(
            text.lineHeight
                ?: (text.fontSize ?: uiAdapter.defaultTextRenderData.fontSize)?.value?.let { it * 1.5 }
                ?: (metrics.actualBoundingBoxAscent + metrics.actualBoundingBoxDescent)
        )
    }

    override fun setReceiverContent(content: String) {
        receiver.textContent = content
    }

    override fun getReceiverContent(): String? {
        return receiver.textContent
    }

    companion object {
        val measureCanvas = document.createElement("canvas") as HTMLCanvasElement
        val measureContext = measureCanvas.getContext("2d") as CanvasRenderingContext2D
    }

}