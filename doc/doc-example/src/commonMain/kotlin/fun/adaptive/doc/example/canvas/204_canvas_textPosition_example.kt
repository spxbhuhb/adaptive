package `fun`.adaptive.doc.example.canvas

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.graphics.canvas.api.*
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.margin
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.popupAlign
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.PopupAlign
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors

/**
 * # Positioning text
 *
 * > [!IMPORTANT]
 * >
 * > PopupAlign is used here but I'll change the name to something more general
 * > in the future.
 */
@Adaptive
fun canvasTextPositionExample() : AdaptiveFragment {

    val guideStroke = stroke(colors.friendly.opaque(0.3))

    box {
        size(422.dp, 422.dp) .. borders.outline .. margin { 10.dp }

        canvas {
            maxSize

            transform {
                translate(100.0, 0.0)

                //dotAndText(100.0, 40.0, null)
                line(80.0, 0.0, 80.0, 400.0) .. guideStroke
                line(160.0, 0.0, 160.0, 400.0) .. guideStroke

                line(0.0, 80.0, 400.0, 80.0) .. guideStroke
                line(0.0, 120.0, 400.0, 120.0) .. guideStroke
                line(0.0, 160.0, 400.0, 160.0) .. guideStroke
                line(0.0, 200.0, 400.0, 200.0) .. guideStroke
                line(0.0, 240.0, 400.0, 240.0) .. guideStroke

                dotAndText(200.0, 40.0, popupAlign.centerCenter)

                dotAndText(80.0, 80.0, popupAlign.beforeAbove)
                dotAndText(80.0, 120.0, popupAlign.beforeTop)
                dotAndText(80.0, 160.0, popupAlign.beforeCenter)
                dotAndText(80.0, 200.0, popupAlign.beforeBottom)
                dotAndText(80.0, 240.0, popupAlign.beforeBelow)

                dotAndText(160.0, 80.0, popupAlign.aboveBefore)
                dotAndText(160.0, 120.0, popupAlign.aboveStart)
                dotAndText(160.0, 160.0, popupAlign.aboveCenter)
                dotAndText(160.0, 200.0, popupAlign.aboveEnd)
                dotAndText(160.0, 240.0, popupAlign.aboveAfter)

                dotAndText(160.0, 80.0, popupAlign.aboveBefore)
                dotAndText(160.0, 120.0, popupAlign.aboveStart)
                dotAndText(160.0, 160.0, popupAlign.aboveCenter)
                dotAndText(160.0, 200.0, popupAlign.aboveEnd)
                dotAndText(160.0, 240.0, popupAlign.aboveAfter)
            }

        }
    }

    return fragment()
}


@Adaptive
fun dotAndText(x: Double, y: Double, alignment: PopupAlign?) {
    val text = alignment?.let { "${it.horizontal?.name}, ${it.vertical?.name}" } ?: "no alignment"

    transform {
        translate(x, y)

        circle(0.0, 0.0, 1.0) .. fill(0x000000)
        fillText(0.0, 0.0, text, alignment) .. fill(colors.angry)
    }
}
