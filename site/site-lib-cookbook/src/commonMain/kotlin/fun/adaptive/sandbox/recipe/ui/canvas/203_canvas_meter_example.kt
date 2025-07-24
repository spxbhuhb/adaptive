package `fun`.adaptive.sandbox.recipe.ui.canvas

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.producer.poll
import `fun`.adaptive.graphics.canvas.api.*
import `fun`.adaptive.graphics.canvas.model.path.Arc
import `fun`.adaptive.graphics.canvas.model.path.LineTo
import `fun`.adaptive.graphics.canvas.model.path.MoveTo
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.borders
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.time.Clock.System.now
import kotlin.time.Duration.Companion.milliseconds
/**
 * # A meter
 * 
 * This example shows how to create a circular meter with a needle that points to the current value.
 * The meter has three zones: green, yellow, and red.
 * It also includes ticks and labels on the outside of the zones.
 */
@Adaptive
fun canvasMeterExample() : AdaptiveFragment {
    // Create an instance of the meter parameters
    val meterParams = MeterParams()
    
    // Current value to display on the meter
    val currentValue = poll(1000.milliseconds) { 
        (now().toEpochMilliseconds() % 240).toDouble()
    } ?: 67.0
    
    // Calculate the angle for the current value
    val valueRatio = (currentValue - meterParams.minValue) / (meterParams.maxValue - meterParams.minValue)
    val valueAngle = meterParams.startAngle + (meterParams.endAngle - meterParams.startAngle) * valueRatio
    
    box {
        size(402.dp, 402.dp) .. borders.outline

        canvas {
            maxSize

            // Draw ticks and labels
            for (i in 0..meterParams.numTicks) {
                tick(
                    i,
                    meterParams.numTicks,
                    meterParams.startAngle,
                    meterParams.endAngle,
                    meterParams.minValue,
                    meterParams.maxValue,
                    meterParams.centerX,
                    meterParams.centerY,
                    meterParams.innerRadius,
                    meterParams.outerRadius
                )
            }

            // Iterate through each section and draw it
            for (section in meterParams.sections) {
                drawZone(
                    meterParams.centerX,
                    meterParams.centerY,
                    meterParams.outerRadius,
                    meterParams.startAngle + (meterParams.endAngle - meterParams.startAngle) * section.first,
                    meterParams.startAngle + (meterParams.endAngle - meterParams.startAngle) * section.second,
                    section.third
                )
            }

            // Draw the inner circle to create the arc effect
            circle(meterParams.centerX, meterParams.centerY, meterParams.innerRadius) .. fill(Color(0xFFFFFFu))

            // Draw the outer border
            //circle(meterParams.centerX, meterParams.centerY, meterParams.outerRadius) .. stroke(Color(0x000000u))

            // Draw the inner border
            //circle(meterParams.centerX, meterParams.centerY, meterParams.innerRadius) .. stroke(Color(0x000000u))

            // Draw the needle
            transform {
                translate(meterParams.centerX, meterParams.centerY) .. rotate(valueAngle)

                // Needle body
                line(0.0, 0.0, meterParams.innerRadius - 10, 0.0) .. stroke(Color(0xFF0000u, 0.8))

                // Needle point
                line(meterParams.innerRadius - 10, 0.0, meterParams.outerRadius - 5, 0.0) .. stroke(Color(0xFF0000u))

                // Needle base circle
                circle(0.0, 0.0, 5.0) .. fill(Color(0x000000u))
            }

            // Draw the current value text
            fillText(meterParams.centerX, meterParams.centerY + 50, "Value: ${currentValue.toInt()}") .. fill(Color(0x000000u))
        }
    }

    return fragment()
}


// Define a class to hold meter parameters
class MeterParams(
    val startAngle: Double = 0.0,
    val endAngle: Double = PI,
    val minValue: Double = 0.0,
    val maxValue: Double = 240.0,
    val outerRadius: Double = 150.0,
    val innerRadius: Double = 100.0,
    val centerX: Double = 200.0,
    val centerY: Double = 200.0,
    val numTicks: Int = 12,
    val sections: List<Triple<Double,Double, Color>> = listOf(
        Triple(0.0, 0.5, Color(0x00AA00u, 0.5)),  // First 50% is green
        Triple(0.5, 0.8, Color(0xFFAA00u, 0.5)),  // Next 30% is yellow
        Triple(0.8, 1.0, Color(0xAA0000u, 0.5))   // Last 20% is red
    )
)

/**
 * Draws a zone segment of the meter.
 */
@Adaptive
fun drawZone(
    centerX: Double,
    centerY: Double,
    outerRadius: Double,
    startAngle: Double,
    endAngle: Double,
    color: Color
) {
    // Calculate start and end points for the arc
    val startX = centerX + outerRadius * cos(startAngle)
    val startY = centerY + outerRadius * sin(startAngle)
    val endX = centerX + outerRadius * cos(endAngle)
    val endY = centerY + outerRadius * sin(endAngle)
    
    // Create path commands for the segment
    path(
        listOf(
            MoveTo(centerX, centerY),
            LineTo(startX, startY),
            Arc(
                rx = outerRadius,
                ry = outerRadius,
                xAxisRotation = 0.0,
                largeArcFlag = if (endAngle - startAngle > PI) 1 else 0,
                sweepFlag = 1, // Clockwise
                x2 = endX,
                y2 = endY,
                x1 = startX,
                y1 = startY
            ),
            LineTo(centerX, centerY)
        )
    ) .. fill(color)
}

@Adaptive
fun tick(
    i: Int,
    numTicks: Int,
    startAngle: Double,
    endAngle: Double,
    minValue: Double,
    maxValue: Double,
    centerX: Double,
    centerY: Double,
    innerRadius: Double,
    outerRadius: Double
) {
    val tickRatio = i.toDouble() / numTicks
    val tickAngle = startAngle + (endAngle - startAngle) * tickRatio
    val tickValue = minValue + (maxValue - minValue) * tickRatio

    // Calculate tick positions
    val innerTickX = centerX + innerRadius * cos(tickAngle)
    val innerTickY = centerY + innerRadius * sin(tickAngle)
    val outerTickX = centerX + (outerRadius + 10) * cos(tickAngle)
    val outerTickY = centerY + (outerRadius + 10) * sin(tickAngle)

    val labelX = centerX + (outerRadius + 25) * cos(tickAngle)
    val labelY = centerY + (outerRadius + 25) * sin(tickAngle)

    // Draw tick line
    line(innerTickX, innerTickY, outerTickX, outerTickY) .. stroke(Color(0x000000u))

    // Draw tick label
    fillText(labelX, labelY, tickValue.toInt().toString()) .. fill(Color(0x000000u))
}