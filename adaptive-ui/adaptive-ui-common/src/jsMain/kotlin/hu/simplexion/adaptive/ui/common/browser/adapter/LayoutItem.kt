/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.browser.adapter

import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.instruction.BoundingRect
import hu.simplexion.adaptive.ui.common.logic.GridCell
import org.w3c.dom.HTMLElement

class LayoutItem(
    val fragment: AdaptiveUIFragment,
    val receiver: HTMLElement,
    override var rowIndex: Int,
    override var colIndex: Int
) : GridCell {

    fun setFrame(colOffsets: FloatArray, rowOffsets: FloatArray) {

        val uiInstructions = fragment.uiInstructions
        val row = rowIndex
        val col = colIndex
        val rowSpan = fragment.uiInstructions.rowSpan
        val colSpan = fragment.uiInstructions.colSpan

        uiInstructions.frame = BoundingRect(
            colOffsets[col],
            rowOffsets[row],
            colOffsets[col + colSpan] - colOffsets[col],
            rowOffsets[row + rowSpan] - rowOffsets[row]
        )
    }

    fun setAbsolutePosition() {
        val frame = fragment.frame
        val style = receiver.style

        style.position = "absolute"
        style.boxSizing = "border-box"
        style.top = "${frame.y}px"
        style.left = "${frame.x}px"
        style.width = "${frame.width}px"
        style.height = "${frame.height}px"
    }

    override val gridRow: Int?
        get() = fragment.uiInstructions.gridRow
    override val gridCol: Int?
        get() = fragment.uiInstructions.gridCol
    override val rowSpan: Int
        get() = fragment.uiInstructions.rowSpan
    override val colSpan: Int
        get() = fragment.uiInstructions.colSpan

    override fun toString(): String =
        "$fragment row:$rowIndex col:$colIndex"

}