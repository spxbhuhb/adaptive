package `fun`.adaptive.iot.history.ui.chart

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.graphics.canvas.api.canvas
import `fun`.adaptive.iot.generated.resources.noHistoricalData
import `fun`.adaptive.iot.history.ui.HistoryContentController
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.alignSelf
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.textColors

@Adaptive
fun historyChart(controller: HistoryContentController) {

    val context = valueFrom { controller.chartContext }

    box {
        maxSize .. padding { 16.dp } .. backgrounds.surface .. cornerRadius { 4.dp }

        if (context.items.isEmpty() || context.items.all { it.sourceData.isEmpty() }) {
            text(Strings.noHistoricalData) .. alignSelf.center .. textColors.onSurfaceVariant
        } else {
            canvas { canvasSize ->

                for (axis in context.axes) {
                    actualize(axis.renderer, emptyInstructions, context, axis, canvasSize)
                }

                for (item in context.items) {
                    actualize(item.renderKey, emptyInstructions, context, item, canvasSize)
                }
            }
        }
    }
}