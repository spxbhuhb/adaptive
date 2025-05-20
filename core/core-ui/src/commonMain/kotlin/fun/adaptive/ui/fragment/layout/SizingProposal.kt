package `fun`.adaptive.ui.fragment.layout

import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.render.model.AuiRenderData

class SizingProposal(
    var minWidth: Double,
    var maxWidth: Double,
    var minHeight: Double,
    var maxHeight: Double
) {

    val containerWidth
        get() = if (maxWidth.isInfinite()) minWidth else maxWidth

    val containerHeight
        get() = if (maxHeight.isInfinite()) minHeight else maxHeight

    fun toItemProposal(adapter: AbstractAuiAdapter<*, *>, renderData: AuiRenderData): SizingProposal {

        val scrollBarSize = adapter.scrollBarSize

        val instructedWidth = renderData.layout?.instructedWidth
        val instructedHeight = renderData.layout?.instructedHeight

        val container = renderData.container
        val horizontalScroll = (container?.horizontalScroll == true)
        val verticalScroll = (container?.verticalScroll == true)

        val proposedMinWidth: Double
        val proposedMaxWidth: Double

        val proposedMinHeight: Double
        val proposedMaxHeight: Double

        val horizontalAdjustment = renderData.surroundingHorizontal + if (verticalScroll) scrollBarSize else 0.0
        val verticalAdjustment = renderData.surroundingVertical + if (horizontalScroll) scrollBarSize else 0.0

        if (instructedWidth != null) {
            proposedMinWidth = instructedWidth - horizontalAdjustment
            proposedMaxWidth = instructedWidth - horizontalAdjustment
        } else {
            proposedMinWidth = minWidth - horizontalAdjustment
            proposedMaxWidth = maxWidth - horizontalAdjustment
        }

        if (instructedHeight != null) {
            proposedMinHeight = instructedHeight - verticalAdjustment
            proposedMaxHeight = instructedHeight - verticalAdjustment
        } else {
            proposedMinHeight = minHeight - verticalAdjustment
            proposedMaxHeight = maxHeight - verticalAdjustment
        }

        return SizingProposal(proposedMinWidth, proposedMaxWidth, proposedMinHeight, proposedMaxHeight)
    }

}