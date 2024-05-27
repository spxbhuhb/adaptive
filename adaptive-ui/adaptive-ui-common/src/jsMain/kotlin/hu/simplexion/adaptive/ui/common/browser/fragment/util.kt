/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.browser.fragment

import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.instruction.AdaptiveUIEvent
import hu.simplexion.adaptive.ui.common.instruction.RenderInstructions
import org.w3c.dom.HTMLElement

fun AdaptiveUIFragment.applyRenderInstructions() {
    renderInstructions = RenderInstructions(instructions)
    // FIXME should clear actual UI settings when null

    tracePatterns = renderInstructions.tracePatterns

    val style = (receiver as HTMLElement).style

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
            p.left?.let { style.paddingLeft = "${it}px"}
            p.top?.let { style.paddingTop = "${it}px"}
            p.right?.let { style.paddingRight = "${it}px"}
            p.bottom?.let { style.paddingBottom = "${it}px"}
        }

        size?.let {
            style.width = "${it.width}px"
            style.height = "${it.height}px"
        }

        onClick?.let {
            // FIXME handling of onClick is wrong on so many levels
            (receiver as HTMLElement).addEventListener(
                "click",
                { onClick!!.handler(AdaptiveUIEvent(this@applyRenderInstructions, it)) }
            )
            style.cursor = "pointer"
        }
    }

}