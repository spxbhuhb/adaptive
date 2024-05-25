/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.browser.adapter

import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.instruction.Frame
import hu.simplexion.adaptive.ui.common.logic.GridCell
import org.w3c.dom.HTMLElement

class LayoutItem(
    val fragment: AdaptiveUIFragment,
    val receiver: HTMLElement,
    override var rowIndex: Int,
    override var colIndex: Int
) : GridCell {

    var layoutFrame
        get() = fragment.renderInstructions.layoutFrame
        set(v) { fragment.renderInstructions.layoutFrame = v }

    var frame
        get() = fragment.renderInstructions.frame
        set(v) { fragment.renderInstructions.frame = v }

    override val gridRow: Int?
        get() = fragment.renderInstructions.gridRow

    override val gridCol: Int?
        get() = fragment.renderInstructions.gridCol

    override val rowSpan: Int
        get() = fragment.renderInstructions.rowSpan

    override val colSpan: Int
        get() = fragment.renderInstructions.colSpan

    override fun toString(): String =
        "$fragment row[index,spec,span]=[$rowIndex,$gridRow,$rowSpan] col[index,spec,span]=[$colIndex,$gridCol,$colSpan]"

    fun measure() {
        fragment.measure()
    }

    fun setFrame(colOffsets: FloatArray, rowOffsets: FloatArray) {

        val uiInstructions = fragment.renderInstructions
        val row = rowIndex
        val col = colIndex
        val rowSpan = uiInstructions.rowSpan
        val colSpan = uiInstructions.colSpan

        layoutFrame = Frame(
            rowOffsets[row],
            colOffsets[col],
            colOffsets[col + colSpan] - colOffsets[col],
            rowOffsets[row + rowSpan] - rowOffsets[row]
        )
    }
}