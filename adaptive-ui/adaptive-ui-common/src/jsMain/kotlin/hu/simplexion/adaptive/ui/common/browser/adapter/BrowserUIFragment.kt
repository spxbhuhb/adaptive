/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.adapter

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.instruction.AdaptiveUIEvent
import hu.simplexion.adaptive.ui.common.instruction.Frame
import org.w3c.dom.HTMLElement

abstract class BrowserUIFragment(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    instructionsIndex: Int,
    stateSize: Int
) : AdaptiveUIFragment(adapter, parent, declarationIndex, instructionsIndex, stateSize) {

    override lateinit var receiver: HTMLElement

    abstract fun makeReceiver(): HTMLElement

    override fun create() {
        receiver = makeReceiver()
        super.create()
    }

    override fun layout(proposedFrame : Frame) {
        super.layout(proposedFrame)

        applyRenderInstructions()

        val layoutFrame = renderInstructions.layoutFrame

        check(layoutFrame !== Frame.NaF) { "Missing layout frame in $this" }

        val style = receiver.style

        val point = layoutFrame.point
        val size = layoutFrame.size

        style.position = "absolute"
        style.boxSizing = "border-box"
        style.top = "${point.top}px"
        style.left = "${point.left}px"
        style.width = "${size.width}px"
        style.height = "${size.height}px"
    }

    fun applyRenderInstructions() {
        if (renderInstructions.tracePatterns.isNotEmpty()) {
            tracePatterns = renderInstructions.tracePatterns
        }

        val style = receiver.style

        with(renderInstructions) {
            // FIXME use classes (when possible) when applying render instructions to HTML element
            backgroundColor?.let { style.backgroundColor = it.toHexColor() }
            backgroundGradient?.let { style.background = "linear-gradient(${it.degree}deg, ${it.start.toHexColor()}, ${it.end.toHexColor()})" }

            border?.let { style.border = "${it.width}px solid ${it.color.toHexColor()}" }
            borderRadius?.let { style.borderRadius = "${it}px" }

            color?.let { style.color = it.toHexColor() }

            fontSize?.let { style.fontSize = "${it}px" }
            fontWeight?.let { style.fontWeight = it.toString() }
            letterSpacing?.let { style.letterSpacing = "${it}px" }

            textAlign?.let {
                style.textAlign = it.name.lowercase()
            }

            textWrap?.let {
                style.setProperty("text-wrap", it.toString().lowercase())
            }

            padding?.let { p ->
                p.left?.let { style.paddingLeft = "${it}px" }
                p.top?.let { style.paddingTop = "${it}px" }
                p.right?.let { style.paddingRight = "${it}px" }
                p.bottom?.let { style.paddingBottom = "${it}px" }
            }

            instructedSize?.let {
                style.width = "${it.width}px"
                style.height = "${it.height}px"
            }

            onClick?.let {
                // FIXME handling of onClick is wrong on so many levels
                (receiver as HTMLElement).addEventListener(
                    "click",
                    { onClick !!.handler(AdaptiveUIEvent(this@BrowserUIFragment, it)) }
                )
                style.cursor = "pointer"
            }
        }
    }
}