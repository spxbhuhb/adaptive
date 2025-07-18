package `fun`.adaptive.sandbox.recipe.ui.canvas

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.producer.poll
import `fun`.adaptive.graphics.canvas.api.canvas
import `fun`.adaptive.graphics.canvas.api.circle
import `fun`.adaptive.graphics.canvas.api.fill
import `fun`.adaptive.graphics.canvas.api.fillText
import `fun`.adaptive.graphics.canvas.api.line
import `fun`.adaptive.graphics.canvas.api.path
import `fun`.adaptive.graphics.canvas.api.rotate
import `fun`.adaptive.graphics.canvas.api.stroke
import `fun`.adaptive.graphics.canvas.api.transform
import `fun`.adaptive.graphics.canvas.api.translate
import `fun`.adaptive.graphics.canvas.model.path.Arc
import `fun`.adaptive.graphics.canvas.model.path.LineTo
import `fun`.adaptive.graphics.canvas.model.path.MoveTo
import `fun`.adaptive.graphics.canvas.model.path.PathCommand
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.flowBox
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.borders
import kotlinx.datetime.Clock.System.now
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
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
    // Angle range for the meter (180 degrees, with 0 on the left, 240 on the right, and 120 at the top)
    val startAngle = PI
    val endAngle = 0.0
    
    // Ratios for the different zones (as percentage of the full range)
    val greenRatio = 0.5  // First 50% is green
    val yellowRatio = 0.8 // Next 30% is yellow
    val redRatio = 1.0    // Last 20% is red
    
    // Current value to display on the meter
    val currentValue = poll(1000.milliseconds) { 
        (now().toEpochMilliseconds() % 240).toDouble()
    } ?: 67.0
    
    // Value range for the meter
    val minValue = 0.0
    val maxValue = 240.0
    
    // Radius configuration
    val outerRadius = 150.0
    val innerRadius = 100.0
    
    // Center position of the meter
    val centerX = 200.0
    val centerY = 200.0
    
    // Number of ticks to display
    val numTicks = 12
    
    // Calculate the angle for the current value
    val valueRatio = (currentValue - minValue) / (maxValue - minValue)
    val valueAngle = startAngle + (endAngle - startAngle) * valueRatio
    
    box {
        size(402.dp, 402.dp) .. borders.outline

        canvas {
            maxSize
            
            // Draw the three zones (green, yellow, red) using paths
            
            drawZone(
                centerX,
                centerY,
                outerRadius,
                startAngle,
                startAngle + (endAngle - startAngle) * greenRatio,
                Color(0x00AA00u, 0.5)
            )
            
            drawZone(
                centerX,
                centerY,
                outerRadius,
                startAngle + (endAngle - startAngle) * greenRatio,
                startAngle + (endAngle - startAngle) * yellowRatio,
                Color(0xFFAA00u, 0.5)
            )
            
            drawZone(
                centerX,
                centerY,
                outerRadius,
                startAngle + (endAngle - startAngle) * yellowRatio,
                endAngle,
                Color(0xAA0000u, 0.5)
            )
            
            // Draw the inner circle to create the arc effect
            //circle(centerX, centerY, innerRadius) .. fill(Color(0xFFFFFFu))
            
            // Draw the outer border
            //circle(centerX, centerY, outerRadius) .. stroke(Color(0x000000u))
            
            // Draw the inner border
            //circle(centerX, centerY, innerRadius) .. stroke(Color(0x000000u))
            
            // Draw ticks and labels
            for (i in 0..numTicks) {
                tick(
                    i,
                    numTicks,
                    startAngle,
                    endAngle,
                    minValue,
                    maxValue,
                    centerX,
                    centerY,
                    innerRadius,
                    outerRadius
                )
            }
            
            // Draw the needle
            transform {
                translate(centerX, centerY) .. rotate(valueAngle)
                
                // Needle body
                line(0.0, 0.0, innerRadius - 10, 0.0) .. stroke(Color(0xFF0000u, 0.8))
                
                // Needle point
                line(innerRadius - 10, 0.0, outerRadius - 5, 0.0) .. stroke(Color(0xFF0000u))
                
                // Needle base circle
                circle(0.0, 0.0, 5.0) .. fill(Color(0x000000u))
            }
            
            // Draw the current value text
            fillText(centerX, centerY + 50, "Value: ${currentValue.toInt()}") .. fill(Color(0x000000u))
        }
    }

    return fragment()
}

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
                x1 = startX,
                y1 = startY,
                x2 = endX,
                y2 = endY
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