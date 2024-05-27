/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.browser.adapter

import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.adapter.LayoutItem
import hu.simplexion.adaptive.ui.common.instruction.Frame
import hu.simplexion.adaptive.ui.common.logic.GridCell
import org.w3c.dom.HTMLElement

open class BrowserLayoutItem(
    fragment: AdaptiveUIFragment,
    val receiver: HTMLElement,
    rowIndex: Int,
    colIndex: Int
) : LayoutItem(fragment, rowIndex, colIndex) {


    fun layout() {
        val layoutFrame = fragment.renderInstructions.layoutFrame
        val style = receiver.style

        check(layoutFrame !== Frame.NaF) // ops

        style.position = "absolute"
        style.boxSizing = "border-box"
        style.top = "${layoutFrame.top}px"
        style.left = "${layoutFrame.left}px"
        style.width = "${layoutFrame.width}px"
        style.height = "${layoutFrame.height}px"
    }

}