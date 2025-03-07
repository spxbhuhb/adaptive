package `fun`.adaptive.graphics.chart

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.graphics.canvas.api.*
import `fun`.adaptive.graphics.chart.model.*
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.color
import `fun`.adaptive.ui.api.margin
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Orientation
import `fun`.adaptive.ui.instruction.layout.PopupAlign
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textColors


val xAxis = ChartAxis(
    Orientation.Horizontal,
    400.0,
    ChartLabel(0.0, "X axis", 0.0, emptyInstructions),
    axisLine = true,
    ticks50100(400.0),
    labels50100(400.0),
    emptyList() // guides50100(400.0, 400.0)
)

val yAxis = ChartAxis(
    Orientation.Vertical,
    400.0,
    ChartLabel(0.0, "Y axis", 0.0, emptyInstructions),
    axisLine = false,
    emptyList(), //ticks50100(400.0),
    labels50100(400.0, reverse = true),
    guides50100(400.0, 400.0)
)

val series1 = ChartSeries(
    color(0xff00ff),
    listOf(
        ChartPoint(0.0, 0.0),
        ChartPoint(100.0, 100.0),
        ChartPoint(200.0, 50.0),
        ChartPoint(300.0, 50.0)
    ),
    0,
    4
)

val series2 = ChartSeries(
    color(0x00ff00),
    listOf(
        ChartPoint(0.0, 10.0),
        ChartPoint(100.0, 80.0),
        ChartPoint(200.0, 70.0),
        ChartPoint(300.0, 60.0)
    ),
    0,
    4
)

val context = ChartRenderContext(ChartTheme())

@Adaptive
fun basicChart() {
    val guideStroke = stroke(colors.friendly.opaque(0.3f))
    box {
        size(422.dp, 422.dp) .. borders.outline .. margin { 10.dp }

        canvas {
//            transform {
//                translate(100.0, 0.0)
//
//                //dotAndText(100.0, 40.0, null)
//                line(80.0, 0.0, 80.0, 400.0) .. guideStroke
//                line(160.0, 0.0, 160.0, 400.0) .. guideStroke
//
//                line(0.0, 80.0, 400.0, 80.0) .. guideStroke
//                line(0.0, 120.0, 400.0, 120.0) .. guideStroke
//                line(0.0, 160.0, 400.0, 160.0) .. guideStroke
//                line(0.0, 200.0, 400.0, 200.0) .. guideStroke
//                line(0.0, 240.0, 400.0, 240.0) .. guideStroke
//
//                dotAndText(200.0, 40.0, popupAlign.centerCenter)
//
//                dotAndText(80.0, 80.0, popupAlign.beforeAbove)
//                dotAndText(80.0, 120.0, popupAlign.beforeTop)
//                dotAndText(80.0, 160.0, popupAlign.beforeCenter)
//                dotAndText(80.0, 200.0, popupAlign.beforeBottom)
//                dotAndText(80.0, 240.0, popupAlign.beforeBelow)
//
//                dotAndText(160.0, 80.0, popupAlign.aboveBefore)
//                dotAndText(160.0, 120.0, popupAlign.aboveStart)
//                dotAndText(160.0, 160.0, popupAlign.aboveCenter)
//                dotAndText(160.0, 200.0, popupAlign.aboveEnd)
//                dotAndText(160.0, 240.0, popupAlign.aboveAfter)
//
//                dotAndText(160.0, 80.0, popupAlign.aboveBefore)
//                dotAndText(160.0, 120.0, popupAlign.aboveStart)
//                dotAndText(160.0, 160.0, popupAlign.aboveCenter)
//                dotAndText(160.0, 200.0, popupAlign.aboveEnd)
//                dotAndText(160.0, 240.0, popupAlign.aboveAfter)
//            }

            //fillText(40.0, 40.0, "(40,40)") .. fill(0xff00ff)

            basicAxis(context, xAxis) .. translate(50.0, 361.0)
            basicAxis(context, yAxis) .. translate(49.0, - 40.0)

            transform {
                translate(50.0, 360.0)
                for (series in listOf(series1, series2)) {
                    basicLineSeries(context, series)
                }
            }
        }
    }
}

@Adaptive
fun dotAndText(x: Double, y: Double, alignment: PopupAlign?) {
    val text = alignment?.let { "${it.horizontal?.name}, ${it.vertical?.name}" } ?: "no alignment"

    transform {
        translate(x, y)

        circle(0.0, 0.0, 1.0) .. fill(0x000000)
        fillText(0.0, 00.0, text, alignment) .. fill(colors.angry)
    }
}

fun ticks50100(size: Double): List<ChartTick> {
    val count = ((size / 50) - 1).toInt()
    val out = mutableListOf<ChartTick>()

    for (i in 1 .. count) {
        val offset = i * 50.0
        val size = if (i % 2 == 0) 8.0 else 4.0
        out += ChartTick(offset, size)
    }

    return out
}

fun labels50100(size: Double, reverse: Boolean = false): List<ChartLabel> {
    val count = ((size / 50) - 1).toInt()
    val out = mutableListOf<ChartLabel>()

    for (i in 1 .. count) {
        val offset = if (reverse) (size - i * 50.0) else i * 50.0
        if (i % 2 == 0) {
            out += ChartLabel(offset, "${i * 50}", instructions = emptyInstructions)
        } else {
            out += ChartLabel(offset, "${i * 50}", instructions = instructionsOf(fill(colors.friendly.opaque(0.7f))))
        }
    }

    return out
}

fun guides50100(axisSize: Double, guideSize: Double): List<ChartGuide> {
    val count = ((axisSize / 50) - 1).toInt()
    val out = mutableListOf<ChartGuide>()

    for (i in 1 .. count) {
        val offset = i * 50.0
        out += ChartGuide(offset, guideSize)
    }

    return out
}

