/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.browser.fragment

import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.instruction.RenderInstructions
import org.w3c.dom.HTMLElement

fun AdaptiveUIFragment.applyRenderInstructions() {
    renderInstructions = RenderInstructions(instructions)
    // FIXME should clear actual UI settings when null

    trace = renderInstructions.trace

    val style = (receiver as HTMLElement).style

    with(renderInstructions) {
        // FIXME use classes (when possible) when applying render instructions to HTML element
        backgroundColor?.let { style.backgroundColor = it.toHexColor() }
        backgroundGradient?.let { style.background = "linear-gradient(${it.degree}deg, ${it.start.toHexColor()}, ${it.end.toHexColor()})" }
        border?.let { style.border = "${it.width}px solid ${it.color.toHexColor()}" }
        borderRadius?.let { style.borderRadius = "${it}px" }
        color?.let { style.color = it.toHexColor() }
        padding?.let { style.padding = "${it}px" }
    }

}