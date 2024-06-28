/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.fragment

import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.CommonAdapter
import hu.simplexion.adaptive.ui.common.common
import kotlinx.browser.document
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLSpanElement

@AdaptiveActual(common)
open class CommonText(
    adapter: CommonAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AbstractCommonFragment<HTMLElement>(adapter, parent, index, 1, 2) {

    override val receiver: HTMLSpanElement =
        document.createElement("span") as HTMLSpanElement

    private val content: String
        get() = state[0]?.toString() ?: ""

    override fun genPatchInternal(): Boolean {

        patchInstructions()

        if (haveToPatch(dirtyMask, 1)) {
            val content = this.content

            if (receiver.textContent != content) {
                receiver.textContent = content

//                if (uiAdapter.autoSizing) {
//                  renderData.innerWidth = Double.NaN
//                  renderData.innerHeight = Double.NaN
//                }
                measureText(content)
//                }
            }
        }

        return false
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

        val text = renderData.text
        measureContext.font = text?.toCssString(uiAdapter) ?: ""

        val metrics = measureContext.measureText(content)

        renderData.innerWidth = metrics.width
        renderData.innerHeight =
            text?.lineHeight
                ?: (text?.fontSize ?: uiAdapter.defaultTextRenderData.fontSize)?.value?.let { it * 1.5 }
                    ?: (metrics.actualBoundingBoxAscent + metrics.actualBoundingBoxDescent)
    }

    companion object {
        val measureCanvas = document.createElement("canvas") as HTMLCanvasElement
        val measureContext = measureCanvas.getContext("2d") as CanvasRenderingContext2D
    }

}