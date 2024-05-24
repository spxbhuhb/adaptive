/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.browser.fragment

import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.instruction.UIInstructions
import org.w3c.dom.HTMLElement

fun AdaptiveUIFragment.applyUIInstructions() {
    uiInstructions = UIInstructions(instructions)

    val style = (receiver as HTMLElement).style

    uiInstructions.color?.let { style.color = it.toHexColor() }

    uiInstructions.backgroundColor?.let { style.backgroundColor = it.toHexColor() }

    uiInstructions.backgroundGradient?.let { style.background = "linear-gradient(${it.degree}deg, ${it.start.toHexColor()}, ${it.end.toHexColor()})" }

    uiInstructions.border?.let { style.border = "${it.width}px solid ${it.color.toHexColor()}" }

    uiInstructions.borderRadius?.let { style.borderRadius = "${it}px" }

}