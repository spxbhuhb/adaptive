/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.fragment.layout

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.instruction.layout.Alignment
import `fun`.adaptive.ui.render.model.LayoutRenderData
import kotlin.math.max

abstract class AbstractColumn<RT, CRT : RT>(
    adapter: AbstractAuiAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int
) : AbstractStack<RT, CRT>(
    adapter, parent, declarationIndex, 0, 2
) {

    override fun itemsWidthCalc(itemsWidth: Double, item: AbstractAuiFragment<RT>): Double =
        max(itemsWidth, item.renderData.finalWidth)

    override fun itemsHeightCalc(itemsHeight: Double, item: AbstractAuiFragment<RT>): Double =
        itemsHeight + item.renderData.finalHeight

    override fun instructedGap(): Double =
        renderData.container?.gapHeight ?: 0.0

    override fun freeSpace(innerWidth: Double, itemsWidth: Double, innerHeight: Double, itemsHeight: Double): Double =
        innerHeight - itemsHeight

    override fun startOffset(): Double =
        renderData.surroundingTop

    override fun mainAxisAlignment(): Alignment? =
        renderData.container?.verticalAlignment

    override fun crossAxisAlignment(): Alignment? =
        renderData.container?.horizontalAlignment

    override fun crossAxisSize(innerWidth: Double, innerHeight: Double): Double =
        innerWidth

    override fun AbstractAuiFragment<RT>.place(crossAxisAlignment: Alignment?, crossAxisSize: Double, offset: Double): Double {

        val innerLeft = alignOnAxis(
            horizontalAlignment,
            crossAxisAlignment,
            crossAxisSize,
            renderData.finalWidth
        )

        placeLayout(offset, innerLeft + this@AbstractColumn.renderData.surroundingStart)

        return renderData.finalHeight
    }

    override fun layoutChange(fragment: AbstractAuiFragment<*>) {
        val previous = fragment.previousRenderData.layout ?: LayoutRenderData(uiAdapter)
        val current = fragment.renderData.layout ?: LayoutRenderData(uiAdapter)

        val currentInstructedWidth = current.instructedWidth

        if (previous.instructedWidth != currentInstructedWidth) {

            if (currentInstructedWidth != null && currentInstructedWidth > renderData.finalWidth) {
                // The instructed width of the contained fragment changed, and it does not fit to the
                // current height of the column. We have to resize the column to fit the changed fragment.

                // TODO if the row has it's own instructed height it might limit the height of the fragment
                // in that case the row height won't change and we don't have re-layout the layout that contains the row
                super.layoutChange(this)
                return
            }

        }

        val currentInstructedHeight = current.instructedHeight

        if (previous.instructedHeight != currentInstructedHeight) {
            // TODO inefficient width change re-layout
            super.layoutChange(this)
            return
        }

        // This part should be reached only if the container can handle the layout change by itself.
        // This means that the final dimensions of the component won't change, no matter what.

        computeLayout(renderData.finalWidth, renderData.finalHeight)

        if (renderData.finalWidth != previousRenderData.finalWidth || renderData.finalHeight != previousRenderData.finalHeight) {
            super.layoutChange(this)
        }
    }
}