/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.fragment.paragraph

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.AuiBrowserAdapter
import `fun`.adaptive.ui.aui
import `fun`.adaptive.ui.fragment.layout.SizingProposal
import `fun`.adaptive.ui.fragment.paragraph.items.LinkParagraphItem
import `fun`.adaptive.ui.fragment.paragraph.items.TextParagraphItem
import `fun`.adaptive.ui.fragment.paragraph.model.ParagraphItem
import `fun`.adaptive.ui.render.BrowserDecorationApplier
import `fun`.adaptive.ui.render.BrowserLayoutApplier
import `fun`.adaptive.ui.render.BrowserTextApplier
import `fun`.adaptive.ui.render.model.AuiRenderData
import `fun`.adaptive.ui.render.model.TextRenderData
import kotlinx.browser.document
import kotlinx.dom.clear
import org.w3c.dom.*
import kotlin.math.ceil

@AdaptiveActual(aui)
class AuiParagraph(
    adapter: AuiBrowserAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AbstractParagraph<HTMLElement, HTMLDivElement>(adapter, parent, declarationIndex) {

    override val receiver: HTMLElement = document.createElement("div") as HTMLDivElement

    class RenderCacheEntry(
        val renderData: AuiRenderData,
        val textRenderData: TextRenderData,
        val context: CanvasRenderingContext2D
    )

    var renderCache = arrayOfNulls<RenderCacheEntry?>(0)

    override fun computeLayout(
        proposal: SizingProposal
    ) {
        renderCache = arrayOfNulls(instructionSets.size)
        super.computeLayout(proposal)
    }

    override fun auiPatchInternal() {

    }

    override fun placeRows(rows: List<Row>) {
        receiver.clear()

        var topOffset = 0.0

        for (row in rows) {
            var leftOffset = 0.0

            for (item in row.items) {
                when (item) {
                    is TextParagraphItem -> addSpan(item, leftOffset, topOffset)
                    is LinkParagraphItem -> addAnchor(item, leftOffset, topOffset)
                    else -> continue
                }
                leftOffset += item.width
            }

            topOffset += row.height
        }
    }

    fun addSpan(item: TextParagraphItem, leftOffset: Double, topOffset: Double) {
        val span = document.createElement("span") as HTMLElement
        span.innerText = item.text
        addElement(span, item, leftOffset, topOffset)
    }

    fun addAnchor(item: LinkParagraphItem, leftOffset: Double, topOffset: Double) {
        val anchor = document.createElement("a") as HTMLAnchorElement
        anchor.innerText = item.text
        anchor.href = item.href
        addElement(anchor, item, leftOffset, topOffset)
    }

    fun addElement(
        element: HTMLElement,
        item: ParagraphItem,
        leftOffset: Double,
        topOffset: Double
    ) {
        val entry = getEntry(item.instructionSetIndex)
        val renderData = entry.renderData

        // FIXME I'm not sure about how to handle margin here, not important right now
        with(element.style) {
            position = "absolute"
            left = "${leftOffset}px"
            top = "${topOffset}px"
            width = "${ceil(item.width)}px" // ceil to avoid Safari ... at the end of text
            height = "${item.height}px"
            whiteSpace = "pre"
        }

        BrowserLayoutApplier.applyTo(element, null, renderData.layout)
        BrowserDecorationApplier.applyTo(element, null, renderData.decoration)
        BrowserTextApplier.applyTo(element, null, renderData.text, uiAdapter.defaultTextRenderData)

        receiver.appendChild(element)
    }

    override fun measureText(item: ParagraphItem, text: String, instructionSetIndex: Int) {

        if (text.isEmpty()) {
            item.width = 0.0
            item.height = 0.0
            item.baseline = 0.0
            return
        }

        val entry = getEntry(instructionSetIndex)
        val metrics = entry.context.measureText(text)

        item.width = entry.renderData.surroundingHorizontal + metrics.width

        item.height = entry.renderData.surroundingVertical + (
            entry.textRenderData.lineHeight
                ?: (entry.textRenderData.fontSize ?: uiAdapter.defaultTextRenderData.fontSize)?.value?.let { it * 1.5 }
                ?: (metrics.actualBoundingBoxAscent + metrics.actualBoundingBoxDescent)
        )

        println("width: ${item.width}, height: ${item.height}, text: $text")
    }

    fun getEntry(index: Int): RenderCacheEntry {
        val cached = renderCache[index]
        if (cached != null) return cached

        val renderData = AuiRenderData(uiAdapter, null, instructionSets[index])
        val context = measureCanvas.getContext("2d") as CanvasRenderingContext2D
        val textRenderData = renderData.text ?: uiAdapter.defaultTextRenderData

        RenderCacheEntry(
            renderData,
            textRenderData,
            context
        ).also {
            renderCache[index] = it
            context.font = textRenderData.toCssString(uiAdapter.defaultTextRenderData)
            return it
        }
    }

    companion object {
        val measureCanvas = document.createElement("canvas") as HTMLCanvasElement
    }

}