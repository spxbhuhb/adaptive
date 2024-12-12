/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.AbstractAuiFragment
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
) : AbstractAuiFragment<HTMLElement>(adapter, parent, index, 1, 2) {

    override val receiver: HTMLSpanElement =
        document.createElement("span") as HTMLSpanElement

    private val content: String
        get() = state[0]?.toString() ?: ""

    override fun auiPatchInternal() {

        if (renderData.text == null) {
            renderData.text = uiAdapter.defaultTextRenderData
        }

        val content = this.content
        val contentChange = (isInit || content != receiver.textContent)
        val styleChange = (renderData.text != previousRenderData.text)

        if (! haveToPatch(dirtyMask, 1) && ! contentChange && ! styleChange) {
            return
        }

        if (contentChange) {
            receiver.textContent = content
        }

        measureText(content)
    }

    override fun placeLayout(top: Double, left: Double) {
        super.placeLayout(top, left)
        receiver.style.whiteSpace = "pre"
    }

    fun measureText(content: String) {

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
        measureContext.font = text.toCssString(uiAdapter)

        val metrics = measureContext.measureText(content)

        renderData.innerWidth = ceil(metrics.width)
        renderData.innerHeight = ceil(
            text.lineHeight
                ?: (text.fontSize ?: uiAdapter.defaultTextRenderData.fontSize)?.value?.let { it * 1.5 }
                ?: (metrics.actualBoundingBoxAscent + metrics.actualBoundingBoxDescent)
        )
    }

    companion object {
        val measureCanvas = document.createElement("canvas") as HTMLCanvasElement
        val measureContext = measureCanvas.getContext("2d") as CanvasRenderingContext2D
    }

}